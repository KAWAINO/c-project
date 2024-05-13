<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE HTML>

<html>

<head>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- meta tag -->
<%@ include file="/decorators/meta.jsp" %>

<title>MVSAT APMS</title>

<script type="text/javascript">

	let downloadExcelMessage = '<spring:message code="confirm.downloadExcel" />';
	let downloadCanceledMessage = '<spring:message code="confirm.downloadCanceled" />';
	let success = '<spring:message code="confirm.success" />';
	let del = '<spring:message code="confirm.delete" />';
	let error = '<spring:message code="confirm.error" />';
	let existsShipCode = '<spring:message code="confirm.exists.shipCode" />';
	let existsShipName = '<spring:message code="confirm.exists.shipName" />';
	let enterShipCode = '<spring:message code="confirm.enter.shipCode" />';
	let enterShipName = '<spring:message code="confirm.enter.shipName" />';
	let wrongShipCode = '<spring:message code="confirm.wrong.shipCode" />';
	let wrongShipCodeLength = '<spring:message code="confirm.wrong.shipCodeLength" />';
	let selectShipOwner = '<spring:message code="confirm.select.shipOwner" />';
	let deleteShip = '<spring:message code="confirm.delete.ship" />';
	let deleteActiveManagement = '<spring:message code="confirm.delete.activeManagement" />';

</script>

<script type="text/javascript" src="/web/js/shipInfo/shipInfo.js"></script>


</head>


<body>
<jsp:include page="../header.jsp" flush="false"/>
                <form name="searchShipInfoForm" id="searchShipInfoForm" method="post">
    <section class="contents-area">
        <header>
            <i class="icon-list"><span>icon</span></i>
            <h2><spring:message code="shipinfo.shipInfo" /></h2>
        </header>
             <input type="hidden" id="pageno" name="pageno" value="${shipInfoVo.pageno }">
             <input type="hidden" id="sessionCompId" name="sessionCompId" value="${shipInfoVo.sessionCompId }">
            <!-- 검색 영역 -->
        <div class="search-area">

            <!-- 선주사 선택 -->
            <select class="w85" id="searchCompName" name="searchCompName">
                <option value="" ${param.searchCompName == '' ? 'selected' : ''}><spring:message code="select.shipOwner" /></option>
                <c:forEach items="${compList}" var="compItem">
                    <option value="${compItem.comp_id}" ${param.searchCompName == compItem.comp_id ? 'selected' : ''}>
                        <c:out value="${compItem.comp_name}" />
                    </option>
                </c:forEach>
            </select>

            <!-- 선박코드 입력 -->
            <input type="text" id="searchSCode" class="w150" name="searchSCode" 
            	placeholder="<spring:message code="search.enterShipCode" />" value="${param.searchSCode}">

            <!-- 선주사명 입력 -->
            <input type="text" id="searchSName" class="w150" name="searchSName" 
            	placeholder="<spring:message code="search.enterShipName" />" value="${param.searchSName}">

            <!-- OWM 접속상태 선택 -->
            <select class="w125" id="searchApmsStatus" name="searchApmsStatus">
                <option value="" ${param.searchApmsStatus == '' ? 'selected' : ''}>
                	<spring:message code="select.owmConnectionStatus" />
                </option>
                <option value="1" ${param.searchApmsStatus == '1' ? 'selected' : ''}>
                	<spring:message code="select.connected" />
                </option>
                <option value="2" ${param.searchApmsStatus == '2' ? 'selected' : ''}>
                	<spring:message code="select.notConnected" />
                </option>
            </select>

            <!-- 검색 버튼 -->
            <button type="button" name="search" id="btnSearch" class="btn-md white">
            	<span><spring:message code="button.search" /></span>
            </button>

        </div>


