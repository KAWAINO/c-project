<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>

<head>

<!-- meta tag --> 
<%@ include file = "/decorators/meta.jsp" %>
<!-- //meta tag -->

<meta charset="UTF-8">

<title>MVSAT APMS</title>
<script type="text/javascript" >

	let downloadExcelMessage = '<spring:message code="confirm.downloadExcel" />';
	let downloadCanceledMessage = '<spring:message code="confirm.downloadCanceled" />';
	let select = '<spring:message code="select.select" />';
	let success = '<spring:message code="confirm.success" />';
	let del = '<spring:message code="confirm.delete" />';
	let error = '<spring:message code="confirm.error" />';

$(document).ready(function(){
	
	// 모달
	$('#addModal').on('hidden.bs.modal', function (e) {
		$('#addEnableTimeForm')[0].reset();
	});

	// 검색
    $("#btnSearch").bind("click", function() {
    	$("#pageno").val(1);
    	$("#searchEnableTimeForm").attr("action", "/enabletime/enabletime.do");
        $("#searchEnableTimeForm").submit();
    });

 	// 추가
    $("#btnAdd").bind("click", function() {
    	addEnableTime();
    });
 	
 	// 수정
    $("#btnUpd").bind("click", function() {
    	updateEnableTime();
    });
 	
 	// 삭제
    $("#btnDel").bind("click", function() {
    	delEnableTime();
    });

 	// 추가모달 선주사에 따른 선박명 set
    $('#addCompId').on('change', function() {
        updateShipList('#addCompId', '#addShipName');
    });
});

function updateShipList(compIdSelector, shipNameSelector) {
    let compId = $(compIdSelector + ' option:selected').val();

    if(compId != "") {
        $.ajax({
            type: "POST",
            url: "/apinfo/shipList.ajax",
            data: { "searchCompId": compId },
            dataType: "json",
            error: function(e) {
                alert(error);
            },
            success: function(data) {
                var result = "<option value='select'>" + select + "</option>";

                for(i = 0; i < data.length; i++) {
                    result += "<option value='"+data[i].s_code+"'>"+data[i].s_name+"</option>";
                }
                $(shipNameSelector).html(result);
            }
        });
    } else {
        $(shipNameSelector).html("<option value='select'>" + select + "</option>");
    }
}

function addEnableTime(){	
	
	if($('#addShipName').val() == "select"){
		alert('<spring:message code="confirm.select.ship" />');
		$('#addShipName').focus();
		return;
	}
	
	$('#addFromTime').val($('#fromHour').val() + $('#fromMin').val());
	$('#addToTime').val($('#toHour').val() + $('#toMin').val());
	
	//console.log($('#fromHour').val());
	//console.log($('#fromMin').val());
	//console.log($('#toHour').val());
	//console.log($('#toMin').val());

	if($('#fromHour').val() > $('#toHour').val() || ($('#fromHour').val() == $('#toHour').val()
			&& $('#fromMin').val() > $('#toMin').val())) {
		alert('<spring:message code="confirm.wrong.laterThanAllowableTime" />');
		$('#addFromTime').focus();
		return
	} 

	
	var datas = $("#addEnableTimeForm").serialize();
	$.ajax({
		type : "POST",
		url : "/enabletime/enableTimeAdd.ajax",
		data : "ajax=true&" + datas,
		dataType : "json",
		error	: function (e) { alert(error);},		
		success : function(data) {		
			if(data.result == "1") {
				alert(success);
				document.location.reload();
			} else if(data.result == "-2") {
				alert('<spring:message code="confirm.wrong.allowedTimeOverlaps" />');
			}
		}
	});
	return;
}

function goUpdate(from_time, s_code){

	$.ajax({
		type : "POST",
		url : "/enabletime/enableTimeUpdateData.ajax",
		data : {
				'from_time' : from_time,
				's_code' : s_code
			   },
		dataType : "json",
		error	: function (e) { alert(error); },
		success : function(data) {
	
			$('#updCompId').val(data.enableTimeInfo.comp_name);
			$('#updShipName').val(data.enableTimeInfo.s_name);
			$('#updFromHour').val(data.enableTimeInfo.fromHour);
			$('#updFromMin').val(data.enableTimeInfo.fromMin);
			$('#updToHour').val(data.enableTimeInfo.toHour);
			$('#updToMin').val(data.enableTimeInfo.toMin);
			$('#updDescr').val(data.enableTimeInfo.descr);
			$('#updScode').val(data.enableTimeInfo.s_code);
			
			$("#updateModal").modal("show");
		}
	});
}

