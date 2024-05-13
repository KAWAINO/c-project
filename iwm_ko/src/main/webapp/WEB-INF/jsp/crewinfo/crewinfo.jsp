<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<!DOCTYPE HTML>

<html>

<head>

<!-- meta tag -->
<%@ include file="/decorators/meta.jsp" %>

<title>MVSAT APMS</title>

<script type="text/javascript">

	let downloadExcelMessage = '<spring:message code="confirm.downloadExcel" />';
	let downloadCanceledMessage = '<spring:message code="confirm.downloadCanceled" />';
	let success = '<spring:message code="confirm.success" />';
	let del = '<spring:message code="confirm.delete" />';
	let error = '<spring:message code="confirm.error" />';
	let sun = '<spring:message code="rdata.sun" />';
	let mon = '<spring:message code="rdata.mon" />';
	let tue = '<spring:message code="rdata.tue" />';
	let wed = '<spring:message code="rdata.wed" />';
	let thu = '<spring:message code="rdata.thu" />';
	let fri = '<spring:message code="rdata.fri" />';
	let sat = '<spring:message code="rdata.sat" />';
	let noDayOfUse = '<spring:message code="rdata.noDayOfUse" />';
	let select = '<spring:message code="select.select" />';
	let selectAll = '<spring:message code="select.selectAll" />';

	let enterCrewID = '<spring:message code="confirm.enter.crewID" />';
	let enterCrewName = '<spring:message code="confirm.enter.crewName" />';
	let enterPassword = '<spring:message code="confirm.enter.password" />';
	let enterConfirmPassword = '<spring:message code="confirm.enter.confirmPassword" />';
	let wrongPassword = '<spring:message code="confirm.wrong.password" />';
	let selectShip = '<spring:message code="confirm.select.ship" />';
	let selectUsagePolicy = '<spring:message code="confirm.select.usagePolicy" />';
	let enterMonthlySupply = '<spring:message code="confirm.enter.monthlySupply" />';
	let wrongMonthlySupply = '<spring:message code="confirm.wrong.monthlySupply" />';
	let wrongMonthlySupplyUpTo = '<spring:message code="confirm.wrong.monthlySupplyUpTo" />';
	let wrongDailyUse = '<spring:message code="confirm.wrong.dailyUse" />';
	let wrongContinuousUse = '<spring:message code="confirm.wrong.continuousUse" />';
	let wrongContinuousUseTime = '<spring:message code="confirm.wrong.continuousUseTime" />';
	let selectStartPeriod = '<spring:message code="confirm.select.startPeriod" />';
	let selectEndPeriod = '<spring:message code="confirm.select.endPeriod" />';
	let wrongPeriodOfUse = '<spring:message code="confirm.wrong.periodOfUse" />';
	let existsID = '<spring:message code="confirm.exists.ID" />';
	let enterShipCode = '<spring:message code="confirm.enter.shipCode" />';
	let wrongShipCode = '<spring:message code="confirm.wrong.shipCode" />';
	let wrongShipCodeLength = '<spring:message code="confirm.wrong.shipCodeLength" />';
	let runCrewAddBulk = '<spring:message code="confirm.run.crewAddBulk" />';
	let wrongAgainShipCode = '<spring:message code="confirm.wrong.againShipCode" />';
	let deleteCrew = '<spring:message code="confirm.delete.crew" />';
	let confirmBlockWIFI = '<spring:message code="crewInfo.confirm.blockWIFI" />';
	let confirmUnBlockWIFI = '<spring:message code="crewInfo.confirm.unBlockWIFI" />';
	let confirmPolicyInBulk = '<spring:message code="crewInfo.confirm.policyInBulk" />';
	let confirmSaveDefaultPolicy = '<spring:message code="crewInfo.confirm.saveDefaultPolicy" />';
	let confirmBlockCarryOver = '<spring:message code="crewInfo.confirm.blockCarryOver" />';
	let confirmCarryOverFunction = '<spring:message code="crewInfo.confirm.carryOverFunction" />';
	let confirmReturnPolicy = '<spring:message code="crewInfo.confirm.returnPolicy" />';

	