<div id ="div_contents" class="board-area" style="display: block;">
<div class="scroll-x">
	      <table class="tbl-default bg-on" >

                               <colgroup>
                                <col style="width:12%;">
                                <col style="width:12%;">
                                <col style="width:10%;">
                                <col style="width:11%;">
                                <col style="width:11%;">
                                <col style="width:10%;">
                                <col style="width:7%;">
                                <col style="">
                                </colgroup>

                                 <thead>
                                    <tr>
                                      <th scope="col"><spring:message code="list.shipCode" /></th>
                                      <th scope="col"><spring:message code="select.shipOwner" /></th>
                                      <th scope="col"><spring:message code="select.shipName" /></th>
                                      <th scope="col"><spring:message code="list.usage.month" /></th>
                                      <th scope="col"><spring:message code="list.usage.today" /></th>
                                      <th scope="col"><spring:message code="list.OWMstatus" /></th>
                                      <th scope="col"><spring:message code="list.wapStatus" /></th>
                                      <th scope="col"><spring:message code="list.description" /></th>
                                    </tr>
                                 </thead>


                                  <tbody id="shipInfoTableBody">
                                    <c:choose>
                                        <c:when test="${fn:length(shipInfoList) > 0 }">
                                            <c:forEach var="item" items="${shipInfoList }" varStatus="idx">
                                                <tr id="list_${item.s_code }" onClick="goUpdate('${item.s_code}')">
                                                    <td>${item.s_code}</td>
                                                    <td>${item.comp_name }</td>
                                                    <td>${item.s_name }</td>
                                                    <td><fmt:formatNumber value="${item.amt_use_month != null ? item.amt_use_month : 0}" groupingUsed="true" /></td>
                                                    <td><fmt:formatNumber value="${item.amt_use_day != null ? item.amt_use_day : 0}" groupingUsed="true" /></td>
                                                    <td>
                                                        <c:if test="${'1' eq item.apms_status}">
                                                            <img src="/web/images/shipInfo/green.png" alt="Green Status" width="15">
                                                        </c:if>
                                                        <c:if test="${'2' eq item.apms_status}">
                                                            <img src="/web/images/shipInfo/red.png" alt="Red Status" width="15">
                                                        </c:if>
                                                    </td>
                                                     <td><fmt:formatNumber value="${item.a_cnt}" groupingUsed="true" /></td>
                                                    <td>${item.descr}</td>
                                                </tr>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            <tr>
                                                <td colspan="8">
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
                       <c:if test="${shipInfoVo.sessionCompId == '0'}">
                    <button type="button" class="btn-lg blue" data-bs-toggle="modal" data-bs-target="#addModal">
                    	<span><spring:message code="button.add" /></span>
                    </button>
                    </c:if>
                </div>
                <div class="fright">
                    <button type="button" name="excel" id="btnExcel" onClick="" class="btn-lg green">
                    	<span><spring:message code="button.exportToExcel" /></span>
                    </button>
                </div>
            </div>
        </div>
       </section>
        </form>

	<jsp:include page="../footer.jsp" flush="false"/>
	
