<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE HTML>

<html>

<head>

<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>

<!-- meta tag -->
<%@ include file="/decorators/meta.jsp" %>

<title>MVSAT APMS</title>

<fmt:formatDate value="${now}" pattern="yyyy-MM-dd" var="formattedToday"/>

<script type="text/javascript" src="/web/js/crewcdr/crewcdr.js"></script>
<script type="text/javascript">
	
	let downloadExcelMessage = '<spring:message code="confirm.downloadExcel" />';
	let downloadCanceledMessage = '<spring:message code="confirm.downloadCanceled" />';
	let success = '<spring:message code="confirm.success" />';
	let del = '<spring:message code="confirm.delete" />';
	let error = '<spring:message code="confirm.error" />';
	let selectShipName = '<spring:message code="select.shipName" />';
	let listCrewID = '<spring:message code="list.crewID" />';
	
	

     $(document).ready(function() {

            var search = sessionStorage.getItem("search");

            //검색일경우 selectbox 유지 아닐경우 초기화
            if (!search) {
                sessionStorage.removeItem("searchCompId");
                sessionStorage.removeItem("searchSCode");
                sessionStorage.removeItem("searchCrewId");
            } else {
                sessionStorage.removeItem("search");
            }

            var reCompId = sessionStorage.getItem("searchCompId");
            var reSCode = sessionStorage.getItem("searchSCode");
            var reCrewId = sessionStorage.getItem("searchCrewId");

            var startDate = formatDateString("${crewcdrVo.searchStartDate}");
            var chkStartDate = formatDateString("${crewcdrVo.chkSearchStartDate}");
            var endDate = formatDateString("${crewcdrVo.searchEndDate}");
            var chkEndDate = formatDateString("${crewcdrVo.chkSearchEndDate}");
            var chkVal = "${crewcdrVo.chkStartDate}";


            $('#searchStartDate').val(startDate);
            $('#chkSearchStartDate').val(chkStartDate);
            $('#searchEndDate').val(endDate);
            $('#chkSearchEndDate').val(chkEndDate);

            if (reCompId) {
                $('#searchCompId').val(reCompId);
                updateShipNames(reCompId);
            }
            if (reSCode) {
                $('#searchSCode').val(reSCode);
                updateCrewId(reSCode);
            }
            if (reCrewId) {
                $('#searchCrewId').val(reCrewId);
            }
            if(chkVal === 'true'){
                $('#chkStartDate').prop('checked', true);
            }

            $("#btnSearch").on("click", function() {

            sessionStorage.setItem("searchCompId", $('#searchCompId').val());
            sessionStorage.setItem("searchSCode", $('#searchSCode').val());
            sessionStorage.setItem("searchCrewId", $('#searchCrewId').val());
            sessionStorage.setItem("search", "true");

                // 페이지 번호를 1로 설정
                $("#pageno").val(1);

                // 폼 제출
                $("#searchCrewcdrForm").attr("action", "/crewcdr/crewcdr.do");
             $('.contents-area').append('<div class="ajax-loading"></div>');

                $("#searchCrewcdrForm").submit();
          });

          // excel
            $("#btnExcel").bind("click", function() {
                goExcelDown();
            });

         });
</script>
</head>
<body>