</script>

<script type="text/javascript" src="/web/js/crewInfo/crewInfo.js"></script>

</head>

<body>

	<jsp:include page="../header.jsp" flush="false"/>
	
	<section class="contents-area">
	    <header>
	        <i class="icon-list"><span>icon</span></i>
	        <h2><spring:message code="crewInfo.crewInformation" /></h2>
	    </header>
	    <form name="searchCrewInfoForm" id="searchCrewInfoForm" method="post">
	        <input type="hidden" id="pageno" name="pageno" value="${crewInfoVo.pageno }">
	        <input type="hidden" id="searchBtn" name="searchBtn" value="${crewInfoVo.searchBtn }">
	        <input type="hidden" id="sessionCompId" name="sessionCompId" value="${crewInfoVo.sessionCompId }">

	        <div class="search-area">
			    <select class="w85" id="searchCompId" name="searchCompId">
			        <option value="" ${param.searchCompId == '' ? 'selected' : ''}>
			        	<spring:message code="select.shipOwner" />
			        </option>
			        <c:forEach items="${compList}" var="compItem">
			            <option value="${compItem.comp_id}" ${param.searchCompId == compItem.comp_id ? 'selected' : ''}>
			                <c:out value="${compItem.comp_name}" />
			            </option>
			        </c:forEach>
			    </select>
		        <input type="text" id="searchSName" class="w150" name="searchSName" 
		        	placeholder="<spring:message code="search.enterShipName" />" value="${param.searchSName}">
		        <input type="text" id="searchIdName" class="w200" name="searchIdName" 
		        	placeholder="<spring:message code="search.enterIDorNickname" />" value="${param.searchIdName}">
				<c:if test="${sessionCompId == '46'}">
			        <button type="button" name="cs_setcrew" class="btn-md white fright" onClick="addAllCrew()">
			            <span> 일괄적용</span>
			        </button>
			        <div class="fright">
			            S_<input type="text" name="cs_scode" id="cs_scode" maxlength="5" value="" class="w200" placeholder="선박코드를 입력하여주세요."/>
			        </div>
		       	</c:if>
			 	<button type="button" name="search" id="btnSearch" class="btn-md white">
		 	 		<span><spring:message code="button.search" /></span>
			 	</button>
	        </div>
	    </form>

        <div id ="div_contents" class="board-area" style="display: block;">
        	<div class="scroll-x">
            	<table class="tbl-default bg-on">
                	<colgroup>
	                    <col style="width:150px;">
	                    <col style="width:150px;">
	                    <col style="width:150px;">
	                    <col style="width:100px;">
	                    <col style="width:120px;">
	                    <col style="width:120px;">
	                    <col style="width:120px;">
	                    <col style="width:120px;"><!-- 잔여량 -->
	                    <col style="width:170px;"><!-- here 추가 데이터 잔여량 -->
	                    <col style="width:250px;">
	                    <col style="width:120px;">
	                    <col style="width:170px;">
	                    <col style="width:140px;">
	                    <col style="width:150px;">
	                    <col style="width:140px;">
	                    <col style="width:140px;">
	                    <col style="width:140px;">
	                    <col style="width:150px;">
	                    <col style="width:150px;">
	                    <col style="width:150px;"> <!-- 데이터 이월여부 here -->
	                    <col style="width:200px;">
                   </colgroup>
				   <thead>
	                   <tr class="scroll-x">
		                   <th scope="col"><spring:message code="select.shipOwner" /></th>
		                   <th scope="col"><spring:message code="select.shipName" /></th>
		                   <th scope="col"><spring:message code="list.crewNickName" /></th>
		                   <th scope="col"><spring:message code="list.crewID" /></th>
		                   <th scope="col"><spring:message code="list.status" /></th>
		                   <th scope="col"><spring:message code="list.dailyUsage" /></th>
		                   <th scope="col"><spring:message code="list.monthlyUsage" /></th>
		                   <th scope="col"><spring:message code="list.remainingAmount" /></th>
		                   <th scope="col"><spring:message code="list.addDataRemaining" /></th>
		                   <th scope="col"><spring:message code="rdata.dataUsagePolicy" /></th>
		                   <th scope="col"><spring:message code="list.monthlySupply" /></th>
		                   <th scope="col"><spring:message code="list.dailyUseLimit" /></th>
		                   <th scope="col"><spring:message code="list.continuousUseLimit" /></th>
		                   <th scope="col"><spring:message code="list.continuousUseTimeout" /></th>
		                   <th scope="col"><spring:message code="list.usageTimeLimitRange" /></th>
		                   <th scope="col"><spring:message code="list.usageTimeLimitRange2" /></th>
		                   <th scope="col"><spring:message code="list.usageTimeLimitRange3" /></th>
		                   <th scope="col"><spring:message code="list.dayOfUse" /></th>
		                   <th scope="col"><spring:message code="list.blockUnlockWIFI" /></th>
		                   <th scope="col"><spring:message code="list.dataCarryOver" /></th> <!-- 데이터 이월 여부 -->
		                   <th scope="col"><spring:message code="list.periodOfUse" /></th>
		               </tr>
                   </thead>
                   <tbody id="crewInfoTableBody">
                     <c:choose>
                         <c:when test="${fn:length(crewInfoList) > 0 }">
                             <c:forEach var="item" items="${crewInfoList }" varStatus="idx">
                                 <tr id="list_${item.s_code }" onClick="goUpdate('${item.s_code}','${item.crew_id}')">
                                     <td>${item.comp_name}</td>
                                     <td>${item.s_name }</td>
                                     <td>${item.crew_name }</td>
                                     <td>${item.crew_id }</td>
                                     <td>${item.crew_status }</td>
                                     <td><fmt:formatNumber value="${item.acc_use_day / 1000}" groupingUsed="true" type="number" /></td>
                                     <td><fmt:formatNumber value="${item.acc_use_month / 1000}" groupingUsed="true" type="number" /></td>
                                      <td><fmt:formatNumber value="${item.amt_rest}" groupingUsed="true" /></td>
                                      <td><fmt:formatNumber value="${item.add_remain}" groupingUsed="true" /></td>
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
                                                 ${item.time_from} ~ ${item.time_to}
                                             </c:otherwise>
                                         </c:choose>
                                     </td>
                                     <td>
                                         <c:choose>
                                             <c:when test="${item.time_from2 == '00' && item.time_to2 == '00'}">
                                                 <spring:message code="rdata.noLimit" />
                                             </c:when>
                                             <c:otherwise>
                                                 ${item.time_from2} ~ ${item.time_to2}
                                             </c:otherwise>
                                         </c:choose>
                                     </td>
                                     <td>
                                         <c:choose>
                                             <c:when test="${item.time_from3 == '00' && item.time_to3 == '00'}">
                                                 <spring:message code="rdata.noLimit" />
                                             </c:when>
                                             <c:otherwise>
                                                 ${item.time_from3}~${item.time_to3}
                                             </c:otherwise>
                                         </c:choose>
                                     </td>
                                    <td id="applyDay_${idx.index}" style="word-break:break-all">${item.apply_day}</td>

                                     <td>
                                       <c:if test="${item.wifi_conn == '0'}">
                                          <spring:message code="crewInfo.block" />
                                       </c:if>
                                       <c:if test="${item.wifi_conn == '1'}">
                                          <spring:message code="crewInfo.unBlock" />
                                       </c:if>
                                     </td>
                                     <td>
                                       <c:if test="${item.val2 == 'Y'}">
                                          <spring:message code="crewInfo.use" />
                                       </c:if>
                                       <c:if test="${item.val2 == 'N'}">
                                          <spring:message code="crewInfo.unUse" />
                                       </c:if>
                                     </td>
                                     <td id="dateRange_${idx.index}">${item.day_from} ~ ${item.day_to}</td>
                                 </tr>
                             </c:forEach>
                         </c:when>
                         <c:when test="${totalCnt < 0}">
                          <tr>
                            <td colspan="19">
                                <div class="no-data">
                                    <p>검색 조건을 입력하세요</p>
                                </div>
                            </td>
                          </tr>
                         </c:when>
                         <c:otherwise>
                             <tr>
                                 <td colspan="19">
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
                    <button type="button" class="btn-lg dbrown" data-bs-toggle="modal" data-bs-target="#addAllModal">
                    	<span><spring:message code="button.applyingAllCrew" /></span>
                    </button>
                </div>
                <div class="fright">

                         <c:if test="${sessionCompId == '0'}">
                            <button type="button" class="btn-lg burgundy" data-bs-toggle="modal" data-bs-target="#importModal">
                            	<span><spring:message code="button.excelImport" /></span>
                            </button>
                          </c:if>
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
                    <form name="addCrewInfoForm" id="addCrewInfoForm" method="post">
                            <section class="contents-area">
                                <header>
                                    <i class="icon-write"><span>icon</span></i>
                                    <h2><spring:message code="crewInfo.crewInformation" /></h2>
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
                                                	<label for="addCrewId">
                                                		<spring:message code="list.crewID" /> 
                                                		<span class="key">*</span>
                                                	</label>
                                                </th>
                                                <td>
                                                    <input type="text" name="addCrewId" id="addCrewId" value="" class="width100">
                                                </td>
                                                 <th>
                                                 	<label for="addCrewName">
                                                 		<spring:message code="list.crewNickName" /> 
                                                 		<span class="key">*</span>
                                                 	</label>
                                                 </th>
                                                <td>
                                                    <input type="text" name="addCrewName" id="addCrewName" value="" class="width100">
                                                </td>
                                            </tr>
                                            <tr>
                                                <th>
                                                	<label for="addPw">
                                                		<spring:message code="list.password" />  
                                                		<span class="key">*</span>
                                                	</label>
                                                </th>
                                                <td>
                                                    <input type="password" name="addPw" id="addPw" value="" class="width100">
                                                </td>
                                                 <th>
                                                 	<label for="addPwChk">
                                                 		<spring:message code="list.confirmPassword" /> 
                                                 		<span class="key">*</span>
                                                 	</label>
                                                 </th>
                                                <td>
                                                    <input type="password" name="addPwChk" id="addPwChk" value="" class="width100">
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
                                                    <select name="addComp" id="addComp" onChange="updateShipNames(this.value);" class="width50">
                                                        <option value="select"><spring:message code="select.select" /> </option>
                                                        <c:forEach items="${compList}" var="compItem">
                                                            <option value="${compItem.comp_id}">${compItem.comp_name}</option>
                                                        </c:forEach>
                                                    </select>
                                                </td>
                                                <th>
	                                                <label for="addSName">
		                                                <spring:message code="select.shipName" />  
		                                                <span class="key">*</span>
	                                                </label>
                                                </th>
                                                <td>
                                                    <div id="name_list">
                                                        <select name="addSName" id="addSName" onChange="updateRdataNames(this.value);" class="width50">
                                                            <option value="select"><spring:message code="select.select" /> </option>
                                                        </select>
                                                    </div>
                                                </td>
                                            </tr>
                                             <tr>
                                                <th>
	                                                <label for="addRdata">
		                                                <spring:message code="rdata.dataUsagePolicy" />
		                                                <span class="key"> *</span>
	                                                </label>
                                                </th>
                                                <td>
                                                    <select name="addRdata" id="addRdata"  class="width50" onChange="valList(this.value);">
                                                        <option value="select"><spring:message code="select.select" />  </option>
                                                    </select>
                                                </td>
                                                <th></th>
                                                <td>

                                                </td>
                                            </tr>
                                            <tr>
                                                <th>
	                                                <label for="addVal2">
	                                                	<spring:message code="list.dataCarryOver" />
	                                                </label>
                                                </th>
                                                <td>
                                                    <span class="check-box1">
                                                       <input type="checkbox" id="addVal2" name="addVal2" value="Y">
                                                       <label for="addVal2"><spring:message code="crewInfo.use" /></label>
                                                    </span>

                                                </td>
                                                <th></th>
                                                <td>

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
                                                    <label for="addTimeFrom" class="mr10"><spring:message code="list.range1" /> </label>
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
                                                   <label for="addTimeFrom2" class="mr10"><spring:message code="list.range2" /></label>
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
                                                       <label for="addTimeFrom3" class="mr10"><spring:message code="list.range3" /></label>
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
                                                <th><spring:message code="list.dayOfUse" /><span class="key">*</span></th>
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
                                                    class="inp-date inp-date-picker">
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
                            <form name="updCrewInfoForm" id="updCrewInfoForm" method="post">
                            <input type="hidden" name="u_updCrewId" id="u_updCrewId">
                                    <section class="contents-area">
                                        <header>
                                            <i class="icon-write"><span>icon</span></i>
                                            <h2><spring:message code="crewInfo.crewInformation" /></h2>
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
	                                                        <label for="updCrewId">
		                                                        <spring:message code="list.crewID" /> 
		                                                        <span class="key">*</span>
	                                                        </label>
                                                        </th>
                                                        <td>
                                                            <span id="updCrewId"> </span>
                                                        </td>
                                                         <th>
	                                                         <label for="updCrewName">
		                                                         <spring:message code="list.crewNickName" /> 
		                                                         <span class="key">*</span>
	                                                         </label>
                                                         </th>
                                                        <td>
                                                            <input type="text" name="updCrewName" id="updCrewName" value="" class="width100">
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <th>
	                                                        <label for="updPw">
		                                                        <spring:message code="list.password" /> 
		                                                        <span class="key">*</span>
	                                                        </label>
                                                        </th>
                                                        <td>
                                                            <input type="password" name="updPw" id="updPw" value="" class="width100" disabled>
                                                            <div class="mt05">
                                                                <span class="check-box1">
                                                                    <input type="checkbox" id="changePw" name="changePw" value="Y" />
                                                                    <label for="changePw" class="fs-11"><spring:message code="cuser.change" /></label>
                                                                </span>
                                                            </div>

                                                        </td>
                                                         <th>
	                                                         <label for="updPwChk">
		                                                         <spring:message code="list.confirmPassword" />
		                                                         <span class="key">*</span>
	                                                         </label>
                                                         </th>
                                                        <td>
                                                            <input type="password" name="updPwChk" id="updPwChk" value="" class="width100" disabled>
                                                        </td>
                                                    </tr>

                                                    <tr>
                                                         <th>
	                                                         <label for="updCompName">
		                                                         <spring:message code="select.shipOwner" /> 
		                                                         <span class="key">*</span>
	                                                         </label>
                                                         </th>
                                                       <td>
                                                           <span id ="updCompName"></span>
                                                           <input type="hidden" name="u_updCompName" id="u_updCompName" value="" class="width100">
                                                       </td>
                                                         <th>
	                                                         <label for="updSName">
		                                                         <spring:message code="select.shipName" /> 
		                                                         <span class="key">*</span>
	                                                         </label>
                                                         </th>
                                                        <td>
                                                            <span id="updSName"></span>
                                                            <input type="hidden" name="u_updSName" id="u_updSName" value="" class="width100" >
                                                            <input type="hidden" name="u_updSCode" id="u_updSCode" value="" class="width100" >
                                                        </td>
                                                    </tr>
                                                     <tr>
                                                        <th>
	                                                        <label for="updRdata">
		                                                        <spring:message code="rdata.dataUsagePolicy" /> 
		                                                        <span class="key">*</span>
	                                                        </label>
                                                        </th>
                                                        <td>
                                                            <select name="updRdata" id="updRdata" onChange="updValList(this.value);" class="width40">
                                                                <option value="select"><spring:message code="select.select" /></option>
                                                            </select>
                                                        </td>
                                                        <th> <label for=""><spring:message code="list.remainingAmount" /></label></th>
                                                            <td> 기본 :<span id="updAmtRest"></span>&nbsp; &nbsp;
                                                                추가 잔여: <fmt:formatNumber value="${empty mapdata['add_remain'] ? 0 : mapdata['add_remain']}" groupingUsed="true" type="number"/> <br>
                                                                ※ <spring:message code="crewInfo.remainingAmount" />
                                                            </td>
                                                    </tr>
                                                    <tr>

                                                        <th><label for="updVal2"><spring:message code="list.dataCarryOver" /></label></th>
                                                        <td>
                                                            <span class="check-box1">
                                                                 <input type="checkbox" id="updVal2" name="updVal2" Value = "Y"/>
                                                                <label for="updVal2" ><spring:message code="crewInfo.use" /></label>
                                                            </span>
                                                        </td>
                                                        <th></th>
                                                        <td>

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
                                                        <th><spring:message code="list.dayOfUse" /> <span class="key">*</span></th>
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
                                                    	<span><spring:message code="button.delete" /></span>
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
                                                <input type="hidden" name="crew_name" id="crew_name">
                                                <input type="hidden" name="val2" id="val2">

                                 </form>
                             </div>
                        </div>
                    </div>

	<!-- 일괄적용 Modal -->
	<div class="modal fade" id="addAllModal" data-bs-backdrop="static">
    	<div class="modal-dialog modal-lg modal-dialog-centered" role="document">
        	<div class="modal-content">
            	<form name="AllCrewInfoForm" id="AllCrewInfoForm" method="post">
                	<section class="contents-area">
                    	<header>
                        	<i class="icon-write"><span>icon</span></i>
                        	<h2><spring:message code="button.applyingAllCrew" /></h2>
                        	<div class="cancelArea" data-bs-dismiss="modal">
								<img class="cancelX" src="../web/images/common/btn-layer-close.png">
							</div>
                    	</header>
                        <div class="board-area">
                        	<table class="tbl-write">
	                            <colgroup>
		                            <col style="width:20%;">
		                            <col style="">
		                            <col style="width:15%;">
		                            <col style="width:35%;">
	                            </colgroup>
	                            <tbody>
                                	<tr>
                                    	<th>
	                                        <label for="addCrewId">
	                                         <spring:message code="crewInfo.appliedItems" />
	                                         <span class="key">*</span>
	                                        </label>
                                        </th>
                                        <td colspan=3>
                                        	<select name="cs_select" id="cs_select" onChange="setList(this.value);" class="width25">
	                                            <option value="wifi">
	                                            	<spring:message code="list.blockUnlockWIFI" />
	                                            </option>
	                                            <option value="rate">
	                                            	<spring:message code="rdata.dataUsagePolicy" />
	                                            </option>
	                                            <option value="default">
	                                            	<spring:message code="crewInfo.loadPolicyDefaults" />
	                                            </option>
	                                            <option value="carry_over">
	                                            	<spring:message code="crewInfo.dataCarryoverFunction" />
	                                            </option>
                                            </select>
                                        </td>
									</tr>
                                    <tr>
                                    	<th>
	                                    	<label for="addAllComp">
		                                    	<spring:message code="select.shipOwner" />
		                                        <span class="key">*</span>
	                                        </label>
                                        </th>
	                                    <td>
		                                    <select name="addAllComp" id="addAllComp" onChange="updateShipNamesAll(this.value);" class="width80">
		                                        <option value="select"><spring:message code="select.select" /></option>
		                                        <c:forEach items="${compList}" var="compItem">
		                                            <option value="${compItem.comp_id}">${compItem.comp_name}</option>
		                                        </c:forEach>
		                                    </select>
	                                    </td>
	                                    <th>
	                                    	<label for="allSCode">
	                                        	<spring:message code="select.shipName" /> 
	                                            <span class="key">*</span>
	                                        </label>
	                                    </th>
	                                    <td>
	                                    	<div id="name_list">
	                                        	<select name="allSCode" id="allSCode" onChange="updateRdataNamesAll(this.value);" >
	                                            	<option value="select"><spring:message code="select.select" /></option>
	                                            </select>
	                                        </div>
	                                    </td>
                            		</tr>
                            		<tr>
                                     <th>
                                     	<label for="allCrewId">
	                                     	<spring:message code="list.crewID" />
	                                     	<span class="key">*</span>
                                     	</label>
                                     </th>
                                         <td colspan=3>
                                             <div class="multiple-checkbox">
                                                 <div class="selected">
                                                 	<span><spring:message code="list.crewID" /></span>
                                                 </div>
                                                 <div class="selectbox" id="wifi_crew_list" style="display: none;">
                                                     <ul id="crew_checkbox_list">
                                                         <!-- 체크박스 리스트가 여기에 동적으로 추가됨 -->
                                                     </ul>
                                                     <div class="btn-area">
                                                         <button type="button" class="btn-md burgundy" onclick="collectSelectedCrewIds();">
                                                         	<span><spring:message code="button.select" /></span>
                                                         </button>
                                                         <button type="button" class="btn-md gray" onclick="toggleCrewList();">
                                                         	<span><spring:message code="button.cancel" /></span>
                                                         </button>
                                                     </div>
                                                 </div>
                                             </div>
                                         </td>
                                 </tr>

                                  <!-- WIFI 차단/해제 -->
                                  <tr id="wifi_option">
                                      <th><label for="cs_data"><spring:message code="list.blockUnlockWIFI" /></label></th>
                                      <td colspan=3>
                                          <select name="cs_data" id="cs_data" >
                                              <option value="0"><spring:message code="crewInfo.block" /></option>
                                              <option value="1"><spring:message code="crewInfo.unBlock" /></option>
                                          </select>
                                      </td>

                                  </tr>

                               <!-- 데이터 사용 정책 옵션-->
                               <tr id="rate_option" style="display:none; ">
                                   <th>
                                   	<label for="allRdata">
                                   		<spring:message code="rdata.dataUsagePolicy" /> 
                                   		<span class="key">*</span>
                                   	</label>
                                   </th>
                                     <td colspan=3>
                                         <select name="allRdata" id="allRdata"  class="width15">
                                             <option value="select"><spring:message code="select.select" /></option>
                                         </select>
                                         &nbsp;
                                         <span class="check-box1" >
                                             <input type="checkbox" id="save_flag" name="save_flag" value="Y" checked/>
                                             <label for="save_flag" class="fs-11">
                                             	<spring:message code="crewInfo.defaultPolicy" />
                                             </label>
                                         </span>
                                     </td>
                               </tr>

                                 <tr id="val2_option" style="display:none;">
                                     <th>
	                                     <label for="allVal2">
	                                     	<spring:message code="crewInfo.dataCarryoverFunction" />
	                                    	<span class="key">*</span>
	                                     </label>
                                     </th>
                                     <td colspan=3>
                                         <select name="allVal2" id="allVal2">
                                             <option value="Y"><spring:message code="crewInfo.use" /></option>
                                             <option value="N"><spring:message code="crewInfo.unUse" /></option>
                                         </select>
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
                                 <button type="button"  id="btnUpdAll" class="btn-lg burgundy">
                                 	<span><spring:message code="button.save" /></span>
                                 </button>
                                 <button type="button" name="cancel" class="btn-lg gray btn-modal-close" 
                                 	data-bs-dismiss="modal" onclick="setList('wifi')" >
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
  
  					<!-- Batch applied items 메뉴 변경 시 동적으로 변경되는 테이블 기존 소스와 동일하게 변경하기 위해 수정중이였음 -->
                                    <%-- <tr>
                                    	<th>
                                        	<label for="allCrewId">
                                            	<spring:message code="list.crewID" /> 
                                           		<span class="key">*</span>
                                           	</label>
                                        </th>
                                        <td colspan=3>
	                                        <div class="multiple-checkbox">
	                                            <div class="selected">
	                                            	<span>
	                                            		<spring:message code="list.crewID" /> 
	                                            	</span>
	                                            </div>
	                                            <div class="selectbox" id="wifi_crew_list"> <!-- style="display:none;" -->
	                                                <ul id="crew_checkbox_list">	                                                
		                                                <!-- <li>
															<span class="check-box1">
																<input type="checkbox" checked>
																<label for="allSel">Select All</label>
															</span>
														</li> -->
	                                                    <!-- 체크박스 리스트가 여기에 동적으로 추가됨 -->
	                                                </ul>
	                                                <div class="btn-area">
														<button type="button" class="btn-md burgundy " onclick="collectSelectedCrewIds();">
															<span><spring:message code="button.select" /></span>
														</button>
														<button type="button" class="btn-md gray " onclick="toggleCrewList();">
															<span><spring:message code="button.cancel" /></span>
														</button>
													</div>                                    
	                                            </div>
	                                        </div>
	                                    </td>
    	                            </tr>                          
                            	</tbody>
							</table>
						</div>
						<div class="board-area" id="select_list">
							<table class="tbl-write">
								<colgroup>
									<col style="width:20%;">
									<col style="">
									<col style="width:15%;">
									<col style="width:35%;">
								</colgroup>
								<tbody>
									<tr>
										<th>
											<label for="cs_data">
                                            	<spring:message code="list.blockUnlockWIFI" />
                                            	<span class="key">*</span>
                                            </label>
										</th>
										<td colspan=3>
                                            <select name="cs_data" id="cs_data">
                                                <option value="0"><spring:message code="crewInfo.block" /></option>
                                                <option value="1"><spring:message code="crewInfo.unBlock" /></option>
                                            </select>
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
                                    <button type="button"  id="btnUpdAll" class="btn-lg burgundy">
                                    	<span><spring:message code="button.save" /></span>
                                    </button>
                                    <button type="button" name="cancel" class="btn-lg gray btn-modal-close" 
                                    	data-bs-dismiss="modal" onclick="setList('wifi')" >
                                    	<span><spring:message code="button.cancel" /></span>
                                    </button>
                                </div>
                            </div>
                        </div>
					</section>
				</form>
			</div>
		</div>
	</div> --%>

            <!-- 엑셀 업로드 modal -->
            <div class="modal fade" id="importModal" data-bs-backdrop="static">
                <div class="modal-dialog modal-lg modal-dialog-centered" role="document">
                 <div class="modal-content">
                  <section class="contents-area excelContents-area">

                      <header>
                            <i class="icon-write"><span>icon</span></i>
                            <h2><spring:message code="crewInfo.crewInformationImport" /></h2>
                        	<div class="cancelArea" data-bs-dismiss="modal">
								<img class="cancelX" src="../web/images/common/btn-layer-close.png">
							</div>
                    </header>

                    <form name="excelUploadFrom" id="excelUploadFrom" method="post">

                                <div class="board-area">
                                    <table class="tbl-write">
                                        <colgroup>
                                            <col style="width:20%;">
                                            <col style="">
                                        </colgroup>
                                        <tbody>
                                          <tr>
                                                <th>
	                                                <label for="add_file">
		                                                <spring:message code="crewInfo.excelFilePath" /> 
		                                                <span class="key">*</span>
	                                                </label>
                                                </th>
                                                <td><input type="file" id="add_file" name="add_file"
                                                    accept=".csv, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.ms-excel"
                                                    class="width100">
                                                </td>
                                            </tr>
                                            <tr>
                                                <th><spring:message code="list.option" /> <span class="key">*</span></th>
                                                <td>
                                                    <span class="check-box1">
                                                        <input type="checkbox" id="add_field" name="add_field" checked>
                                                        <label for="add_field">
                                                        	<spring:message code="crewInfo.useTheFieldTitle" />
                                                        </label>
                                                    </span>
                                                    <span class="check-box1"> <!-- style="display: none;"> -->
                                                        <input type="checkbox" id="add_predel" name="add_predel">
                                                        <label for="add_predel">
                                                        	<spring:message code="crewInfo.clearData" />
                                                        </label>
                                                    </span>

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
                                            <button type="button"  id="btnUpdExcel" class="btn-lg burgundy" onClick="req_import();">
                                            	<span>Import</span>
                                            </button>
                                            <button type="button" name="cancel" class="btn-lg gray btn-modal-close" data-bs-dismiss="modal">
                                            	<span>닫 기</span>
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