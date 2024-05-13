<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE HTML>

<html>

<head>

<!-- meta tag -->
<%@ include file="/decorators/meta.jsp" %>

<title>MVSAT APMS</title>
<fmt:formatDate value="${now}" pattern="yyyy-MM-dd" var="formattedToday" />
<script type="text/javascript" src="/web/js/shipStat/shipStat.js"></script>
<script type="text/javascript">

	let downloadExcelMessage = '<spring:message code="confirm.downloadExcel" />';
	let downloadCanceledMessage = '<spring:message code="confirm.downloadCanceled" />';
	let success = '<spring:message code="confirm.success" />';
	let del = '<spring:message code="confirm.delete" />';
	let error = '<spring:message code="confirm.error" />';
	let selectShipName = '<spring:message code="select.shipName" />';

      window.onload = function() {

                  var search = sessionStorage.getItem("search");

                  //검색일경우 selectbox 유지 아닐경우 초기화

                  if (!search) {

                          $('#cs_ref_comp').prop('checked', true);
                          $('#cs_ref_ship').prop('checked', true);
                      sessionStorage.removeItem("searchCompId");
                      sessionStorage.removeItem("cs_unit");
                      sessionStorage.removeItem("searchSCode");

                  } else {
                      var cs_ref_comp = sessionStorage.getItem("cs_ref_comp") || '${shipStatVo.cs_ref_comp}';
                      var cs_ref_ship = sessionStorage.getItem("cs_ref_ship") || '${shipStatVo.cs_ref_ship}';

                      $('#cs_ref_comp').prop('checked', cs_ref_comp === "true");
                      $('#cs_ref_ship').prop('checked', cs_ref_ship === "true");
                      sessionStorage.removeItem("search");
                  }

                  var reCompId = sessionStorage.getItem("searchCompId");
                  var reSCode = sessionStorage.getItem("searchSCode");

                  var startDate = formatDateString("${shipStatVo.searchStartDate}");
                  var endDate = formatDateString("${shipStatVo.searchEndDate}");

                  $('#searchStartDate').val(startDate);
                  $('#searchEndDate').val(endDate);
                  if (reCompId) {
                      $('#searchCompId').val(reCompId);
                      updateShipNames(reCompId);
                  }
                 if (reSCode) {
                        $('#searchSCode').val(reSCode);
                    chk_ref();
                 }

        };
</script>
</head>
<body>
<jsp:include page="../header.jsp" flush="false"/>
<section class="contents-area">
<header>
	<i class="icon-stat2"><span>icon</span></i>
	<h2><spring:message code="shipstat.shipUsageHistory" /></h2>