</body>



        <!-- 추가 Modal -->
        <div class="modal fade" id="addModal" data-bs-backdrop="static">
            <div class="modal-dialog modal-lg modal-dialog-centered" role="document">
             <div class="modal-content">
                 <form name="addShipInfoForm" id="addShipInfoForm" method="post">
                 <section class="contents-area">
                    <header>
                        <i class="icon-write"><span>icon</span></i>
                        <h2><spring:message code="shipinfo.shipInfo" /></h2>
                        <div class="cancelArea" data-bs-dismiss="modal">
							<img class="cancelX" src="../web/images/common/btn-layer-close.png">
						</div>
                    </header>

                    <div class="board-area">
                        <table class="tbl-write">
                            <colgroup>
                            <col style="width:15%;">
                            <col style="">
                            <col style="width:15%;">
                            <col style="width:35%;">
                            </colgroup>
                            <tbody>
                                <tr>
                                    <th style="text-align: left;">
                                    	<label for="addCode">
                                    		<spring:message code="list.shipCode" />
                                    	</label> 
                                    	<span class="key">*</span>
                                    </th>
                                    <td>S_ <input type="text" id="addCode" name="addCode" maxlength=5 value="" class="width90" ></td>
                                    <th>
                                    	<label for="addName">
                                    		<spring:message code="select.shipName" />
                                    		<span class="key">*</span>
                                    	</label>
                                    </th>
                                    <td><input type="text" id="addName" name="addName" value="" class="width100"></td>
                                </tr>
                                <tr>
                                    <th>
                                    	<label for="addComp">
                                    		<spring:message code="select.shipOwner" />
                                    	</label> 
                                    	<span class="key">*</span>
                                    </th>
                                    <td colspan=3>
                                        <select name="addCompId" id="addCompId">
                                          <option value="select"><spring:message code="select.select" /></option>
                                           <c:forEach items="${compList}" var="compItem">
                                               <option value="${compItem.comp_id}">
                                                   <c:out value="${compItem.comp_name}" />
                                               </option>
                                           </c:forEach>
                                        </select>
                                    </td>
                                </tr>

                                <tr>
                                    <th><label for="addDesc"><spring:message code="list.description" /></label></th>
                                    <td colspan=3>
                                        <div class="textarea">
                                            <textarea id="addDescr" name="addDescr"></textarea>
                                        </div>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                    <div class="board-btm-area">
                        <div class="btn-area">
                            <div class="fleft">

                            </div>
                            <div class="fright">

                                <button type="button" name="btn_shipcomp_add" id="btnAdd" class="btn-lg burgundy">
                                	<span><spring:message code="button.save" /></span>
                                </button>
                                <button type="button" name="cancel" class="btn-lg gray btn-modal-close" data-bs-dismiss="modal">
                                	<span><spring:message code="button.cancel" /></span>
                                </button>


                            </div>
                        </div>
                    </div>
  </section>
                 </div>
            </div>
                </form>

        </div>




    <!-- 수정 Modal -->
    <div class="modal fade" id="updateModal" data-bs-backdrop="static">
        <div class="modal-dialog modal-lg modal-dialog-centered" role="document">
         <div class="modal-content">
     <form name="updShipInfoForm" id="updShipInfoForm" method="post">
                         <section class="contents-area">
                <header>
                    <i class="icon-write"><span>icon</span></i>
                    <h2><spring:message code="shipinfo.shipInfo" /></h2>
                    <div class="cancelArea" data-bs-dismiss="modal">
						<img class="cancelX" src="../web/images/common/btn-layer-close.png">
					</div>
                </header>

             <input type="hidden" id="u_updCode" name="u_updCode">

             <input type="hidden" id="chkDel" name="chkDel">
                <div class="board-area">
                    <table class="tbl-write">
                         <colgroup>
                         <col style="width:15%;">
                         <col style="">
                         <col style="width:15%;">
                         <col style="width:35%;">
                         </colgroup>
                     <tbody>
                         <tr>
                             <th>
                             	<label for="updCode">
                             		<spring:message code="list.shipCode" />
                             		<span class="key">*</span>
                             	</label>
                             </th>
                             <td><span id="displayUpdCode"></span></td>
                             <input type="hidden" id="updCode" name="updCode" value="" class="width90 no-border-input" readonly>
                             <th>
                             	<label for="updName">
                             		<spring:message code="select.shipName" /> 
                             		<span class="key">*</span>
                             	</label>
                                <input type="hidden" id="u_updName" name="u_updName">
                             </th>
                             <td><input type="text" id="updName" name="updName" value="" class="width100"></td>

                         </tr>
                         <tr>
                         <th>
	                         <label for="updCompId">
	                         	<spring:message code="select.shipOwner" /> 
	                         	<span class="key">*</span> 
	                         </label>
                         </th>
                         <td colspan=3>
                            <select name="updCompId" id="updCompId">
                                <option value=""><spring:message code="select.select" /></option>
                                <c:forEach items="${compList}" var="compItem">
                                    <option value="${compItem.comp_id}"
                                            ${compItem.comp_id == selectedCompId ? 'selected' : ''}>
                                        <c:out value="${compItem.comp_name}" />
                                    </option>
                                </c:forEach>
                            </select>
                         </td>


                         <tr>
                             <th><label for="updDescr"><spring:message code="list.description" /></label></th>
                             <td colspan=3>
                                 <div class="textarea">
                                     <textarea id="updDescr" name="updDescr"></textarea>
                                     <input type="hidden" id="u_updDescr" name="u_updDescr">
                                 </div>
                             </td>
                         </tr>
                     </tbody>
                    </table>
                </div>
                <div class="board-btm-area">
                    <div class="btn-area">
                        <div class="fleft">
                           <c:if test="${shipInfoVo.sessionCompId == '0'}">
                            <button type="button" id="btnDel" class="btn-lg yellow">
                            	<span><spring:message code="button.delete" /></span>
                            </button>
                            </c:if>
                        </div>
                        <div class="fright">
                             <c:if test="${shipInfoVo.sessionCompId == '0'}">
                            <button type="button" name="btn_shipcomp_add" id="btnUpd" class="btn-lg burgundy">
                            	<span><spring:message code="button.save" /></span>
                            </button>
                            </c:if>
                            <button type="button" name="cancel" class="btn-lg gray btn-modal-close" data-bs-dismiss="modal">
                            	<span><spring:message code="button.cancel" /></span>
                            </button>

                        </div>
                    </div>
                </div>
            </form>
             </section>
             </div>
        </div>
    </div>


</html>