function delEnableTime(){
	let from_time = $('#updFromHour').val() + $('#updFromMin').val();
	let to_time = $('#updToHour').val() + $('#updToMin').val();
	let s_code = $('#updScode').val();
	//console.log($('#updScode').val());
	
	if(!confirm($('#updFromHour').val() + ":" + $('#updFromMin').val()
			+ " ~ " + $('#updToHour').val() + ":" + $('#updToMin').val() + " " + '<spring:message code="confirm.delete.enableTime" />')){
		return;
	}
	
	$.ajax({
		type : "POST",
		url : "/enabletime/enableTimeDelete.ajax",
		data : {
					'from_time' : from_time,
					'to_time' : to_time,
					's_code' : s_code
			   },
		dataType : "json",
		error	: function (e) { alert(error);},
		success : function(data) {
			if(data.result == "1") {
				alert("정상적으로 삭제되었습니다.");
				document.location.reload();
			}else {
				alert(error);
			}
		}
	});
}

</script>
</head>
<body>

	<jsp:include page="../header.jsp" flush="false"/>
	
	<div class="contents-area">
		<header>
			<i class="icon-list"><span>icon</span></i>
			<h2><spring:message code="enabletime.usageTimeManagement" /></h2>
		</header>
		<form name="searchEnableTimeForm" id="searchEnableTimeForm" method="post">
			<input type="hidden" id="pageno" name="pageno" value="${enableTimeVo.pageno }"/>
			<input type="hidden" id="excelMsg" name="excelMsg" value="">
			<div class="search-area">
				<select id="searchCompId" name="searchCompId">
					<option value=""><spring:message code="select.shipOwner" /></option>
					<c:forEach var="comp" items="${compList }">
						<option value="${comp.comp_id}" <c:if test="${comp.comp_id eq enableTimeVo.searchCompId }">selected</c:if>>
							<c:out value="${comp.comp_name}" />
						</option>
					</c:forEach>
				</select>
				<input type="text" id="searchShipName" name="searchShipName" class="w150" maxlength="" 
					value="${enableTimeVo.searchShipName }" placeholder="<spring:message code="search.enterShipName" />">
				<button class="btn-md white" type="button" id="btnSearch">
					<span><spring:message code="button.search" /></span>
				</button> &nbsp;&nbsp;<spring:message code="enabletime.information" />
			</div>
			<div class="board-area">
				<table class="tbl-default bg-on">
					<colgroup>
						<col style="width:15%;">
						<col style="width:15%;">
						<col style="width:15%;">
						<col style="width:15%;">
						<col style="">
					</colgroup>
					<thead>
						<tr>
							<th scope="col"><spring:message code="select.shipOwner" /></th>
							<th scope="col"><spring:message code="select.shipName" /></th>
							<th scope="col"><spring:message code="list.allowableStartTime" /></th>
							<th scope="col"><spring:message code="list.allowableEndTime" /></th>
							<th scope="col"><spring:message code="list.description" /></th>
						</tr>
					</thead>
					<tbody>
						<c:choose>
							<c:when test="${fn:length(enableTimeList) > 0 }">
								<c:forEach var="item" items="${enableTimeList }" varStatus="idx" >
									<tr id="list_${item.from_time }" onClick="goUpdate('${item.from_time}', '${item.s_code}')" >
										<td>${item.comp_name }</td>
										<td>${item.s_name }</td>
										<td>${item.from_time }</td>
										<td>${item.to_time }</td>
										<td>${item.descr }</td>
									</tr>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<tr>
							    	<td colspan="5">
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
				    	<button type="button" class="btn-lg blue" data-bs-toggle="modal" data-bs-target="#addModal">
				    		<span><spring:message code="button.add" /></span>
				    	</button>
				    </div> 
				</div>			
			</div>
		</form>
	</div>
	<jsp:include page="../footer.jsp" flush="false"/>

