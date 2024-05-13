<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<!DOCTYPE HTML>

<html>

<head>

<!-- meta tag -->
<%@ include file="/decorators/meta.jsp" %>

<title>MVSAT APMS</title>

<script type="text/javascript">

	let sun = '<spring:message code="rdata.sun" />';
	let mon = '<spring:message code="rdata.mon" />';
	let tue = '<spring:message code="rdata.tue" />';
	let wed = '<spring:message code="rdata.wed" />';
	let thu = '<spring:message code="rdata.thu" />';
	let fri = '<spring:message code="rdata.fri" />';
	let sat = '<spring:message code="rdata.sat" />';
	let noDayOfUse = '<spring:message code="rdata.noDayOfUse" />';
	
	let downloadExcelMessage = '<spring:message code="confirm.downloadExcel" />';
	let downloadCanceledMessage = '<spring:message code="confirm.downloadCanceled" />';
	let success = '<spring:message code="confirm.success" />';
	let del = '<spring:message code="confirm.delete" />';
	let error = '<spring:message code="confirm.error" />';
	let existsPolicyName = '<spring:message code="confirm.exists.policyName" />';
	let enterMonthlySupply = '<spring:message code="confirm.enter.monthlySupply" />';
	let wrongMonthlySupply = '<spring:message code="confirm.wrong.monthlySupply" />';
	let wrongMonthlySupplyUpTo = '<spring:message code="confirm.wrong.monthlySupplyUpTo" />';
	let wrongdailyUse = '<spring:message code="confirm.wrong.dailyUse" />';
	let wrongContinuousUse = '<spring:message code="confirm.wrong.continuousUse" />';
	let wrongContinuousUseTime = '<spring:message code="confirm.wrong.continuousUseTime" />';
	let selectStartPeriod = '<spring:message code="confirm.select.startPeriod" />';
	let selectEndPeriod = '<spring:message code="confirm.select.endPeriod" />';
	let selectShip = '<spring:message code="confirm.select.ship" />';
	let wrongPeriodOfUse = '<spring:message code="confirm.wrong.periodOfUse" />';
	let enterDataUsagePolicy = '<spring:message code="confirm.enter.dataUsagePolicy" />';
	let deleteDataUsagePolicy = '<spring:message code="confirm.delete.dataUsagePolicy" />';
	let select = '<spring:message code="select.select" />';

</script>

<script type="text/javascript" src="/web/js/rdata/rdata.js"></script>

</head>

