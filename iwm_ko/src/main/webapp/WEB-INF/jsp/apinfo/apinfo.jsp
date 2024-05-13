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
		$('#addApInfoForm')[0].reset();
	});

	// 검색
    $("#btnSearch").bind("click", function() {
    	$("#pageno").val(1);
    	$("#searchApInfoForm").attr("action", "/apinfo/apinfo.do");
        $("#searchApInfoForm").submit();
    });
	
 	// 추가
    $("#btnAdd").bind("click", function() {
    	addApInfo();
    });
 	
 	// 수정
    $("#btnUpd").bind("click", function() {
    	updateApInfo();
    });
 	
 	// 삭제
    $("#btnDel").bind("click", function() {
    	delApInfo();
    });
 	
 	// 리부팅
    $("#btn_reboot").bind("click", function() {
    	reboot();
    });
	
	// excel
    $("#btnExcel").bind("click", function() {
    	goExcelDown();
    });
	
 	// 추가모달 선주사에 따른 선박명 set
	$('#addCompId').on('change', function() {
		updateShipList('#addCompId', '#addShipName');
	});
 	
	$('#updConnTimeChange').click(function() {
		let isChecked = $(this).is(":checked");		
	    $('#updHour').prop('disabled', !isChecked);
	    $('#updMin').prop('disabled', !isChecked);
	    $('#updUnLimit').prop('disabled', !isChecked);
	});	
	
});

//paging
function goPage(pageno) {
	$("#pageno").val(pageno);
	$("#searchApInfoForm").attr("action", "/apinfo/apinfo.do");
    $("#searchApInfoForm").submit();	
}

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
                let result = "<option value=''>" + select + "</option>";

                for(i = 0; i < data.length; i++) {
                    result += "<option value='"+data[i].s_code+"'>"+data[i].s_name+"</option>";
                }
                $(shipNameSelector).html(result);
            }
        });
    } else {
        $(shipNameSelector).html("<option value=''>" + select + "</option>");
    }
}


function addApInfo(){	
	
	//MAC 주소
    if ('' == $("#addMac").val()) {
        alert('<spring:message code="confirm.enter.macAddress" />');
        $("#addMac").focus();
        return;
    }
	
  	//MAC 주소
    if (12 != $("#addMac").val().length) {
        alert('<spring:message code="confirm.wrong.macAddressLength" />');
        $("#addMac").focus();
        return;
    }
	
	//AP NAME
    if ('' == $("#addApName").val()) {
        alert('<spring:message code="confirm.enter.apManagementName" />');
        $("#addApName").focus();
        return;
    }
	
    //선박
	if($("#addShipName").val() == ""){
		alert('<spring:message code="confirm.select.ship" />');
        $("#addShipName").focus();
		return;
	}
    
	//시리얼번호
    if ('' == $("#addSerial").val()) {
        alert('<spring:message code="confirm.enter.serialNumber" />');
        $("#addSerial").focus();
        return;
    }
	
    //내부아이피대역
    if ('' == $("#addIpNumber").val()) {
        alert('<spring:message code="confirm.enter.internalIPband" />');
        $("#addIpNumber").focus();
        return;
    }
    
  	//동시접속자
    if ('' == $("#addConUser").val()) {
        alert('<spring:message code="confirm.enter.concurrentUsers" />');
        $("#addConUser").focus();
        return;
    }
	
  	//동시접속자
    if (!$.isNumeric($("#addConUser").val())) {
        alert('<spring:message code="confirm.wrong.numberConcurrentUsers" />');
        $("#addConUser").focus();
        return;
    }

    var datas = $("#addApInfoForm").serialize();
	$.ajax({
		type : "POST",
		url : "/apinfo/apinfoAdd.ajax",
		data : "ajax=true&" + datas,
		dataType : "json",
		error	: function (e) { alert(error);},		
		success : function(data) {		
			if(data.result == "1") {
				alert(success);
				document.location.reload();
			} else if(data.result == "-2") {
				alert('<spring:message code="confirm.exists.mac" />');
			} else if(data.result == "-3"){
				alert('<spring:message code="confirm.exists.apName" />')
			} else { 
				alert(error);
			}
		}
	});
	return;
}

