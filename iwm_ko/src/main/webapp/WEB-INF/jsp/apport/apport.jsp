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
	let allPorts = '<spring:message code="apport.allPorts1" />';
	let allowed = '<spring:message code="apport.allPorts2" />';
	let disabled = '<spring:message code="apport.allPorts3" />';
	let koreanEnd = '<spring:message code="apport.allPorts4" />';
	let close = '<spring:message code="button.close" />';

$(document).ready(function(){

	// 등록, 수정 모달 s_code, s_name set
	let selectedShipCode = $('#searchShipList option:selected').val();
	let selectedShipName = '${portList[0].s_name}';
	$('#addShipName').val(selectedShipName);
	$('#addScode').val(selectedShipCode);
	$('#updShipName').val(selectedShipName);
	$('#updScode').val(selectedShipCode);

	// 모달
	$('#addModal').on('hidden.bs.modal', function (e) {
		$('#addApportForm')[0].reset();
		// 추가모달 s_name 초기화 방지
		$('#addShipName').val(selectedShipName);
	});
	
 	// 추가
    $("#btnAdd").bind("click", function() {  	
    	addApport();
    });
 	
 	// 수정
    $("#btnUpd").bind("click", function() {
    	updateApport();
    });
 	
 	// 삭제
    $("#btnDel").bind("click", function() {
    	delApport();
    });
 	
	// excel
    $("#btnExcel").bind("click", function() {
    	
    	let s_code = $('#searchShipList option:selected').val();
    	
    	let msg = prompt(downloadExcelMessage);
    	if(!msg) {
    		alert(downloadCanceledMessage);
    		return;
    	}
    	
    	$("input[name ='excelMsg']").val(msg);
    	goExcelDown(msg);
    });
	
	// searchShipList 변경에 따른 portList get
	$('#searchShipList').on('change', () => {		
		$("#searchApportForm").attr("action", "/apport/apport.do");
	    $("#searchApportForm").submit();
	});	

	// 모든포트사용 허용 및 해제
	$('#allowAllPort').on('change', () => {
		
		let port = $('input[id=allowAllPort]').prop('checked');
		let s_code = $('#searchShipList option:selected').val();
		
		let language = '${sessionScope.language}';
		
		$.ajax({
			type : "POST",
			url : "/apport/allowAllPort.ajax",
			data : {
						'allowAllPort' : port,
						's_code' : s_code
				   },
			dataType : "json",
			error	: function (e) { alert(error); },
			success : function(data) {

				if ($('.modal-popup').length > 0) {
					closeModal();
				}
				
				if(language === 'korean'){				
					if(port == true){
						result = "<div class='modal-popup'><div class='modal-wrap'><div class='modal-msg'><div class='content-wrap'>";
						result += "<div class='a-center'><p class='fs-14'><strong>" + allPorts + " <span class='fc-red'>" + allowed + "</span>" + koreanEnd + "</strong></div>";
		    			result += "<div class='btn-area a-center'>";
		    			result += "<button class='btn-lg gray btn-modal-close' type='button' name='cancel' ><span>" + close + "</span></button></div></div>";
		    			result += "<button type='button' class='btn-modal-close btn-close'><span>" + close + "</span></button></div></div></div>";
					} else {
						result = "<div class='modal-popup'><div class='modal-wrap'><div class='modal-msg'><div class='content-wrap'>";
						result += "<div class='a-center'><p class='fs-14'><strong>" + allPorts + " <span class='fc-red'>" + disabled + "</span>" + koreanEnd + "</strong></div>";
		    			result += "<div class='btn-area a-center'>";
		    			result += "<button class='btn-lg gray btn-modal-close' type='button' name='cancel' ><span>" + close + "</span></button></div></div>";
		    			result += "<button type='button' class='btn-modal-close btn-close'><span>" + close + "</span></button></div></div></div>";
					}
				} else {
					if(port == true){
						result = "<div class='modal-popup'><div class='modal-wrap'><div class='modal-msg'><div class='content-wrap'>";
						result += "<div class='a-center'><p class='fs-14'><strong>" + allPorts + " <span class='fc-red'>" + allowed + "</span></strong></div>";
		    			result += "<div class='btn-area a-center'>";
		    			result += "<button class='btn-lg gray btn-modal-close' type='button' name='cancel' ><span>" + close + "</span></button></div></div>";
		    			result += "<button type='button' class='btn-modal-close btn-close'><span>" + close + "</span></button></div></div></div>";
					} else {
						result = "<div class='modal-popup'><div class='modal-wrap'><div class='modal-msg'><div class='content-wrap'>";
						result += "<div class='a-center'><p class='fs-14'><strong>" + allPorts + " <span class='fc-red'>" + disabled + "</span></strong></div>";
		    			result += "<div class='btn-area a-center'>";
		    			result += "<button class='btn-lg gray btn-modal-close' type='button' name='cancel' ><span>" + close + "</span></button></div></div>";
		    			result += "<button type='button' class='btn-modal-close btn-close'><span>" + close + "</span></button></div></div></div>";
					} 
				}
	   			
				$('body').append(result);

			}
		});
	});
	
	// 모든선박에적용
	$('#setAllShip').on('click', () => {
		if(!confirm('<spring:message code="confirm.run.applyAllShips" />')) {
			return false;
		}	
		
		$('.contents-area').append('<div class="ajax-loading"></div>');
		
		let port = $('input[id=allowAllPort]').prop('checked');
		let s_code = $('#searchShipList option:selected').val();
		//console.log(port);
		//console.log(s_code);
		
		$.ajax({
			type : "POST",
			url : "/apport/setAllShip.ajax",
			data : {
						'allowAllPort' : port,
						's_code' : s_code
				   },
			dataType : "json",
			error	: function (e) { alert(error); },
			success : function(data) {
				if(data.result == "1") {
					alert(success);
					document.location.reload();
				} else {
					alert(error);
				}
			}
		});
	});
});