</body>

	<!-- 추가 모달 -->
	<div class="modal fade" id="addModal" data-bs-backdrop="static"> 
    	<div class="modal-dialog modal-lg modal-dialog-centered" role="document"> 
        	<div class="modal-content"> 
				<section class="contents-area"> 
					<header>
						<i class="icon-write"><span>icon</span></i>
						<h2><spring:message code="enabletime.usageTimeManagement" /></h2>
						<div class="cancelArea" data-bs-dismiss="modal">
							<img class="cancelX" src="../web/images/common/btn-layer-close.png">
						</div>
					</header>
					<form name="addEnableTimeForm" id="addEnableTimeForm" method="post">
					<input type="hidden" id="addFromTime" name="addFromTime"/>
					<input type="hidden" id="addToTime" name="addToTime"/>
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
											<label for="addCompId">
												<spring:message code="select.shipOwner" /><span class="key">*</span>
											</label>
										</th>
										<td>
											<select id="addCompId" name="addCompId">
												<option value="select"><spring:message code="select.select" /></option>
												<c:forEach var="ship" items="${compList }">
													<option value="${ship.comp_id}" <%-- <c:if test="${ship.comp_id eq enableTimeVo.searchCompId }">selected</c:if> --%>>
														<c:out value="${ship.comp_name}" />
													</option>
												</c:forEach>
											</select>
										</td>
										<th>
											<label for="addShipName">
											<spring:message code="select.shipName" /><span class="key">*</span>
											</label>
										</th>
										<td>
											<select id="addShipName" name="addShipName">
												<option value="select"><spring:message code="select.select" /></option>
											</select>
										</td>
									</tr>
									<tr>
										<th>
											<label for="addFromTime">
												<spring:message code="list.allowableStartTime" /><span class="key">*</span>
											</label>
										</th>
										<td>
											<select class="form-select" id="fromHour" name="fromHour" >					
												<c:forEach begin="0" end="23" var="hour">
							                    	<c:set var="setHour" value="${hour}" />
							                        	<c:if test="${hour < 10}">
							                            	<c:set var="setHour" value='0${hour}' />
							                            </c:if>
							                            <option value="${setHour}" ${setHour eq enableTimeVo.fromHour ? 'selected' : ''}>
							                            	<c:out value="${setHour}" />
							                            </option>
							                   	</c:forEach>
											</select>
											<label><spring:message code="time.hour2" />&nbsp;</label>
											<select class="form-select" id="fromMin" name="fromMin">					
												<c:forEach begin="0" end="59" var="min">
							                    	<c:set var="setMin" value="${min}" />
							                        	<c:if test="${min < 10}">
							                            	<c:set var="setMin" value='0${min}' />
							                            </c:if>
							                           	<option value="${setMin}" ${setMin eq enableTimeVo.fromMin ? 'selected' : ''}>
							                               	<c:out value="${setMin}" />
							                            </option>
												</c:forEach>
											</select>
											<label><spring:message code="time.min2" /></label>
										</td>
										<th>
											<label for="addFromTime">
												<spring:message code="list.allowableEndTime" /><span class="key">*</span>
											</label>
										</th>
										<td>
											<select class="form-select" id="toHour" name="toHour" >					
												<c:forEach begin="0" end="23" var="hour">
							                    	<c:set var="setHour" value="${hour}" />
							                        	<c:if test="${hour < 10}">
							                            	<c:set var="setHour" value='0${hour}' />
							                            </c:if>
							                            <option value="${setHour}" ${setHour eq enableTimeVo.toHour ? 'selected' : ''}>
							                            	<c:out value="${setHour}" />
							                            </option>
							                   	</c:forEach>
											</select>
											<label><spring:message code="time.hour2" />&nbsp;</label>
											<select class="form-select" id="toMin" name="toMin">					
												<c:forEach begin="0" end="59" var="min">
							                    	<c:set var="setMin" value="${min}" />
							                        	<c:if test="${min < 10}">
							                            	<c:set var="setMin" value='0${min}' />
							                            </c:if>
							                           	<option value="${setMin}" ${setMin eq enableTimeVo.toMin ? 'selected' : ''}>
							                               	<c:out value="${setMin}" />
							                            </option>
												</c:forEach>
											</select>
											<label><spring:message code="time.min2" /></label>
										</td>
									</tr>
									<tr>
										<th><label for="addDescr"><spring:message code="list.description" /></label></th>
										<td colspan="3">
											<div class="textarea">
												<textarea name="addDescr" id="addDescr"></textarea>
											</div>
										</td>
									</tr>
								</tbody>
							</table>
						</div>
					</form>
					<div class="board-btm-area">
						<div class="btn-area">
							<div class="fright">
								<button class="btn-lg burgundy btn-modal" id=btnAdd>
									<span><spring:message code="button.save" /></span>
								</button>
								<button class="btn-lg gray btn-modal-close" type="button" data-bs-dismiss="modal">
									<span><spring:message code="button.cancel" /></span>
								</button>
							</div>
						</div>
					</div>	
				</section>
			</div>
		</div>
	</div>
	
	<!-- 수정 모달 -->
	<div class="modal fade" id="updateModal" data-bs-backdrop="static"> 
    	<div class="modal-dialog modal-lg modal-dialog-centered" role="document"> 
        	<div class="modal-content"> 
				<section class="contents-area"> 
					<header>
						<i class="icon-write"><span>icon</span></i>
						<h2><spring:message code="enabletime.usageTimeManagement" /></h2>
						<div class="cancelArea" data-bs-dismiss="modal">
							<img class="cancelX" src="../web/images/common/btn-layer-close.png">
						</div>
					</header>
					<form name="updEnableTimeForm" id="updEnableTimeForm" method="post">
						<input type="hidden" id="updScode" />
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
											<label for="updCompId">
												<spring:message code="select.shipOwner" /><span class="key">*</span>
											</label>
										</th>
										<td>
											<input id="updCompId" name="updCompId" disabled>
										</td>
										<th>
											<label for="updShipName">
												<spring:message code="select.shipName" /><span class="key">*</span>
											</label>
										</th>
										<td>
											<input id="updShipName" name="updShipName" disabled>
										</td>
									</tr>
									<tr>
										<th>
											<label for="FromTime">
												<spring:message code="list.allowableStartTime" /><span class="key">*</span>
											</label>
										</th>
										<td>
											<select class="form-select" id="updFromHour" name="updFromHour" disabled>					
												<c:forEach begin="0" end="23" var="hour">
							                    	<c:set var="setHour" value="${hour}" />
							                        	<c:if test="${hour < 10}">
							                            	<c:set var="setHour" value='0${hour}' />
							                            </c:if>
							                            <option value="${setHour}" ${setHour eq enableTimeVo.fromHour ? 'selected' : ''}>
							                            	<c:out value="${setHour}" />
							                            </option>
							                   	</c:forEach>
											</select>
											<label><spring:message code="time.hour2" />&nbsp;</label>
											<select class="form-select" id="updFromMin" name="updFromMin" disabled>					
												<c:forEach begin="0" end="59" var="min">
							                    	<c:set var="setMin" value="${min}" />
							                        	<c:if test="${min < 10}">
							                            	<c:set var="setMin" value='0${min}' />
							                            </c:if>
							                           	<option value="${setMin}" ${setMin eq enableTimeVo.fromMin ? 'selected' : ''}>
							                               	<c:out value="${setMin}" />
							                            </option>
												</c:forEach>
											</select>
											<label><spring:message code="time.min2" /></label>
										</td>
										<th>
											<label for="addFromTime">
												<spring:message code="list.allowableEndTime" /><span class="key">*</span>
											</label>
										</th>
										<td>
											<select class="form-select" id="updToHour" name="updToHour" disabled>					
												<c:forEach begin="0" end="23" var="hour">
							                    	<c:set var="setHour" value="${hour}" />
							                        	<c:if test="${hour < 10}">
							                            	<c:set var="setHour" value='0${hour}' />
							                            </c:if>
							                            <option value="${setHour}" ${setHour eq enableTimeVo.toHour ? 'selected' : ''}>
							                            	<c:out value="${setHour}" />
							                            </option>
							                   	</c:forEach>
											</select>
											<label><spring:message code="time.hour2" />&nbsp;</label>
											<select class="form-select" id="updToMin" name="updToMin" disabled>					
												<c:forEach begin="0" end="59" var="min">
							                    	<c:set var="setMin" value="${min}" />
							                        	<c:if test="${min < 10}">
							                            	<c:set var="setMin" value='0${min}' />
							                            </c:if>
							                           	<option value="${setMin}" ${setMin eq enableTimeVo.toMin ? 'selected' : ''}>
							                               	<c:out value="${setMin}" />
							                            </option>
												</c:forEach>
											</select>
											<label><spring:message code="time.min2" /></label>
										</td>
									</tr>
									<tr>
										<th><label for="updDescr"><spring:message code="list.description" /></label></th>
										<td colspan="3">
											<div class="textarea">
												<textarea name="updDescr" id="updDescr"></textarea>
											</div>
										</td>
									</tr>
								</tbody>
							</table>
						</div>
					</form>
					<div class="board-btm-area">
						<div class="btn-area">
							<div class="fleft" id="fleft">
								<button type="button" class="btn-lg yellow" id="btnDel">
									<span><spring:message code="button.delete" /></span>
								</button>
							</div>
							<div class="fright">
								<button class="btn-lg gray btn-modal" type="button" name="cancel" data-bs-dismiss="modal">
									<span><spring:message code="button.cancel" /></span>
								</button>
							</div>
						</div>
					</div>
				</section>
			</div>
		</div>
	</div>
</html>