function goUpdate(mac, s_code){
	
	$.ajax({
		type : "POST",
		url : "/apinfo/apinfoUpdateData.ajax",
		data : {
					'mac' : mac,
					's_code' : s_code,
			   },
		dataType : "json",
		error	: function (e) { alert(error); },
		success : function(data) {
			$('#updMac').val(data.apInfoVo.mac);
			$('#u_updMac').val(data.apInfoVo.mac);
			$('#updApName').val(data.apInfoVo.ap_name);
			$('#u_updApName').val(data.apInfoVo.ap_name);
			$('#updCompId').val(data.apInfoVo.comp_name);
			$('#updShipName').val(data.apInfoVo.s_name);
			$('#updSerial').val(data.apInfoVo.serial_no);
			$('#updIpNumber').val(data.apInfoVo.ip_no);
			$('#updBand').val(data.apInfoVo.band_width);
			$('#updConUser').val(data.apInfoVo.con_user);
			$('#updWIFI').val(data.apInfoVo.wifi_conn_flag);
			$('#updDescr').val(data.apInfoVo.descr);
			$('#updScode').val(data.apInfoVo.s_code);
			
			$('#updHour').val(data.apInfoVo.updHour).prop('selected', true);
			
			/* conn_time hour 설정 */
			let updHour = $('#updHour');
			for (let i=0; i<24; i++) {  
				if(i < 10){
					updHour += ("<option value='"+i+"'>"+'0'+i+"</option>");
				} else {
					updHour += ("<option value='"+i+"'>"+i+"</option>");
				}
			}
			$('#updHour').html(updHour);
			$('#updHour2').val(data.apInfoVo.updHour); 
			//console.log($('#updHour2').val());

			let hour = document.getElementById('updHour');
			let len1 = hour.options.length;
			let str1 = document.getElementById('updHour2');
			
			for (let i=0; i<len1; i++){  
				if(hour.options[i].value == str1.value){
					hour.options[i].selected = true;
				}
			} 
			/* conn_time hour 설정 끝*/

			/* conn_time min 설정 */
			let updMin = $('#updMin');
			for (let i=0; i<60; i++) {  
				if(i < 10){
					updMin += ("<option value='"+i+"'>"+'0'+i+"</option>");
				} else {
					updMin += ("<option value='"+i+"'>"+i+"</option>");
				}
			}
			$('#updMin').html(updMin);
			$('#updMin2').val(data.apInfoVo.updMin); 
			//console.log($('#updMin2').val());

			let min = document.getElementById('updMin');
			let len2 = min.options.length;
			let str2 = document.getElementById('updMin2');
			
			for (let i=0; i<len2; i++){  
				if(min.options[i].value == str2.value){
					min.options[i].selected = true;
				}
			} 
			/* conn_time min 설정 끝*/

			$("#updateModal").modal("show");
		}
	});
}
function updateApInfo(mac, s_code){
	
	//AP NAME
    if ('' == $("#updApName").val()) {
        alert('<spring:message code="confirm.enter.apManagementName" />');
        $("#updApName").focus();
        return;
    }
    
	//시리얼번호
    if ('' == $("#updSerial").val()) {
        alert('<spring:message code="confirm.enter.serialNumber" />');
        $("#updSerial").focus();
        return;
    }
	
    //내부아이피대역
    if ('' == $("#updIpNumber").val()) {
        alert('<spring:message code="confirm.enter.internalIPband" />');
        $("#updIpNumber").focus();
        return;
    }
    
  	//동시접속자
    if ('' == $("#updConUser").val()) {
        alert('<spring:message code="confirm.enter.concurrentUsers" />');
        $("#updConUser").focus();
        return;
    }
 
  	//동시접속자
    if (!$.isNumeric($("#updConUser").val())) {
        alert('<spring:message code="confirm.wrong.numberConcurrentUsers" />');
        $("#updConUser").focus();
        return;
    }
	
  	$('#updHour, #updMin').prop('disabled', false);
  	
	let datas = $("#updApInfoForm").serialize();
	
	$.ajax({
		type : "POST",
		url : "/apinfo/apinfoUpdate.ajax",
		data : "ajax=true&" + datas,
		dataType : "json",
		error	: function (e) { alert(error); },
		success : function(data) {
			if(data.result == "1") {
				alert(success);
				document.location.reload();
			} else if(data.result == "-3"){
				alert('<spring:message code="confirm.exists.apName" />')
			} else {
				alert('<spring:message code="confirm.exists.mac" />');
			}
		}
	});
	return;
}