function addApport(){	

    if ('' == $("#addRuleName").val()) {
        alert('<spring:message code="confirm.enter.name" />');
        $("#addRuleName").focus();
        return;
    }
    
    if ('' == $("#addPortFrom").val()) {
        alert('<spring:message code="confirm.enter.portStartRange" />');
        $("#addPortFrom").focus();
        return;
    }
    
    if ('' == $("#addPortTo").val()) {
        alert('<spring:message code="confirm.enter.portEndRange" />');
        $("#addPortTo").focus();
        return;
    }
    
    if (!$.isNumeric($("#addPortFrom").val())) {
        alert('<spring:message code="confirm.wrong.allowedPortRange" />');
        $("#addPortFrom").focus();
        return;
    }
    
    if (!$.isNumeric($("#addPortTo").val())) {
        alert('<spring:message code="confirm.wrong.allowedPortRange" />');
        $("#addPortTo").focus();
        return;
    }
 
    if (parseInt($("#addPortFrom").val()) > parseInt($("#addPortTo").val())) {
        alert('<spring:message code="confirm.wrong.greeterThenPort" />');
        $("#addPortFrom").focus();
        return;
    }
    
    if ($("#addPortFrom").val() > 65535 || $("#addPortFrom").val() < 0) {
        alert('<spring:message code="confirm.wrong.portRangeLength" />');
        $("#addPortFrom").focus();
        return;
    }
    
    if ($("#addPortTo").val() > 65535 || $("#addPortTo").val() < 0) {
        alert('<spring:message code="confirm.wrong.portRangeLength" />');
        $("#addPortTo").focus();
        return;
    }
    
    let addScode = $('#searchShipList').val();
    let datas = $("#addApportForm").serialize();
	$.ajax({
		type : "POST",
		url : "/apport/apportAdd.ajax",
		data : "ajax=true&" + datas,
		dataType : "json",
		error	: function (e) { alert(error);},		
		success : function(data) {						
			if(data.result == "1") {
				alert(success);
				document.location.reload();
			} else if(data.result == "-2") {
				alert('<spring:message code="confirm.exists.name" />');
			} else if(data.result == "-3"){
				alert('<spring:message code="confirm.exists.range" />')
			} else { 
				alert(error);
			}
		}
	});
	return;
}

function goUpdate(rule_name){
	
	let s_code = $('#searchShipList option:selected').val();
	
	$.ajax({
		type : "POST",
		url : "/apport/apportUpdateData.ajax",
		data : {
					's_code' : s_code,
					'rule_name' : rule_name,
			   },
		dataType : "json",
		error	: function (e) { alert(error); },
		success : function(data) {
			
			$('#updShipName').val();
			$('#updRuleName').val(rule_name);
			$('#updPortFrom').val(data.apportVo.port_from);
			$('#updPortTo').val(data.apportVo.port_to);
			$('#updUseFlag').val(data.apportVo.use_flag);
			$('#updDescr').val(data.apportVo.descr);
		
			$("#updateModal").modal("show");
		}
	});
}