</header>
        <form name="searchShipStatForm" id ="searchShipStatForm" method="post">
        <input type="hidden" id="pageno" name="pageno" value="${shipStatVo.pageno }">
	<!-- 검색 영역 -->
	<div class="search-area">
        <select name="cs_unit" size="1" id="cs_unit">
            <option value="hour" ${shipStatVo.cs_unit == 'hour' ? 'selected' : ''}>
            	<spring:message code="select.hourly" />
            </option>
            <option value="day" ${shipStatVo.cs_unit == 'day' ? 'selected' : ''}>
            	<spring:message code="select.daily" />
            </option>
            <option value="week" ${shipStatVo.cs_unit == 'week' ? 'selected' : ''}>
            	<spring:message code="select.weekly" />
            </option>
            <option value="month" ${shipStatVo.cs_unit == 'month' ? 'selected' : ''}>
            	<spring:message code="select.monthly" />
            </option>
        </select>
		<div class="multiple-checkbox">
			<div class="selected"><span><spring:message code="select.selectQueryCriteria" /></span></div>
			<!-- 아래 '선택' 버튼을 누르면 선택값이 <span>영역에 뿌려짐 //-->
            <input type="hidden" name="cs_ref_comp" id="hidden_cs_ref_comp">
            <input type="hidden" name="cs_ref_ship" id="hidden_cs_ref_ship">

			<div class="selectbox">
				<ul>
					<li>
						<span class="check-box1">
							<input type="checkbox" id="cs_ref_comp" checked name="cs_ref" onChange="chk_ref()">
							<label for="cs_ref_comp"><spring:message code="select.shipOwner" /></label>
						</span>
					</li>
					<li>
						<span class="check-box1">
							<input type="checkbox" id="cs_ref_ship" checked name="cs_ref" onChange="chk_ref()">
							<label for="cs_ref_ship"><spring:message code="select.shipName" /></label>
						</span>
					</li>
				</ul>
				<div class="btn-area">
					<button type="button" name="select" onClick="" class="btn-md burgundy cancel">
						<span><spring:message code="button.ok" /></span>
					</button>
					<button type="button" name="cancel" class="btn-md gray cancel">
						<span><spring:message code="button.close" /></span>
					</button>
				</div>
			</div>
		</div>
            <!-- 선주사 선택 -->
            <select name="searchCompId" id="searchCompId" onChange="updateShipNames(this.value);" class="width5">
                <option value="" ${param.searchCompId == '' ? 'selected' : ''}>
                	<spring:message code="select.shipOwner" />
                </option>
                <c:forEach items="${compList}" var="compItem">
                    <option value="${compItem.comp_id}">${compItem.comp_name}</option>
                </c:forEach>
            </select>
		<div id="ship">
             <div id="name_list">
                <select name="searchSCode" id="searchSCode" onChange="" >
                        <option value="select"><spring:message code="select.shipName" /></option>
                    <%-- <c:forEach items="${shipList}" var="shipList">
                        <option value="${shipList.s_code}">${shipList.comp_name} - ${shipList.s_name}</option>
                    </c:forEach> --%>

                </select>
            </div>
		</div>
	</div>
	<div class="search-area">
		<span><strong><spring:message code="search.viewTime" /></strong></span>
        <input type="text" readonly name="searchStartDate" id="searchStartDate" class="inp-date inp-date-picker w150">
           <select name="searchStartHour" id="searchStartHour">
                <c:forEach var="i" begin="0" end="23">
                    <c:set var="formattedHour" value="${i < 10 ? '0' : ''}${i}" />
                 <option value="${formattedHour}" ${formattedHour == shipStatVo.searchStartHour ? 'selected' : ''}>${formattedHour}</option>
                </c:forEach>
           </select>

		<spring:message code="time.hour" /> ~
            <input type="text" readonly name="searchEndDate" id="searchEndDate" maxlength=""  value=""class="inp-date inp-date-picker w150">
            <select name="searchEndHour" id="searchEndHour">
                <c:forEach var="i" begin="0" end="23">
                    <c:set var="formattedHour" value="${i < 10 ? '0' : ''}${i}" />
                    <c:choose>
                        <c:when test="${shipStatVo.searchEndHour == formattedHour || (empty shipStatVo.searchEndHour && formattedHour == '23')}">
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
	<!-- //검색 영역 -->

<div class="board-area" id="div_contents">
        <table class="tbl-default bg-on">
            <colgroup>
            <col style="width:20%;">
            <c:if test="${shipStatVo.cs_ref_comp == 'true'}">
            <col style="width:15%;">
            </c:if>
            <c:if test="${shipStatVo.cs_ref_ship == 'true'}">
            <col style="width:15%;">
            </c:if>
            <col style="width:15%;">
            <col style="width:15%;">
            <col style="width:15%;">
            </colgroup>
            <thead>
                <tr>
                    <th scope="col"><spring:message code="list.creationTime" /></th>
                    <c:if test="${shipStatVo.cs_ref_comp == 'true'}">
                    <th scope="col"><spring:message code="select.shipOwner" /></th>
                    </c:if>
                    <c:if test="${shipStatVo.cs_ref_ship == 'true'}">
                    <th scope="col"><spring:message code="select.shipName" /></th>
                    </c:if>
                    <th scope="col"><spring:message code="list.collectionNumber" /></th>
                    <th scope="col"><spring:message code="list.usage" /></th>
                    <th scope="col"><spring:message code="list.averageUsage" /></th>
                </tr>
            </thead>
                   <tbody id="shipStatTableBody">
                     <c:choose>
                         <c:when test="${fn:length(shipStatList) > 0 }">
                             <c:forEach var="item" items="${shipStatList }" varStatus="idx">
                                 <tr>
                                     <td>${item.mon_time}</td>
                                     <c:if test="${shipStatVo.cs_ref_comp == 'true'}">
                                     <td>${item.comp_name }</td>
                                     </c:if>
                                     <c:if test="${shipStatVo.cs_ref_ship == 'true'}">
                                     <td>${item.s_name }</td>
                                     </c:if>
                                     <td>${item.val1 }</td>
                                     <td><fmt:formatNumber value="${item.val2}" groupingUsed="true" /></td>
                                     <td><fmt:formatNumber value="${item.sumval}" groupingUsed="true" /></td>
                                 </tr>
                             </c:forEach>
                         </c:when>

                         <c:otherwise>
                             <tr>
                                 <td colspan="6">
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

      <div class="board-btm-area">
                <!-- paging -->
                    ${pagingHTML}
                <!-- //paging -->
            <div class="btn-area">
                <div class="fleft">
                </div>
                <div class="fright">
                 <button type="button" name="excel" id="btnExcel" onClick="" class="btn-lg green">
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