<body>

	<jsp:include page="../header.jsp" flush="false"/>
	
	<section class="contents-area">
    	<header>
        	<i class="icon-list"><span>icon</span></i>
        	<h2><spring:message code="rdata.dataUsagePolicy" /></h2>
    	</header>
        <form name="searchRdataForm" id="searchRdataForm" method="post">
        <input type="hidden" id="pageno" name="pageno" value="${rdataVo.pageno }">
        <div class="search-area">
        	<select class="w85" id="searchCompId" name="searchCompId">
           		<option value="" ${param.searchCompId == '' ? 'selected' : ''}><spring:message code="select.shipOwner" /></option>
               	<c:forEach items="${compList}" var="compItem">
                	<option value="${compItem.comp_id}" ${param.searchCompId == compItem.comp_id ? 'selected' : ''}>
                        <c:out value="${compItem.comp_name}" />
                    </option>
                </c:forEach>
      		</select>
            <input type="text" id="searchSName" class="w150" name="searchSName" 
            	placeholder="<spring:message code="search.enterShipName" />" value="${param.searchSName}">
            <input type="text" id="searchRateName" class="w180" name="searchRateName" 
            	placeholder="<spring:message code="search.enterDataUsagePolicy" />" value="${param.searchRateName}">
            <button type="button" name="search" id="btnSearch" class="btn-md white">
            	<span><spring:message code="button.search" /></span>
            </button>
       </div>
       <div class="board-area" >
       		<div class="scroll-x">
            	<table class="tbl-default bg-on">
                                <colgroup>
                                    <col style="width:150px;">
                                    <col style="width:150px;">
                                    <col style="width:250px;">
                                    <col style="width:150px;">
                                    <col style="width:150px;">
                                    <col style="width:150px;">
                                    <col style="width:150px;">
                                    <col style="width:150px;">
                                    <col style="width:150px;">
                                    <col style="width:150px;">
                                    <col style="width:140px;">
                                    <col style="width:200px;">
                                </colgroup>
                             <thead>
                                <tr >
                                  <th scope="col"><spring:message code="select.shipOwner" /></th>
                                  <th scope="col"><spring:message code="select.shipName" /></th>
                                  <th scope="col"><spring:message code="list.dataUsagePolicy" /></th>
                                  <th scope="col"><spring:message code="list.monthlySupply" /></th>
                                  <th scope="col"><spring:message code="list.dailyUseLimit" /></th>
                                  <th scope="col"><spring:message code="list.continuousUseLimit" /></th>
                                  <th scope="col"><spring:message code="list.continuousUseTimeout" /></th>
                                  <th scope="col"><spring:message code="list.usageTimeLimitRange" /></th>
                                  <th scope="col"><spring:message code="list.usageTimeLimitRange2" /></th>
                                  <th scope="col"><spring:message code="list.usageTimeLimitRange3" /></th>
                                  <th scope="col"><spring:message code="list.dayOfUse" /></th>
                                  <th scope="col"><spring:message code="list.periodOfUse" /></th>
                                </tr>
                              </thead>
                              <tbody id="rdataTableBody">
                                <c:choose>
                                    <c:when test="${fn:length(rdataList) > 0 }">
                                        <c:forEach var="item" items="${rdataList }" varStatus="idx">
                                            <tr id="list_${item.s_code }" onClick="goUpdate('${item.s_code}','${item.rate_name}')">
                                                <td>${item.comp_name}</td>
                                                <td>${item.s_name }</td>
                                                <td>${item.rate_name }</td>
                                                <td><fmt:formatNumber value="${item.amt_total_month}" groupingUsed="true" /></td>
                                                <td><fmt:formatNumber value="${item.limit_day_amt}" groupingUsed="true" /></td>
                                                <td><fmt:formatNumber value="${item.limit_cont_amt}" groupingUsed="true" /></td>
                                                <td>${item.limit_cont_time }</td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${item.time_from == '00' && item.time_to == '00'}">
                                                            <spring:message code="rdata.noLimit" />
                                                        </c:when>
                                                        <c:otherwise>
                                                            ${item.time_from}~${item.time_to}
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${item.time_from2 == '00' && item.time_to2 == '00'}">
                                                            <spring:message code="rdata.noLimit" />
                                                        </c:when>
                                                        <c:otherwise>
                                                            ${item.time_from2}~${item.time_to2}
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${item.time_from3 == '00' && item.time_to3 == '00'}">
                                                            <spring:message code="rdata.noLimit" />
                                                        </c:when>
                                                        <c:otherwise>
                                                            ${item.time_from3} ~ ${item.time_to3}
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                               <td id="applyDay_${idx.index}" style="word-break:break-all">${item.apply_day}</td>
                                                <td>
                                                    ${fn:substring(item.day_from, 0, 4)}-${fn:substring(item.day_from, 4, 6)}-${fn:substring(item.day_from, 6, 8)}~${fn:substring(item.day_to, 0, 4)}-${fn:substring(item.day_to, 4, 6)}-${fn:substring(item.day_to, 6, 8)}
                                                </td>
                                            </tr>
                                        </c:forEach>
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
                            <button type="button" class="btn-lg blue" data-bs-toggle="modal" data-bs-target="#addModal">
                            	<span><spring:message code="button.add" /></span>
                            </button>

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
     <!-- 추가 Modal -->
            <div class="modal fade" id="addModal" data-bs-backdrop="static">
                <div class="modal-dialog modal-lg modal-dialog-centered" role="document">
                 <div class="modal-content">
                    <form name="addRdataForm" id="addRdataForm" method="post">
                            <section class="contents-area">
                           
                                <header>
                                    <i class="icon-write"><span>icon</span></i>
                                    <h2><spring:message code="rdata.dataUsagePolicy" /></h2>
                                    <div class="cancelArea" data-bs-dismiss="modal">
						<img class="cancelX" src="../web/images/common/btn-layer-close.png">
					</div>
                                </header>

                                <div class="board-area">
                                    <table class="tbl-write">
                                        <colgroup>
                                        <col style="width:18%;">
                                        <col style="">
                                        <col style="width:15%;">
                                        <col style="width:35%;">
                                        </colgroup>
                                        <tbody>
                                            <tr>
                                                <th>
                                                	<label for="addRateName">
                                                		<spring:message code="rdata.dataUsagePolicy" />
                                                		<span class="key">*</span>
                                                	</label>
                                                </th>
                                                <td colspan="3">
                                                    <input type="text" name="addRateName" id="addRateName" value="" class="width100">
                                                </td>
                                            </tr>
                                            <tr>
                                                <th>
                                                	<label for="addComp">
                                                		<spring:message code="select.shipOwner" /> 
                                                		<span class="key">*</span>
                                                	</label>
                                                </th>
                                                <td>
                                                    <select name="addComp" id="addComp" onChange="updateShipNames(this.value);" class="width55">
                                                        <option value="select"><spring:message code="select.select" /></option>
                                                        <c:forEach items="${compList}" var="compItem">
                                                            <option value="${compItem.comp_id}">${compItem.comp_name}</option>
                                                        </c:forEach>
                                                    </select>
                                                </td>
                                                <th>
                                                	<label for="addName">
                                                		<spring:message code="select.shipName" /> 
                                                		<span class="key">*</span>
                                                	</label>
                                                </th>
                                                <td>
                                                    <div id="name_list">
                                                        <select name="addName" id="addName" class="width50">
                                                            <option value="select"><spring:message code="select.select" /></option>
                                                        </select>
                                                    </div>
                                                </td>
                                            </tr>
											<tr>
                                                <th>
                                                	<label for="addMonth">
                                                		<spring:message code="list.monthlySupply" />
                                                		<span class="key">*</span>
                                                	</label>
                                                </th>
                                                <td><input type="text" id="addMonth" name="addMonth" value="" maxlength="" class="width100"></td>
                                                <th>
                                                	<label for="addDay">
                                                		<spring:message code="list.dailyUseLimit" />
                                                	</label>
                                                </th>
                                                <td><input type="text" id="addDay" name="addDay" value="" maxlength="" class="width100"></td>
                                            </tr>
                                            <tr>
                                                <th>
                                                	<label for="addUse">
                                                		<spring:message code="list.continuousUseLimit" />
                                                	</label>
                                                </th>
                                                <td><input type="text" id="addUse" name="addUse" value="" maxlength="" class="width100"></td>
                                                <th>
                                                	<label for="addMin">
                                                		<spring:message code="list.continuousUseTimeout" />
                                                	</label>
                                                </th>
                                                <td><input type="text" id="addMin" name="addMin" value="" maxlength="" class="width100"></td>
                                            </tr>
                                            <tr>
                                                <th>
                                                	<label for="addTimeFrom">
                                                		<spring:message code="list.usageTimeLimitRange" />
                                                		<span class="key">*</span>
                                                	</label>
                                                </th>
                                                <td colspan="3">
                                                    <label for="addTimeFrom" class="mr10">
                                                    	<spring:message code="list.range1" />
                                                    </label>
                                                    <select name="addTimeFrom" id="addTimeFrom">
                                                       <c:forEach begin="0" end="23" var="hour">
                                                            <c:set var="formattedHour" value="${hour < 10 ? '0' : ''}${hour}" />
                                                            <option value="${formattedHour}">${formattedHour}</option>
                                                        </c:forEach>
                                                    </select>
                                                    ~
                                                      <select name="addTimeTo" id="addTimeTo" class="mr25">
                                                          <c:forEach begin="0" end="23" var="hour">
                                                              <c:set var="formattedHour" value="${hour < 10 ? '0' : ''}${hour}" />
                                                              <option value="${formattedHour}">${formattedHour}</option>
                                                          </c:forEach>
                                                      </select>

                                                    </select>
                                                   <label for="addTimeFrom2" class="mr10">
                                                     <spring:message code="list.range2" />
                                                   </label>
                                                   <select name="addTimeFrom2" id="addTimeFrom2">
                                                      <c:forEach begin="0" end="23" var="hour">
                                                           <c:set var="formattedHour" value="${hour < 10 ? '0' : ''}${hour}" />
                                                           <option value="${formattedHour}">${formattedHour}</option>
                                                       </c:forEach>
                                                   </select>
                                                    ~
                                                      <select name="addTimeTo2" id="addTimeTo2" class="mr25">
                                                          <c:forEach begin="0" end="23" var="hour">
                                                              <c:set var="formattedHour" value="${hour < 10 ? '0' : ''}${hour}" />
                                                              <option value="${formattedHour}">${formattedHour}</option>
                                                          </c:forEach>
                                                      </select>
                                                       <label for="addTimeFrom3" class="mr10">
                                                       	 <spring:message code="list.range3" />
                                                       </label>
                                                       <select name="addTimeFrom3" id="addTimeFrom3">
                                                          <c:forEach begin="0" end="23" var="hour">
                                                               <c:set var="formattedHour" value="${hour < 10 ? '0' : ''}${hour}" />
                                                               <option value="${formattedHour}">${formattedHour}</option>
                                                           </c:forEach>
                                                       </select>
                                                    ~
                                                      <select name="addTimeTo3" id="addTimeTo3" class="mr25">
                                                          <c:forEach begin="0" end="23" var="hour">
                                                              <c:set var="formattedHour" value="${hour < 10 ? '0' : ''}${hour}" />
                                                              <option value="${formattedHour}">${formattedHour}</option>
                                                          </c:forEach>
                                                      </select>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th>
                                                	<spring:message code="list.dayOfUse" />
                                                	<span class="key">*</span>
                                                </th>
                                                <td colspan="3">
                                                    <span class="check-box1">
                                                        <input type="checkbox" id="add_apply_day1" name="add_apply_day" value="1">
                                                        <label for="add_apply_day1"><spring:message code="rdata.day.sun" /></label>
                                                    </span>
                                                    <span class="check-box1">
                                                        <input type="checkbox" id="add_apply_day2" name="add_apply_day" value="2">
                                                        <label for="add_apply_day2"><spring:message code="rdata.day.mon" /></label>
                                                    </span>
                                                    <span class="check-box1">
                                                        <input type="checkbox" id="add_apply_day3" name="add_apply_day" value="3">
                                                        <label for="add_apply_day3"><spring:message code="rdata.day.tue" /></label>
                                                    </span>
                                                    <span class="check-box1">
                                                        <input type="checkbox" id="add_apply_day4" name="add_apply_day" value="4">
                                                        <label for="add_apply_day4"><spring:message code="rdata.day.wed" /></label>
                                                    </span>
                                                    <span class="check-box1">
                                                        <input type="checkbox" id="add_apply_day5" name="add_apply_day" value="5">
                                                        <label for="add_apply_day5"><spring:message code="rdata.day.thu" /></label>
                                                    </span>
                                                    <span class="check-box1">
                                                        <input type="checkbox" id="add_apply_day6" name="add_apply_day" value="6">
                                                        <label for="add_apply_day6"><spring:message code="rdata.day.fri" /></label>
                                                    </span>
                                                    <span class="check-box1">
                                                        <input type="checkbox" id="add_apply_day7" name="add_apply_day" value="7">
                                                        <label for="add_apply_day7"><spring:message code="rdata.day.sat" /></label>
                                                    </span>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th>
                                                	<label for="addDayFrom">
                                                		<spring:message code="list.periodOfUse" />
                                                		<span class="key">*</span>
                                                	</label>
                                                </th>
                                                <td colspan="3">
                                                    <input type="text" readonly name="addDayFrom" id="addDayFrom" maxlength="" value=""
                                                    class="inp-date inp-date-picker">
                                                    ~
                                                    <input type="text" readonly name="addDayTo" id="addDayTo" maxlength="" value=""
                                                    class="inp-date inp-date-picker ">
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
                                            <button type="button"  id="btnAdd" class="btn-lg burgundy">
                                            	<span><spring:message code="button.save" /></span>
                                            </button>
                                            <button type="button" name="cancel" class="btn-lg gray btn-modal-close" data-bs-dismiss="modal">
                                            	<span><spring:message code="button.cancel" /></span>
                                            </button>
                                        </div>
                                    </div>
                                </div>
                             </section>
                         </form>
                     </div>
                </div>
            </div>




            <!-- 수정 Modal -->
                    <div class="modal fade" id="updateModal" data-bs-backdrop="static">
                        <div class="modal-dialog modal-lg modal-dialog-centered" role="document">
                         <div class="modal-content">
                            <form name="updRdataForm" id="updRdataForm" method="post">
                            <input type="hidden" name="updSCode" id="updSCode">
                            <input type="hidden" name="chkDel" id="chkDel">
                                    <section class="contents-area">
                                        <header>
                                            <i class="icon-write"><span>icon</span></i>
                                            <h2><spring:message code="rdata.dataUsagePolicy" /></h2>
                                                <div class="cancelArea" data-bs-dismiss="modal">
													<img class="cancelX" src="../web/images/common/btn-layer-close.png">
												</div>
                                        </header>

                                        <div class="board-area">
                                            <table class="tbl-write">
                                                <colgroup>
                                                <col style="width:18%;">
                                                <col style="">
                                                <col style="width:15%;">
                                                <col style="width:35%;">
                                                </colgroup>
                                                <tbody>
                                                    <tr>
                                                        <th>
                                                        	<label for="updRateName">
                                                        		<spring:message code="rdata.dataUsagePolicy" /> 
                                                        		<span class="key">*</span>
                                                        	</label>
                                                        </th>
                                                        <td colspan="3">
                                                            <span id="updRateName" class="width100"></span>
                                                            <input type="hidden" name="u_updRateName" id="u_updRateName">

                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <th>
                                                        	<label for="updComp">
                                                        		<spring:message code="select.shipOwner" />  
                                                        		<span class="key">*</span>
                                                        	</label>
                                                        </th>
                                                        <td>
                                                            <span id="updComp" class="width100"></span>
                                                            <input type="hidden" name="u_updComp" id="u_updComp">
                                                        </td>
                                                        <th>
                                                        	<label for="updName">
                                                        		<spring:message code="select.shipName" /> 
                                                        		<span class="key">*</span>
                                                        	</label>
                                                        </th>
                                                        <td>
                                                              <span id="updName" class="width100"></span>
                                                            <input type="hidden" name="u_updName" id="u_updName">
                                                        </td>
                                                    </tr>
													<tr>
                                                        <th>
                                                        	<label for="updMonth">
                                                        		<spring:message code="list.monthlySupply" /> 
                                                        		<span class="key">*</span>
                                                        	</label>
                                                        </th>
                                                        <td><input type="text" id="updMonth" name="updMonth" value="" maxlength="" class="width100"></td>
                                                        <th>
                                                        	<label for="updDay">
                                                        		<spring:message code="list.dailyUseLimit" />
                                                        	</label>
                                                        </th>
                                                        <td><input type="text" id="updDay" name="updDay" value="" maxlength="" class="width100"></td>
                                                    </tr>
                                                    <tr>
                                                        <th>
                                                        	<label for="updUse">
                                                        		<spring:message code="list.continuousUseLimit" />
                                                        	</label>
                                                        </th>
                                                        <td><input type="text" id="updUse" name="updUse" value="" maxlength="" class="width100"></td>
                                                        <th>
                                                        	<label for="updMin">
                                                        		<spring:message code="list.continuousUseTimeout" />
                                                        	</label>
                                                        </th>
                                                        <td><input type="text" id="updMin" name="updMin" value="" maxlength="" class="width100"></td>
                                                    </tr>
                                                    <tr>
                                                        <th>
                                                        	<label for="updTimeFrom">
                                                        		<spring:message code="list.usageTimeLimitRange" />
                                                        		<span class="key">*</span>
                                                        	</label>
                                                        </th>
                                                        <td colspan="3">
                                                            <label for="updTimeFrom" class="mr10"><spring:message code="list.range1" /></label>
                                                            <select name="updTimeFrom" id="updTimeFrom">
                                                               <c:forEach begin="0" end="23" var="hour">
                                                                    <c:set var="formattedHour" value="${hour < 10 ? '0' : ''}${hour}" />
                                                                    <option value="${formattedHour}">${formattedHour}</option>
                                                                </c:forEach>
                                                            </select>
                                                            ~
                                                              <select name="updTimeTo" id="updTimeTo" class="mr25">
                                                                  <c:forEach begin="0" end="23" var="hour">
                                                                      <c:set var="formattedHour" value="${hour < 10 ? '0' : ''}${hour}" />
                                                                      <option value="${formattedHour}">${formattedHour}</option>
                                                                  </c:forEach>
                                                              </select>

                                                            </select>
                                                           <label for="updTimeFrom2" class="mr10"><spring:message code="list.range2" /></label>
                                                           <select name="updTimeFrom2" id="updTimeFrom2">
                                                              <c:forEach begin="0" end="23" var="hour">
                                                                   <c:set var="formattedHour" value="${hour < 10 ? '0' : ''}${hour}" />
                                                                   <option value="${formattedHour}">${formattedHour}</option>
                                                               </c:forEach>
                                                           </select>
                                                            ~
                                                              <select name="updTimeTo2" id="updTimeTo2" class="mr25">
                                                                  <c:forEach begin="0" end="23" var="hour">
                                                                      <c:set var="formattedHour" value="${hour < 10 ? '0' : ''}${hour}" />
                                                                      <option value="${formattedHour}">${formattedHour}</option>
                                                                  </c:forEach>
                                                              </select>
                                                               <label for="updTimeFrom3" class="mr10"><spring:message code="list.range3" /></label>
                                                               <select name="updTimeFrom3" id="updTimeFrom3">
                                                                  <c:forEach begin="0" end="23" var="hour">
                                                                       <c:set var="formattedHour" value="${hour < 10 ? '0' : ''}${hour}" />
                                                                       <option value="${formattedHour}">${formattedHour}</option>
                                                                   </c:forEach>
                                                               </select>
                                                            ~
                                                              <select name="updTimeTo3" id="updTimeTo3" class="mr25">
                                                                  <c:forEach begin="0" end="23" var="hour">
                                                                      <c:set var="formattedHour" value="${hour < 10 ? '0' : ''}${hour}" />
                                                                      <option value="${formattedHour}">${formattedHour}</option>
                                                                  </c:forEach>
                                                              </select>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <th>
                                                        	<spring:message code="list.dayOfUse" /> 
                                                        	<span class="key">*</span>
                                                        </th>
                                                        <td colspan="3">
                                                            <span class="check-box1">
                                                                <input type="checkbox" id="upd_apply_day1" name="upd_apply_day" value="1">
                                                                <label for="upd_apply_day1"><spring:message code="rdata.day.sun" /></label>
                                                            </span>
                                                            <span class="check-box1">
                                                                <input type="checkbox" id="upd_apply_day2" name="upd_apply_day" value="2">
                                                                <label for="upd_apply_day2"><spring:message code="rdata.day.mon" /></label>
                                                            </span>
                                                            <span class="check-box1">
                                                                <input type="checkbox" id="upd_apply_day3" name="upd_apply_day" value="3">
                                                                <label for="upd_apply_day3"><spring:message code="rdata.day.tue" /></label>
                                                            </span>
                                                            <span class="check-box1">
                                                                <input type="checkbox" id="upd_apply_day4" name="upd_apply_day" value="4">
                                                                <label for="upd_apply_day4"><spring:message code="rdata.day.wed" /></label>
                                                            </span>
                                                            <span class="check-box1">
                                                                <input type="checkbox" id="upd_apply_day5" name="upd_apply_day" value="5">
                                                                <label for="upd_apply_day5"><spring:message code="rdata.day.thu" /></label>
                                                            </span>
                                                            <span class="check-box1">
                                                                <input type="checkbox" id="upd_apply_day6" name="upd_apply_day" value="6">
                                                                <label for="upd_apply_day6"><spring:message code="rdata.day.fri" /></label>
                                                            </span>
                                                            <span class="check-box1">
                                                                <input type="checkbox" id="upd_apply_day7" name="upd_apply_day" value="7">
                                                                <label for="upd_apply_day7"><spring:message code="rdata.day.sat" /></label>
                                                            </span>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <th>
                                                        	<label for="updDayFrom">
                                                        		<spring:message code="list.periodOfUse" /> 
                                                        		<span class="key">*</span>
                                                        	</label>
                                                        </th>
                                                        <td colspan="3">
                                                            <input type="text" readonly name="updDayFrom" id="updDayFrom" maxlength="" value=""
                                                            class="inp-date inp-date-picker">
                                                            ~
                                                            <input type="text" readonly name="updDayTo" id="updDayTo" maxlength="" value=""
                                                            class="inp-date inp-date-picker">
                                                        </td>
                                                    </tr>
                                                </tbody>
                                            </table>
                                        </div>


                                        <div class="board-btm-area">
                                            <div class="btn-area">
                                                <div class="fleft">
                                                    <button type="button" id="btnDel" class="btn-lg yellow">
                                                    	<span><spring:message code="button.delete" /> </span>
                                                    </button>
                                                </div>
                                                <div class="fright">
                                                    <button type="button"  id="btnUpd" class="btn-lg burgundy">
                                                    	<span><spring:message code="button.save" /> </span>
                                                    </button>
                                                    <button type="button" name="cancel" class="btn-lg gray btn-modal-close" data-bs-dismiss="modal">
                                                    	<span><spring:message code="button.cancel" /> </span>
                                                    </button>
                                                </div>
                                            </div>
                                        </div>
                                     </section>

                                                <input type="hidden" name="rate_name" id="rate_name">
                                                <input type="hidden" name="amt_total_month" id="amt_total_month">
                                                <input type="hidden" name="limit_day_amt" id="limit_day_amt">
                                                <input type="hidden" name="limit_cont_amt" id="limit_cont_amt">
                                                <input type="hidden" name="limit_cont_time" id="limit_cont_time">
                                                <input type="hidden" name="time_from" id="time_from">
                                                <input type="hidden" name="time_from2" id="time_from2">
                                                <input type="hidden" name="time_from3" id="time_from3">
                                                <input type="hidden" name="time_to" id="time_to">
                                                <input type="hidden" name="time_to2" id="time_to2">
                                                <input type="hidden" name="time_to3" id="time_to3">
                                                <input type="hidden" name="apply_day" id="apply_day">
                                                <input type="hidden" name="day_from" id="day_from">
                                                <input type="hidden" name="day_to" id="day_to">

                                 </form>
                             </div>
                        </div>
                    </div>

    </html>