function updateApport(){
	
	if ('' == $("#updPortFrom").val()) {
        alert('<spring:message code="confirm.enter.portStartRange" />');
        $("#updPortFrom").focus();
        return;
    }
    
    if ('' == $("#updPortTo").val()) {
        alert('<spring:message code="confirm.enter.portEndRange" />');
        $("#updPortTo").focus();
        return;
    }
    
    if (!$.isNumeric($("#updPortFrom").val())) {
        alert('<spring:message code="confirm.wrong.allowedPortRange" />');
        $("#updPortFrom").focus();
        return;
    }
    
    if (!$.isNumeric($("#updPortTo").val())) {
        alert('<spring:message code="confirm.wrong.allowedPortRange" />');
        $("#updPortTo").focus();
        return;
    }
    
    if (parseInt($("#updPortFrom").val()) > parseInt($("#updPortTo").val())) {
        alert('<spring:message code="confirm.wrong.greeterThenPort" />');
        $("#updPortFrom").focus();
        return;
    }
    
    if ($("#updPortFrom").val() > 65535 || $("#updPortFrom").val() < 0) {
        alert('<spring:message code="confirm.wrong.portRangeLength" />');
        $("#updPortFrom").focus();
        return;
    }
    
    if ($("#updPortTo").val() > 65535 || $("#updPortTo").val() < 0) {
        alert('<spring:message code="confirm.wrong.portRangeLength" />');
        $("#updPortTo").focus();
        return;
    }
    
	let updRuleName = $('#updRuleName').val();
	let updScode = $('#updScode').val();
	let updPortFrom = $('#updPortFrom').val();
	let updPortTo = $('#updPortTo').val();
	let updUseFlag = $('#updUseFlag').val();
	let updDescr = $('#updDescr').val();
	
	$.ajax({
		type : "POST",
		url : "/apport/apportUpdate.ajax",
		data : {
				"updRuleName" : updRuleName,
				"updScode" : updScode,
				"updPortFrom" : updPortFrom,
				"updPortTo" : updPortTo,
				"updUseFlag" : updUseFlag,
				"updDescr" : updDescr
			   },
		dataType : "json",
		error	: function (e) { alert(error); },
		success : function(data) {
			if(data.result == "1") {
				alert(success);
				document.location.reload();
			} else if(data.result == "-3"){
				alert('<spring:message code="confirm.exists.range" />')
			} else {
				alert(error);
			}
		}
	});
	return;
}

