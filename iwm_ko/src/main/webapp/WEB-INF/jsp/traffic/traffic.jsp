<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE HTML>

<html>

<head>
<!-- meta tag -->
<%@ include file="/decorators/meta.jsp" %>

<title>MVSAT APMS</title>
<script type="text/javascript" src="/web/js/traffic/traffic.js"></script>
<script type="text/javascript">

	let downloadExcelMessage = '<spring:message code="confirm.downloadExcel" />';
	let downloadCanceledMessage = '<spring:message code="confirm.downloadCanceled" />';
	let select = '<spring:message code="select.select" />';
	let success = '<spring:message code="confirm.success" />';
	let del = '<spring:message code="confirm.delete" />';
	let error = '<spring:message code="confirm.error" />';
	let every = '<spring:message code="traffic.every" />';
	let hours = '<spring:message code="traffic.hours" />';
	let days = '<spring:message code="traffic.days" />';
	let hour = '<spring:message code="time.hour" />';
	let minute = '<spring:message code="time.minute" />';
	let selectShipOwner = '<spring:message code="confirm.select.shipOwner" />';
	let selectShip = '<spring:message code="confirm.select.ship" />';
	let enterScheduleName = '<spring:message code="confirm.enter.scheduleName" />';
	let existsScheduleName = '<spring:message code="confirm.exists.scheduleName" />';
	let deleteSchedule = '<spring:message code="confirm.delete.schedule" />';

</script>

</head>
<body>
<jsp:include page="../header.jsp" flush="false"/>
<section class="contents-area">
<header>
	<i class="icon-list"><span>icon</span></i>
	<h2><spring:message code="traffic.shipTraffic" /></h2>