function delApInfo(){
	let mac = $('#updMac').val();
	let s_code = $('#updScode').val();
	
	if(!confirm(mac + ' ' + '<spring:message code="confirm.delete.mac" />')){
		return;
	}
	
	$.ajax({
		type : "POST",
		url : "/apinfo/apinfoDelete.ajax",
		data : {
					'mac' : mac,
					's_code' : s_code
			   },
		dataType : "json",
		error	: function (e) { alert(error);},
		success : function(data) {
			if(data.result == "1") {
				alert(del);
				document.location.reload();
			}else {
				alert(error);
			}
		}
	});
}

function reboot(){
	
	let mac = $('#updMac').val();
	let apName = $('#updApName').val();
	let language = '${sessionScope.language}';
	
	if(language === 'korean'){
		if(!confirm(apName + '<spring:message code="confirm.run.reboot" />')){
			return;
		}
	} else {
		if(!confirm('<spring:message code="confirm.run.reboot" />' + apName + "?")){
			return;
		}
	}
	
	$.ajax({
		type : "POST",
		url : "/apinfo/reboot.ajax",
		data : {'mac' : mac},
		dataType : "json",
		error	: function (e) { alert(error);},
		success : function(data) {
			if(data.result == "1") {
				alert(success);
				document.location.reload();
			}else {
				alert(error);
			}
		}
	});
}

//엑셀 다운로드
function goExcelDown() {
	
	let msg = "";
	
	msg = prompt(downloadExcelMessage);

	if(!msg) {
		alert(downloadCanceledMessage);
		return;
	}
	
	$("input[name ='excelMsg']").val(msg);
	
	$("#searchApInfoForm").attr("action", "/apinfo/apinfoExcelDownload.do");
    $("#searchApInfoForm").submit();
}