function delApport(){
	
	let s_code = $('#updScode').val();
	let rule_name = $('#updRuleName').val();
	
	if(!confirm(rule_name + '<spring:message code="confirm.delete.wapPort" />')){
		return;
	}
	
	$.ajax({
		type : "POST",
		url : "/apport/apportDelete.ajax",
		data : {
					's_code' : s_code,
					'rule_name' : rule_name
			   },
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
	$("#searchApportForm").attr("action", "/apport/apportExcelDownload.do");
	$("#searchApportForm").submit();
}

</script>
</head>
<body>

	<jsp:include page="../header.jsp" flush="false"/>
	
	<div class="contents-area">
		<header>
			<i class="icon-list"><span>icon</span></i>
			<h2><spring:message code="apport.wapPortUsage" /></h2>
		</header>
		<form name="searchApportForm" id="searchApportForm" method="post">
			<input type="hidden" id="excelMsg" name="excelMsg" value="">
			<input type="hidden" id="pageno" name="pageno" value="${apportVo.pageno }"/>
			<input type="hidden" id="s_code" name="s_code" value=""/>
			<%-- <input type="hidden" id="selectShipName" value="${selectShipName }" /> --%>
			<input type="hidden" id="selectedScode" value="${searchShipList }"/>
			<div class="search-area">
				<select id="searchShipList" name="searchShipList">
					<c:forEach var="ship" items="${shipList }">
						<option value="${ship.s_code}" <c:if test="${ship.s_code eq apportVo.searchShipCode }">selected</c:if> >
							<c:out value="${ship.comp_name} - ${ship.s_name}" />
						</option>
					</c:forEach>
				</select>
				<button type="button" id="setAllShip" name="setAllShip" class="btn-md white">
					<span><spring:message code="button.applyToAllShips" /></span>
				</button>
				<div class="fright mt07">
					<span class="check-box1">
						<input type="checkbox" id="allowAllPort" name="allowAllPort" checked> 
						<label for="allowAllPort"><spring:message code="button.allowAllPorts" /></label>
					</span>
				</div>
			</div>
			<div class="board-area">
				<table class="tbl-default bg-on">
					<colgroup>
						<col style="width:12%;">
						<col style="width:12%;">
						<col style="width:15%;">
						<col style="width:12%;">
						<col style="width:8%;">
						<col style="">
					</colgroup>
					<thead>
						<tr>
							<th scope="col"><spring:message code="list.name" /></th>
							<th scope="col"><spring:message code="select.shipOwner" /></th>
							<th scope="col"><spring:message code="select.shipName" /></th>
							<th scope="col"><spring:message code="list.allowedPortRange" /></th>
							<th scope="col"><spring:message code="list.allowed" /></th>
							<th scope="col"><spring:message code="list.description" /></th>
						</tr>
					</thead>
					<tbody id="port">
						<c:choose>
							<c:when test="${fn:length(portList) > 0 }">
								<c:forEach var="item" items="${portList }" varStatus="idx" >
									<tr id="list_${item.rule_name }" onClick="goUpdate('${item.rule_name}')" >
										<td>${item.rule_name }</td>
										<td>${item.comp_name }</td>
										<td>${item.s_name }</td>
										<td>${item.port_from } ~ ${item.port_to }</td>
										<td>${item.use_flag }</td>
										<td>${item.descr }</td>
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
				<div class="btn-area">
					<div class="fleft"> 
				    	<button type="button" class="btn-lg blue" data-bs-toggle="modal" data-bs-target="#addModal" id="adModal">
				    		<span><spring:message code="button.add" /></span>
				    	</button>
				    </div> 
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
						<h2><spring:message code="apport.wapPortUsage" /></h2>
						<div class="cancelArea" data-bs-dismiss="modal">
							<img class="cancelX" src="../web/images/common/btn-layer-close.png">
						</div>
					</header>
					<form name="addApportForm" id="addApportForm" method="post">
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
										<th><label for="addShipName"><spring:message code="select.shipOwner" /></label></th>
										<td>
											<input name="addShipName" id="addShipName" value="${apportVo.searchShipName }" disabled/>
											<input type="hidden" name="addScode" id="addScode"/>
										</td>
										<th>
											<label for="addRuleName">
												<spring:message code="list.name" />
												<span class="key">*</span>
											</label>
										</th>
										<td>
											<input type="text" name="addRuleName" id="addRuleName" class="width100"/>
										</td>
									</tr>
									<tr>
										<th>
											<label for="addPort">
												<spring:message code="list.allowedPortRange" />
												<span class="key">*</span>
											</label>
										</th>
										<td>
											<input class="width45" type="text" name="addPortFrom" id="addPortFrom"/> ~
											<input class="width45" type="text" name="addPortTo" id="addPortTo"/> 
										</td>
										<th>
											<label for="addUseFlag">
												<spring:message code="list.allowed" />
												<span class="key">*</span>
											</label>
										</th>
										<td>
											<select name="addUseFlag" id="addUseFlag">
												<option value="Y"><spring:message code="list.allow" /></option>
												<option value="N"><spring:message code="list.block" /></option>
											</select>
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
								<button class="btn-lg burgundy btn-modal" type="button" name="btnAdd"  id=btnAdd>
									<span><spring:message code="button.save" /></span>
								</button>
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
	
	<!-- 수정 모달 -->
	<div class="modal fade" id="updateModal" data-bs-backdrop="static"> 
    	<div class="modal-dialog modal-lg modal-dialog-centered" role="document"> 
        	<div class="modal-content"> 
				<section class="contents-area"> 
					<header>
						<i class="icon-write"><span>icon</span></i>
						<h2><spring:message code="apport.wapPortUsage" /></h2>
						<div class="cancelArea" data-bs-dismiss="modal">
							<img class="cancelX" src="../web/images/common/btn-layer-close.png">
						</div>
					</header>
					<form name="updApportForm" id="updApportForm" method="post">
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
										<th><label for="updShipName"><spring:message code="select.shipName" /></label></th>
										<td>
											<input name="updShipName" id="updShipName" disabled/>
											<input type="hidden" name="updScode" id="updScode" />
										</td>
										<th><label for="updRuleName"><spring:message code="list.name" /><span class="key">*</span></label></th>
										<td>
											<input name="updRuleName" id="updRuleName" class="width100" disabled/>
										</td>
									</tr>
									<tr>
										<th>
											<label for="updPort">
												<spring:message code="list.allowedPortRange" />
												<span class="key">*</span>
											</label>
										</th>
										<td>
											<input class="width45" type="text" name="updPortFrom" id="updPortFrom"/> ~
											<input class="width45" type="text" name="updPortTo" id="updPortTo"/>		
										</td>
										<th>
											<label for="updUseFlag">
												<spring:message code="list.allowed" />
												<span class="key">*</span>
											</label>
										</th>
										<td>
											<select name="updUseFlag" id="updUseFlag">
												<option value="Y"><spring:message code="list.allow" /></option>
												<option value="N"><spring:message code="list.block" /></option>
											</select>
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
								<button class="btn-lg burgundy btn-modal" type="button" name="btnUpd"  id=btnUpd>
									<span><spring:message code="button.save" /></span>
								</button>
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