<jsp:include page="../header.jsp" flush="false"/>
    <section class="contents-area">
        <header>
            <i class="icon-list"><span>icon</span></i>
            <h2><spring:message code="crewcdr.crewUsageDetailedHistory" /></h2>
        </header>
            <form name="searchCrewcdrForm" id ="searchCrewcdrForm" method="post">
            <input type="hidden" id="pageno" name="pageno" value="${crewcdrVo.pageno }">
            <input type="hidden" id="searchTableName" name="searchTableName" value="${crewcdrVo.searchTableName }">
            <input type="hidden" id="finalSearchStartDate" name="finalSearchStartDate" value="${crewcdrVo.finalSearchStartDate }">
            <input type="hidden" id="finalSearchEndDate" name="finalSearchEndDate" value="${crewcdrVo.finalSearchEndDate }">
            <input type="hidden" id="finalChkSearchStartDate" name="finalChkSearchStartDate" value="${crewcdrVo.finalChkSearchStartDate }">
            <input type="hidden" id="finalChkSearchEndDate" name="finalChkSearchEndDate" value="${crewcdrVo.finalChkSearchEndDate }">

        <!-- 검색 영역 -->
        <div class="search-area">
            <!-- 선주사 선택 -->
            <select name="searchCompId" id="searchCompId" onChange="updateShipNames(this.value);" class="width10">
                <option value="" ${param.searchCompId == '' ? 'selected' : ''}><spring:message code="select.shipOwner" /></option>
                <c:forEach items="${compList}" var="compItem">
                    <option value="${compItem.comp_id}" ${compItem.comp_id == param.searchCompId ? 'selected' : ''}>${compItem.comp_name}</option>
                </c:forEach>
            </select>
             <div id="name_list">
                <select name="searchSCode" id="searchSCode" onChange="updateCrewId(this.value);" >
                    <option value="select"><spring:message code="select.shipName" /></option>
                </select>
            </div>

            <div id="crew">
                <select name="searchCrewId" id="searchCrewId" >
                    <option value="select"><spring:message code="list.crewID" /></option>
                </select>
            </div>
        </div>

        <div class="search-area">
            <span><strong><spring:message code="search.collectionTime" /></strong></span>
    <input type="text" readonly name="searchStartDate" id="searchStartDate" class="inp-date inp-date-picker w150" value="${crewcdrVo.searchStartDate}">

    <select name="searchStartHour" id="searchStartHour">
        <c:forEach var="i" begin="0" end="23">
            <c:set var="formattedHour" value="${i < 10 ? '0' : ''}${i}" />
            <option value="${formattedHour}" ${formattedHour == crewcdrVo.searchStartHour ? 'selected' : ''}>${formattedHour}</option>
        </c:forEach>
    </select> <spring:message code="time.hour" /> ~

    <input type="text" readonly name="searchEndDate" id="searchEndDate" class="inp-date inp-date-picker w150" value="${crewcdrVo.searchEndDate}">

    <select name="searchEndHour" id="searchEndHour">
        <c:forEach var="i" begin="0" end="23">
            <c:set var="formattedHour" value="${i < 10 ? '0' : ''}${i}" />
            <c:choose>
                <c:when test="${crewcdrVo.searchEndHour == formattedHour || (empty crewcdrVo.searchEndHour && formattedHour == '23')}">
                    <option value="${formattedHour}" selected>${formattedHour}</option>
                </c:when>
                <c:otherwise>
                    <option value="${formattedHour}">${formattedHour}</option>
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </select>
            <spring:message code="time.hour" />
        </div>
        <div class="search-area">
            <span class="check-box1">
                <input type="checkbox" name="chkStartDate" id="chkStartDate" value="true">
                <label for="chkStartDate">
                	<strong><spring:message code="search.startTime" /></strong>
                </label>
            </span>
            <input type="text" readonly name="chkSearchStartDate" id="chkSearchStartDate" maxlength=""  class="inp-date inp-date-picker w150">
            <select name="chkSearchStartHour" id="chkSearchStartHour">
               <c:forEach var="i" begin="0" end="23">
                    <c:set var="formattedHour" value="${i < 10 ? '0' : ''}${i}" />
                    <option value="${formattedHour}"${formattedHour == crewcdrVo.chkSearchStartHour ? 'selected' : ''}>${formattedHour}</option>
                </c:forEach>
            </select>
            <spring:message code="time.hour" /> ~
            <input type="text" readonly name="chkSearchEndDate" id="chkSearchEndDate" maxlength=""  class="inp-date inp-date-picker w150">
            <select name="chkSearchEndHour" id="chkSearchEndHour">
                <c:forEach var="i" begin="0" end="23">
                    <c:set var="formattedHour" value="${i < 10 ? '0' : ''}${i}" />
                    <c:choose>
                        <c:when test="${crewcdrVo.chkSearchEndHour == formattedHour || (empty crewcdrVo.chkSearchEndHour && formattedHour == '23')}">
                            <option value="${formattedHour}" selected>${formattedHour}</option>
                        </c:when>
                        <c:otherwise>
                            <option value="${formattedHour}">${formattedHour}</option>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </select>
            <spring:message code="time.hour" />
            <button type="button" name="search" id="btnSearch" class="btn-md white">
            	<span><spring:message code="button.search" /></span>
            </button>
        </div>
        <div class="board-area" id="div_contents">
                <div class="scroll-x">
                <table class="tbl-default bg-on">
                    <colgroup>
                    <col style="width:150px;">
                    <col style="width:180px;">
                    <col style="width:140px;">
                    <col style="width:140px;">
                    <col style="width:140px;">
                    <col style="width:140px;">
                    <col style="width:180px;">
                    <col style="width:180px;">
                    <col style="width:140px;">
                    <col style="width:100px;">
                    <col style="width:150px;">
                    <col style="width:150px;">
                    <col style="width:180px;">
                    </colgroup>
                    <thead>
                        <tr>
                            <th scope="col">MAC</th>
                            <th scope="col"><spring:message code="list.wapName" /></th>
                            <th scope="col"><spring:message code="select.shipOwner" /></th>
                            <th scope="col"><spring:message code="select.shipName" /></th>
                            <th scope="col"><spring:message code="list.crewID" /></th>
                            <th scope="col"><spring:message code="list.crewNickName" /></th>
                            <th scope="col"><spring:message code="list.crewNickName" /></th>
                            <th scope="col"><spring:message code="list.collectionTime" /></th>
                            <th scope="col"><spring:message code="list.usageTime" /></th>
                            <th scope="col"><spring:message code="list.usage" /></th>
                            <th scope="col"><spring:message code="list.cumulativeUsageToday" /></th>
                            <th scope="col"><spring:message code="list.cumulativeMonthlyUsage" /></th>
                            <th scope="col"><spring:message code="list.monthlyRemainingData" /></th>
                        </tr>
                    </thead>
                      <tbody id="">
                        <c:choose>
                            <c:when test="${fn:length(crewcdrList) > 0 }">
                                <c:forEach var="item" items="${crewcdrList }" varStatus="idx">
                                    <tr id="list_${item.s_code }" onClick="goUpdate('${item.s_code}','${item.crew_id}')">
                                        <td>${item.mac}</td>
                                        <td>${item.ap_name }</td>
                                        <td>${item.comp_name }</td>
                                        <td>${item.s_name }</td>
                                        <td>${item.crew_id }</td>
                                        <td>${item.crew_name }</td>
                                        <td>${item.start_time }</td>
                                        <td>${item.end_time }</td>
                                        <td>${item.use_time }</td>
                                        <td><fmt:formatNumber value="${item.amt_use / 1000}" groupingUsed="true" type="number" /></td>
                                        <td><fmt:formatNumber value="${item.amt_acc_day / 1000}" groupingUsed="true" type="number" /></td>
                                        <td><fmt:formatNumber value="${item.amt_acc_mon / 1000}" groupingUsed="true" /></td>
                                        <td><fmt:formatNumber value="${item.amt_bal / 1000}" pattern="#,##0" /></td>

                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:when test="${totalCnt < 0}">
                             <tr>
                               <td colspan="13">
                                    <div class="no-data">
                                        <p>검색 조건을 입력하세요</p>
                                    </div>
                               </td>
                             </tr>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="13">
                                        <div class="no-data">
                                            <p><spring:message code="noData" /></p>
                                        </div>
                                    </td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                      </tbody>
        </table>
        </div>
        </div>

         <div class="board-btm-area">

                <!-- paging -->
                    ${pagingHTML}
                <!-- //paging -->

                <div class="btn-area">
                    <div class="fleft">
                    </div>
                    <div class="fright">
                        <button type="button" name="excel" id="btnExcel" class="btn-lg green">
                        	<span><spring:message code="button.exportToExcel" /></span>
                        </button>
                    </div>
                </div>
          </div>

</form>
</section>
<jsp:include page="../footer.jsp" flush="false"/>
</body>
</html>