</header>
<form name="searchTrafficForm" id="searchTrafficForm" method="post">

            <div id ="div_contents" class="board-area" style="display: block;">
                    <table class="tbl-default bg-on">
                        <colgroup>
                            <col style="">
                            <col style="width:15%;">
                            <col style="width:15%;">
                            <col style="width:13%;">
                            <col style="width:13%;">
                            <col style="width:13%;">
                            <col style="width:13%;">
                        </colgroup>
                        <thead>
                            <tr>
                                <th scope="col"><spring:message code="select.shipOwner" /></th>
                                <th scope="col"><spring:message code="select.shipName" /></th>
                                <th scope="col"><spring:message code="list.scheduleName" /></th>
                                <th scope="col"><spring:message code="list.unit" /></th>
                                <th scope="col"><spring:message code="list.period" /></th>
                                <th scope="col"><spring:message code="time.hour" /></th>
                                <th scope="col"><spring:message code="time.minute" /></th>
                            </tr>
                        </thead>
                               <tbody id="trafficTableBody">
                                 <c:choose>
                                     <c:when test="${fn:length(trafficList) > 0 }">
                                         <c:forEach var="item" items="${trafficList }" varStatus="idx">
                                             <tr id="list_${item.seq_traf_sch }" onClick="goUpdate('${item.seq_traf_sch}')">
                                                 <td>${item.comp_name }</td>
                                                 <td>${item.s_name }</td>
                                                 <td>${item.sch_name }</td>
                                                 <td>
                                                     <c:choose>
                                                         <c:when test="${item.sch_unit == '1'}"><spring:message code="select.hourly" /></c:when>
                                                         <c:otherwise><spring:message code="select.daily" /></c:otherwise>
                                                     </c:choose>
                                                 </td>
                                                 <td>
                                                     <c:choose>
                                                         <c:when test="${item.sch_unit == '1'}">
                                                             ${item.sch_interval}<spring:message code="select.hourly" />
                                                         </c:when>
                                                         <c:otherwise>
                                                             ${item.sch_interval}<spring:message code="select.daily" />
                                                         </c:otherwise>
                                                     </c:choose>
                                                 </td>
                                                 <td>
                                                     <c:choose>
                                                         <c:when test="${item.sch_unit == '1'}">-</c:when>
                                                         <c:otherwise>${item.sch_hour}</c:otherwise>
                                                     </c:choose>
                                                 </td>
                                                 <td>${item.sch_min}</td>
                                             </tr>
                                         </c:forEach>
                                     </c:when>
                                     <c:otherwise>
                                         <tr>
                                             <td colspan="7">
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

                        <div class="btn-area">
                            <div class="fleft">
                                 <c:choose>
                                     <c:when test="${sessionCompId == '0'}">
                                         <button type="button" class="btn-lg blue" data-bs-toggle="modal" data-bs-target="#addModal">
                                         	<span><spring:message code="button.add" /></span>
                                         </button>
                                     </c:when>
                                 </c:choose>
                            </div>
                            <div class="fright">
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
                <form name="addTrafficForm" id="addTrafficForm" method="post">
                     <section class="contents-area">
                            <header>
                                <i class="icon-write"><span>icon</span></i>
                                <h2><spring:message code="traffic.shipTraffic" /></h2>
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
                                            <th>
                                            	<label for="addComp">
                                            		<spring:message code="select.shipOwner" /> 
                                            		<span class="key">*</span>
                                            	</label>
                                            </th>
                                            <td>
                                                <select name="addComp" id="addComp" onChange="updateShipNames(this.value);" class="width50">
                                                    <option value="select"><spring:message code="select.select" /></option>
                                                    <c:forEach items="${compList}" var="compItem">
                                                        <option value="${compItem.comp_id}">${compItem.comp_name}</option>
                                                    </c:forEach>
                                                </select>
                                            </td>
                                            <th>
	                                            <label for="addSCode">
	                                            	<spring:message code="select.shipName" /> 
	                                            	<span class="key">*</span>
	                                            </label>
                                            </th>
                                            <td>
                                                <div id="name_list">
                                                    <select name="addSCode" id="addSCode" class="width32">
                                                        <option value="select"><spring:message code="select.select" /></option>
                                                    </select>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <th>
                                            	<label for="add_sch">
                                            		<spring:message code="list.scheduleName" /> 
                                            		<span class="key">*</span>
                                            	</label>
                                            </th>
                                            <td><input type="text" id="add_sch" name="add_sch" value="" maxlength="" class="width100"></td>
                                            <th>
                                            	<label for="add_unit">
                                            		<spring:message code="list.unit" /> 
                                            		<span class="key">*</span>
                                            	</label>
                                            </th>
                                            <td>
                                                <select name="add_unit" size="1" id="add_unit" onChange="updateOptions();">
                                                    <option value="1"><spring:message code="select.hourly" /></option>
                                                    <option value="2"><spring:message code="select.daily" /></option>
                                                </select>
                                            </td>
                                        </tr>
                                        <tr>
                                            <th>
                                            	<label for="add_interval">
                                            		<spring:message code="list.period" /> 
                                            		<span class="key">*</span> 
                                            	</label>
                                            </th>
                                            <td>
                                                <div id="interval">
                                                    <span id="intervalLabel"><spring:message code="traffic.every" />
                                                    <select name="add_interval" id="add_interval">
                                                        <c:forEach var="i" begin="1" end="12">
                                                            <option value="${i}">${i}</option>
                                                        </c:forEach>
                                                    </select>
                                                    <spring:message code="traffic.hours" /> 
                                                    </span>
                                                </div>
                                            </td>


                                            <th>
                                            <label for="add_min">
                                            	<spring:message code="traffic.timeToRun" />
                                            	<span class="key">*</span> 
                                            </label>
                                            </th>
                                            <td>
                                                <div id="time">
                                                    <select name="add_min" id="add_min">
                                                        <c:forEach var="i" begin="0" end="59">
                                                            <option value="${i < 10 ? '0' : ''}${i}">${i < 10 ? '0' : ''}${i}</option>
                                                        </c:forEach>
                                                    </select>
                                                    <spring:message code="time.minute" />
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
                <form name="updTrafficForm" id="updTrafficForm" method="post">
                     <input type="hidden" id="seq_traf_sch" name="seq_traf_sch" value="">

                     <section class="contents-area">
                            <header>
                                <i class="icon-write"><span>icon</span></i>
                                <h2><spring:message code="traffic.shipTraffic" /></h2>
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
                                            <th>
                                            	<label for="updComp">
                                            		<spring:message code="select.shipOwner" />
                                            		<span class="key">*</span>
                                            	</label>
                                            </th>
                                            <td>
                                                <span id="u_updComp"> </span>
                                                <input type="hidden" id="updComp" name="updComp" class="width100" readonly>
                                            </td>
                                            <th>
                                            	<label for="updSCode">
                                            		<spring:message code="select.shipName" /> 
                                            		<span class="key">*</span>
                                            	</label>
                                            </th>
                                            <td>
                                            <span id="u_updSCode"> </span>
                                                <input type="hidden" id="updSCode" name="updSCode" class="width100" readonly>
                                            </td>
                                        </tr>
                                        <tr>
                                            <th>
                                            	<label for="upd_sch">
                                            		<spring:message code="list.scheduleName" /> 
                                            		<span class="key">*</span>
                                            	</label>
                                            </th>
                                            <td>
                                                <span id="u_upd_sch"></span>
                                                <input type="hidden" id="upd_sch" name="upd_sch" value="" class="width100" readonly>
                                            </td>
                                            <th>
                                            	<label for="upd_unit">
                                            		<spring:message code="list.unit" /> 
                                            		<span class="key">*</span> 
                                            	</label>
                                            </th>
                                            <td>
                                                <select name="upd_unit" size="1" id="upd_unit" onChange="updateOptionsUpd();">
                                                    <option value="1"><spring:message code="select.hourly" /></option>
                                                    <option value="2"><spring:message code="select.daily" /></option>
                                                </select>
                                            </td>
                                        </tr>
                                        <tr>
                                            <th>
	                                            <label for="upd_interval">
	                                           		<spring:message code="list.period" /> 
	                                           		<span class="key">*</span> 
	                                            </label>
                                            </th>
                                            <td>
                                                <div id="interval">
                                                    <span id="intervalLabelUpd"><spring:message code="traffic.every" />
                                                    <select name="upd_interval" id="upd_interval">
                                                        <c:forEach var="i" begin="1" end="12">
                                                            <option value="${i}">${i}</option>
                                                        </c:forEach>
                                                    </select>
                                                    <spring:message code="traffic.hours" />
                                                    </span>
                                                </div>
                                            </td>


                                            <th>
                                            	<label for="upd_min">
                                            		<spring:message code="traffic.timeToRun" /> 
                                            		<span class="key">*</span> 
                                            	</label>
                                            </th>
                                            <td>
                                                <div id="timeUpd">
                                                    <select name="upd_min" id="upd_min">
                                                        <c:forEach var="i" begin="0" end="59">
                                                            <option value="${i < 10 ? '0' : ''}${i}">${i < 10 ? '0' : ''}${i}</option>
                                                        </c:forEach>
                                                    </select>
                                                    <spring:message code="time.minute" />
                                                </div>
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

</html>