</script>
</head>
<body>

	<jsp:include page="../header.jsp" flush="false"/>
	
	<div class="contents-area">
		<header>
			<i class="icon-list"><span>icon</span></i>
			<h2><spring:message code="apinfo.wapManagement" /></h2>
		</header>
		<form name="searchApInfoForm" id="searchApInfoForm" method="post">
			<input type="hidden" id="pageno" name="pageno" value="${apInfoVo.pageno }"/>
			<input type="hidden" id="excelMsg" name="excelMsg" value="">
			<div class="search-area">
				<select id="searchCompId" name="searchCompId">
					<option value=""><spring:message code="select.shipOwner" /></option>
					<c:forEach var="comp" items="${compList }">
						<option value="${comp.comp_id}" <c:if test="${comp.comp_id eq apInfoVo.searchCompId }">selected</c:if>>
							<c:out value="${comp.comp_name}" />
						</option>
					</c:forEach>
				</select>
				<select id="searchStatus" name="searchStatus">
					<option value=""><spring:message code="select.wapConnectionStatus" /></option>
					<option value="1" ${'1' eq apInfoVo.searchStatus ? 'selected' : '' }>
						<spring:message code="select.connected" />
					</option>
					<option value="2" ${'2' eq apInfoVo.searchStatus ? 'selected' : '' }>
						<spring:message code="select.notConnected" />
					</option>
				</select>
				<input type="text" id="searchShipName" name="searchShipName" class="w150" maxlength="" 
					value="${apInfoVo.searchShipName }" placeholder="<spring:message code="search.enterShipName" />">
				<input type="text" id="searchApName" name="searchApName" class="w180" maxlength="" 
					value="${apInfoVo.searchApName }" placeholder="<spring:message code="search.enterWapName" />">
				<button class="btn-md white" type="button" id="btnSearch">
					<span><spring:message code="button.search" /></span>
				</button>
			</div>
			<div class="board-area">
				<div class="scroll-x">
					<table class="tbl-default bg-on">
						<colgroup>
							<col style="width:150px;">
							<col style="width:160px;">
							<col style="width:180px;">
							<col style="width:120px;">
							<col style="width:120px;">
							<col style="width:120px;">
							<col style="width:120px;">
							<col style="width:100px;">
							<col style="width:120px;">
							<col style="width:120px;">
							<col style="width:150px;">
							<col style="width:150px;">
							<col style="width:250px;">
						</colgroup>
						<thead>
							<tr>
								<th scope="col"><spring:message code="select.shipOwner" /></th>
								<th scope="col"><spring:message code="select.shipName" /></th>
								<th scope="col"><spring:message code="list.wapName" /></th>
								<th scope="col">MAC</th>
								<th scope="col"><spring:message code="list.serialNumber" /></th>
								<th scope="col"><spring:message code="list.bandWidthKBPS" /></th>
								<th scope="col"><spring:message code="list.concurrentUsers" /></th>
								<th scope="col"><spring:message code="list.status" /></th>
								<th scope="col"><spring:message code="list.externalIP" /></th>
								<th scope="col"><spring:message code="list.internalIPband" /></th>
								<th scope="col"><spring:message code="list.communicationServer" /></th>
								<th scope="col"><spring:message code="list.wifiRetentionTime" /></th>
								<th scope="col"><spring:message code="list.description" /></th>
							</tr>
						</thead>
						<tbody>
							<c:choose>
								<c:when test="${fn:length(apInfoList) > 0 }">
									<c:forEach var="item" items="${apInfoList }" varStatus="idx" >
										<tr id="list_${item.mac }" onClick="goUpdate('${item.mac}', '${item.s_code}')" >							
						  					<td>${item.comp_name }</td>
						  					<td>${item.s_name }</td>
						  					<td>${item.ap_name }</td>
						  					<td>${item.mac }</td>
						  					<td>${item.serial_no }</td>
						  					<td>
						  						<c:set var="width" value="${item.band_width }" />
												<c:choose>
													<c:when test="${width == 0 }"><spring:message code="list.noLimit" /></c:when>
													<c:when test="${width != 0 }">${width } kbps</c:when>
												</c:choose>
						  					</td>
						  					<td>${item.con_user }</td>
						  					<td>
						  						<c:set var="status" value="${item.status }" />
						  						<c:choose>
							  						<c:when test="${status == 1 }"><img src="/web/images/apinfo/green.png" width=13 height=13></c:when>
													<c:when test="${status != 1 }"><img src="/web/images/apinfo/red.png" width=13 height=13></c:when>
												</c:choose> 
						  					</td>
						  					<td>${item.fw_version }
						  						<%-- <c:set var="fwVersion" value="${item.fw_version }" />
						  						<c:choose>
						  							<c:when test="${fwVersion == null }">""</c:when>
						  							<c:when test="${fwVersion == '' }">""</c:when>
						  							<c:when test="${fwVersion != null }">${item.fw_version }</c:when>
						  						</c:choose> --%>
						  					</td>
						  					<td>
						  						192.168.${item.ip_no }.1
						  					</td>
						  					<td>
						  						<c:set var="connFlag" value="${item.wifi_conn_flag }" />
						  						<c:choose>
						  							<c:when test="${connFlag == null }"><spring:message code="list.use" /></c:when>
						  							<c:when test="${connFlag == 0 }"><spring:message code="list.use" /></c:when>
						  							<c:when test="${connFlag != 0 }"><spring:message code="list.notUsed" /></c:when>
						  						</c:choose>
						  					</td>
						  					<td>${item.conn_time }
						  						<%-- <c:set var="connTime" value="${item.conn_time }" />
						  						<c:choose>
						  							<c:when test="${connTime == null }">0</c:when>
						  							<c:when test="${connTime == '' }">0</c:when>
						  							<c:when test="${connTime != null }">${item.conn_time }</c:when>
						  						</c:choose> --%>
						  					</td>
						  					<td>
						  						<c:set var="descr" value="${item.descr }"/>
						  						<c:choose>
						  							<c:when test="${descr == null }">""</c:when>
						  							<c:when test="${descr != null }">${item.descr }</c:when>
						  						</c:choose>
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
					<c:choose>
        				<c:when test="${sessionScope.comp_id eq 0}">
							<div class="fleft"> 
						    	<button type="button" class="btn-lg blue" id="add" data-bs-toggle="modal" data-bs-target="#addModal">
						    		<span><spring:message code="button.add" /></span>
						    	</button>
						    </div> 
						</c:when>
						<%-- <c:otherwise>
				            <div class="fleft" style="display: none;"> 
				                <button type="button" class="btn-lg blue" id="add" data-bs-toggle="modal" data-bs-target="#addModal">
				                	<span>등 록</span>
				                </button>
				            </div> 
				        </c:otherwise> --%>
				    </c:choose>
					<div class="fright">
						<button type="button" id="btnExcel" name="btnExcel" class="btn-lg green">
							<span><spring:message code="button.exportToExcel" /></span>
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
						<h2><spring:message code="apinfo.wapManagement" /></h2>
						<div class="cancelArea" data-bs-dismiss="modal">
							<img class="cancelX" src="../web/images/common/btn-layer-close.png">
						</div>
					</header>
					<form name="addApInfoForm" id="addApInfoForm" method="post">
						<div class="board-area">
							<table class="tbl-write">
								<colgroup>
									<col style="width:20%;">
									<col style="">
									<col style="width:20%;">
									<col style="width:30%;">
								</colgroup>
									<tbody>
									<tr>
										<th>
											<label for="addMac" style="text-align:left;">
												MAC
												<span class="key">*</span>
											</label>
										</th>
										<td>
											<input type="text" name="addMac" id="addMac" class="width100" maxlength=12/>
										</td>
										<th>
											<label for="addApName">
												<spring:message code="list.wapName" />
												<span class="key">*</span>
											</label>
										</th>
										<td>
											<input type="text" name="addApName" id="addApName" class="width100" maxlength=12/>
										</td>
									</tr>
									<tr>
										<th>
											<label for="addCompId">
												<spring:message code="select.shipOwner" />
												<span class="key">*</span>
											</label>
										</th>
										<td>
											<select id="addCompId" name="addCompId">
												<option value=""><spring:message code="select.select" /></option>
												<c:forEach var="ship" items="${compList }">
													<option value="${ship.comp_id}" <%-- <c:if test="${ship.comp_id eq cuserVo.searchCompId }">selected</c:if> --%>>
														<c:out value="${ship.comp_name}" />
													</option>
												</c:forEach>
											</select>
										</td>
										<th>
											<label for="addShipName">
												<spring:message code="select.shipName" />
												<span class="key">*</span>
											</label>
										</th>
										<td>
											<select id="addShipName" name="addShipName">
												<option value=""><spring:message code="select.select" /></option>
											</select>
										</td>
									</tr>
									<tr>
										<th>
											<label for="addSerial">
												<spring:message code="list.serialNumber" />
												<span class="key">*</span>
											</label>
										</th>
										<td>
											<input type="text" name="addSerial" id="addSerial" class="inp250" />
										</td>
										<th>
											<label for="addIpNumber">
												<spring:message code="list.internalIPband" />
												<span class="key">*</span>
											</label>
										</th>
										<td>
											192.168. <input type="text" name="addIpNumber" id="addIpNumber" class="width5" maxlength="" /> .1
										</td>
									</tr>
									<tr>
										<th>
											<label for="addBand">
												<spring:message code="list.bandWidthKBPS" />
												<span class="key">*</span>
											</label>
										</th>
										<td>
											<select id="addBand" name="addBand" class="sbox">
												<option value="0"><spring:message code="list.noLimit" /></option>
												<c:forEach var="band" items="${bandList }">
													<option value="${band.band_width }">
														<c:out value="${band.band_width }" />
													</option>
												</c:forEach>
											</select>
										</td>
										<th>
											<label for="addConUser">
												<spring:message code="list.concurrentUsers" />
												<span class="key">*</span>
											</label>
										</th>
										<td>
											<input type="text" name="addConUser" id="addConUser" class="width100" maxlength=""/>
										</td>
									</tr>
									<tr>
										<th>
											<label for="addWIFI">
												<spring:message code="list.communicationServer" />
												<span class="key">*</span>
											</label>
										</th>
										<td>
											<select id="addWIFI" name="addWIFI" class="sbox">
												<option value="0"><spring:message code="list.use" /></option>
												<option value="1"><spring:message code="list.notUsed" /></option>
											</select>
										</td>
										<th>
											<label for="addWIFI">
												<spring:message code="list.wifiRetentionTime" />
												<span class="key">*</span>
											</label>
										</th>
										<td>
											<select class="sbox" id="addHour" name="addHour">					
												<c:forEach begin="0" end="23" var="hour">
							                    	<c:set var="setHour" value="${hour}" />
							                        	<c:if test="${hour < 10}">
							                            	<c:set var="setHour" value='0${hour}' />
							                            </c:if>
							                            <option value="${setHour}" <%-- ${setHour eq apInfoVo.addHour ? 'selected' : ''} --%>>
							                            	<c:out value="${setHour}" />
							                            </option>
							                    </c:forEach>
											</select>
											<label><spring:message code="list.hourly" />&nbsp;</label>
											<select class="sbox" id="addMin" name="addMin" >
												<c:forEach begin="0" end="59" var="min">
							                    	<c:set var="setMin" value="${min}" />
							                        	<c:if test="${min < 10}">
							                            	<c:set var="setMin" value='0${min}' />
							                            </c:if>
							                            <option value="${setMin}" <%-- ${setMin eq apInfoVo.addMin ? 'selected' : ''} --%>>
							                            	<c:out value="${setMin}" />
							                            </option>
							                    </c:forEach>
											</select>
											<label><spring:message code="list.minutes" /></label>
											<div class="mt05">
												<span class="check-box1">
													<input type="checkbox" id="addUnLimit" name="addUnLimit" value="UnLimit" 
														<c:if test="${fn:contains(alInfoVo.conn_time, null) }">checked</c:if>/>
													<label for="addUnLimit" class="fs-11"><spring:message code="list.noLimit" /></label>
												</span>
											</div>
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
						<h2><spring:message code="apinfo.wapManagement" /></h2>
						<div class="cancelArea" data-bs-dismiss="modal">
							<img class="cancelX" src="../web/images/common/btn-layer-close.png">
						</div>
					</header>
					<form name="updApInfoForm" id="updApInfoForm" method="post">
						<input type="hidden" id="updMac" name="updMac">
						<input type="hidden" id="updScode" name="updScode">
						<input type="hidden" id="updHour2" name="updHour2" value="${updHour2 }"/>
						<input type="hidden" id="updMin2" name="updMin2" value="${updMin2 }"/>
						<div class="board-area">
							<table class="tbl-write">
								<colgroup>
									<col style="width:20%;">
									<col style="">
									<col style="width:20%;">
									<col style="width:30%;">
								</colgroup>
									<tbody>
									<tr>
										<th><label for="updMac" >MAC<span class="key">*</span></label></th>
										<td>
											<input  name="u_updMac" id="u_updMac" class="width100" maxlength=12/>
										</td>
										<th>
											<label for="updApName">
												<spring:message code="list.wapName" />
												<span class="key">*</span>
											</label>
										</th>
										<td>
											<input type="text" name="updApName" id="updApName" class="width100" maxlength=12 />
										</td>
									</tr>
									<tr>
										<th>
											<label for="updCompId">
												<spring:message code="select.shipOwner" />
												<span class="key">*</span>
											</label>
										</th>
										<td>
											<input name="updCompId" id="updCompId" class="width100" />
										</td>
										<th>
											<label for="updShipName">
												<spring:message code="select.shipName" />
												<span class="key">*</span>
											</label>
										</th>
										<td>
											<input name="updShipName" id="updShipName" class="width100" />
										</td>
									</tr>
									<tr>
										<th>
											<label for="updSerial">
												<spring:message code="list.serialNumber" />
												<span class="key">*</span>
											</label>
										</th>
										<td>
											<input type="text" name="updSerial" id="updSerial" class="inp250" />
										</td>
										<th>
											<label for="updIpNumber">
												<spring:message code="list.internalIPband" />
												<span class="key">*</span>
											</label>
										</th>
										<td>
											192.168. <input type="text" name="updIpNumber" id="updIpNumber" class="width5" maxlength="" /> .1
										</td>
									</tr>
									<tr>
										<th>
											<label for="updBand">
												<spring:message code="list.bandWidthKBPS" />
												<span class="key">*</span>
											</label>
										</th>
										<td>
											<select id="updBand" name="updBand" class="sbox">
												<option value="0"><spring:message code="list.noLimit" /></option>
												<c:forEach var="band" items="${bandList }">
													<option value="${band.band_width }">
														<c:out value="${band.band_width }" />
													</option>
												</c:forEach>
											</select>
										</td>
										<th>
											<label for="updConUser">
												<spring:message code="list.concurrentUsers" />
												<span class="key">*</span>
											</label>
										</th>
										<td>
											<input type="text" name="updConUser" id="updConUser" class="width100" maxlength=""/>
										</td>
									</tr>
									<tr>
										<th>
											<label for="updWIFI">
												<spring:message code="list.communicationServer" />
												<span class="key">*</span>
											</label>
										</th>
										<td>
											<select id="updWIFI" name="updWIFI" class="sbox">
												<option value="0"><spring:message code="list.use" /></option>
												<option value="1"><spring:message code="list.notUsed" /></option>
											</select>
										</td>
										<th>
											<label>
												<spring:message code="list.wifiRetentionTime" />
												<span class="key">*</span>
											</label>
										</th>
										<td>
											<select class="sbox" id="updHour" name="updHour" disabled>	
												<%-- <c:forEach begin="0" end="23" var="hour">
							                    	<c:set var="setHour" value="${hour}" />
							                        	<c:if test="${hour < 10}">
							                            	<c:set var="setHour" value='0${hour}' />
							                            </c:if>
							                            <option value="${setHour}" ${setHour eq apInfoVo.updHour ? 'selected' : ''}>
							                            	<c:out value="${setHour}" />
							                            </option>
							                    </c:forEach> --%>					
											</select>
											<label><spring:message code="list.hourly" />&nbsp;</label>
											<select class="sbox" id="updMin" name="updMin" disabled>	
												<%-- <c:forEach begin="0" end="59" var="min">
							                    	<c:set var="setMin" value="${min}" />
							                        	<c:if test="${min < 10}">
							                            	<c:set var="setMin" value='0${min}' />
							                            </c:if>
							                            <option value="${setMin}" ${setMin eq apInfoVo.updMin ? 'selected' : ''}>
							                            	<c:out value="${setMin}" />
							                            </option>
							                    </c:forEach> --%>	
											</select>
											<label><spring:message code="list.minutes" /></label>
											<div class="mt05">
												<span class="check-box1">
													<input type="checkbox" id="updConnTimeChange" name="updConnTimeChange" value="unLimit"/>
													<label for="updConnTimeChange"><spring:message code="cuser.change" /></label>
												</span>
												<span class="check-box1">
													<input type="checkbox" id="updUnLimit" name="updUnLimit" value="unLimit" disabled>
													<label for="updUnLimit"><spring:message code="list.noLimit" /></label>
												</span>
											</div>
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
							<c:choose>
        						<c:when test="${sessionScope.comp_id eq 0}">	
									<div class="fleft" id="fleft">
										<button type="button" class="btn-lg yellow" id="btnDel">
											<span><spring:message code="button.delete" /></span>
										</button>
										<button type="button" id="btn_reboot" class="btn-lg white">
											<span><spring:message code="button.reboot" /></span>
										</button>
									</div>
									<div class="fright">
										<button class="btn-lg burgundy btn-modal" type="button" name="btnUpd"  id=btnUpd>
											<span><spring:message code="button.save" /></span>						
										</button>
										<button class="btn-lg gray btn-modal" type="button" name="cancel" data-bs-dismiss="modal">
											<span><spring:message code="button.cancel" /></span>
										</button>
									</div>
								</c:when>
								<c:otherwise>
									<div class="fright">
										<button class="btn-lg gray btn-modal" type="button" name="cancel" data-bs-dismiss="modal">
											<span><spring:message code="button.cancel" /></span>
										</button>
									</div>
								</c:otherwise>
							</c:choose>
						</div>
					</div>
				</section>
			</div>
		</div>
	</div>
	
</html>