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
		$('#addNoAuthForm')[0].reset();
	});

	// 검색
    $("#btnSearch").bind("click", function() {
    	$("#pageno").val(1);
    	$("#searchNoAuthForm").attr("action", "/noauth/noauth.do");
        $("#searchNoAuthForm").submit();
    });
	
 	// 추가
    $("#btnAdd").bind("click", function() {
    	addNoAuth();
    });
 	
 	// 수정
    $("#btnUpd").bind("click", function() {
    	updateNoAuth();
    });
 	
 	// 삭제
    $("#btnDel").bind("click", function() {
    	delNoAuth();
    });
 	
	// excel
    $("#btnExcel").bind("click", function() {
    	goExcelDown();
    });
	
 	// 추가모달 선주사에 따른 선박명 set
    $('#addCompId').on('change', function() {
        updateShipList('#addCompId', '#addShipName');
    });
});

//paging
function goPage(pageno) {
	$("#pageno").val(pageno);
	$("#searchNoAuthForm").attr("action", "/noauth/noauth.do");
    $("#searchNoAuthForm").submit();	
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
                var result = "<option value=''>" + select + "</option>";

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

function addNoAuth(){	
	
	//MAC 주소
    if ('' == $("#addMac").val()) {
        alert('<spring:message code="confirm.enter.macAddress" />');
        $("#addMac").focus();
        return;
    }
	
	//단말기명
    if ('' == $("#addTermName").val()) {
        alert('<spring:message code="confirm.enter.deviceName" />');
        $("#addTermName").focus();
        return;
    }
	
    //선박
	if($("[name=addShipName] option:selected").val() == "select"){
		alert('<spring:message code="confirm.select.ship" />');
        $("#addShipName").focus();
		return;
	}
    
	//MAC 주소
	if ($("#addMac").val().length !== 12) {
	    alert('<spring:message code="confirm.wrong.macAddressLength" />');
	    $("#addMac").focus();
	    return;
    }
	
	var datas = $("#addNoAuthForm").serialize();
	$.ajax({
		type : "POST",
		url : "/noauth/noAuthAdd.ajax",
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
				alert('<spring:message code="confirm.exists.deviceName" />')
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
		url : "/noauth/noAuthUpdateData.ajax",
		data : {
				'mac' : mac,
				's_code' : s_code
			   },
		dataType : "json",
		error	: function (e) { alert(error); },
		success : function(data) {
			
			$('#u_updMac').val(data.noAuthVo.mac);
			$('#updMac').val(data.noAuthVo.mac);
			$('#updTermName').val(data.noAuthVo.term_name);
			$('#updCompId').val(data.noAuthVo.comp_name);
			$('#updShipName').val(data.noAuthVo.s_name);
			$('#updDescr').val(data.noAuthVo.descr);
			$('#updScode').val(data.noAuthVo.s_code);
			
			$("#updateModal").modal("show");
		}
	});
}

function updateNoAuth(mac, s_code){
	
	var datas = $("#updNoAuthForm").serialize();
	$.ajax({
		type : "POST",
		url : "/noauth/noAuthUpdate.ajax",
		data : "ajax=true&" + datas,
		dataType : "json",
		error	: function (e) { alert(error); },
		success : function(data) {
			if(data.result == "1") {
				alert(success);
				document.location.reload();
			} else if(data.result == "-3"){
				alert('<spring:message code="confirm.exists.deviceName" />')
			} else {
				alert(error);
			}
		}
	});
	return;
}

function delNoAuth(){
	var mac = $('#updMac').val();
	var s_code = $('#updScode').val();
	
	if(!confirm(mac + " " + '<spring:message code="confirm.delete.mac" />')){
		return;
	}
	
	$.ajax({
		type : "POST",
		url : "/noauth/noAuthDelete.ajax",
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

//엑셀 다운로드
function goExcelDown() {
	
	var msg = "";
	
	msg = prompt(downloadExcelMessage);

	if(!msg) {
		alert(downloadCanceledMessage);
		return;
	}
	
	$("input[name ='excelMsg']").val(msg);
	
	$("#searchNoAuthForm").attr("action", "/noauth/noAuthExcelDownload.do");
    $("#searchNoAuthForm").submit();
}

</script>
</head>
<body>

	<jsp:include page="../header.jsp" flush="false"/>
	
	<div class="contents-area">
		<header>
			<i class="icon-list"><span>icon</span></i>
			<h2><spring:message code="noauth.noAuthRequiredMAC" /></h2>
		</header>
		<form name="searchNoAuthForm" id="searchNoAuthForm" method="post">
			<input type="hidden" id="pageno" name="pageno" value="${noAuthVo.pageno }"/>
			<input type="hidden" id="excelMsg" name="excelMsg" value="">
			<div class="search-area">
				<select id="searchCompId" name="searchCompId">
					<option value=""><spring:message code="select.shipOwner" /></option>
					<c:forEach var="comp" items="${compList }">
						<option value="${comp.comp_id}" <c:if test="${comp.comp_id eq noAuthVo.searchCompId }">selected</c:if> >
							<c:out value="${comp.comp_name}" />
						</option>
					</c:forEach>
				</select>
				<input type="text" id="searchShipName" name="searchShipName" class="w150" maxlength="" 
					value="${noAuthVo.searchShipName }" placeholder="<spring:message code="search.enterShipName" />">
				<input type="text" id="searchTermName" name="searchTermName" class="w150" maxlength="" 
					value="${noAuthVo.searchTermName }" placeholder="<spring:message code="search.enterDeviceName" />">
				<button class="btn-md white" type="button" id="btnSearch"><span><spring:message code="button.search" /></span></button>
			</div>
			<div class="board-area">
				<table class="tbl-default bg-on">
					<colgroup>
						<col style="width:15%;">
						<col style="width:15%;">
						<col style="width:12%;">
						<col style="width:15%;">
						<col style="">
					</colgroup>
					<thead>
						<tr>
							<th scope="col"><spring:message code="select.shipOwner" /></th>
							<th scope="col"><spring:message code="select.shipName" /></th>
							<th scope="col"><spring:message code="list.deviceName" /></th>
							<th scope="col">MAC</th>
							<th scope="col"><spring:message code="list.description" /></th>
						</tr>
					</thead>
					<tbody>
						<c:choose>
							<c:when test="${fn:length(authList) > 0 }">
								<c:forEach var="item" items="${authList }" varStatus="idx" >
									<tr id="list_${item.mac }" onClick="goUpdate('${item.mac}', '${item.s_code}')" >
										<td>${item.comp_name }</td>
										<td>${item.s_name }</td>
										<td>${item.term_name }</td>
										<td>${item.mac }</td>
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
						<h2><spring:message code="noauth.noAuthRequiredMAC" /></h2>
						<div class="cancelArea" data-bs-dismiss="modal">
							<img class="cancelX" src="../web/images/common/btn-layer-close.png">
						</div>
					</header>
					<form name="addNoAuthForm" id="addNoAuthForm" method="post">
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
										<th><label for="addMac">MAC<span class="key">*</span></label></th>
										<td>
											<input type="text" class="width100" name="addMac" id="addMac" maxlength=12 />
										</td>
										<th>
											<label for="addTermName">
												<spring:message code="list.deviceName" />
												<span class="key">*</span>
											</label>
										</th>
										<td>
											<input type="text" class="width100" name="addTermName" id="addTermName" maxlength=12 />
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
												<option value="select"><spring:message code="select.select" /></option>
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
												<option value="select"><spring:message code="select.select" /></option>
												<c:forEach var="ship" items="${shipList }">
													<option value="${ship.s_code }">
														<c:out value="${ship.s_name }" />
													</option>
												</c:forEach>
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
						<h2><spring:message code="noauth.noAuthRequiredMAC" /></h2>
						<div class="cancelArea" data-bs-dismiss="modal">
							<img class="cancelX" src="../web/images/common/btn-layer-close.png">
						</div>
					</header>
					<form name="updNoAuthForm" id="updNoAuthForm" method="post">
						<input type="hidden" id="u_updMac" name="u_updMac">
						<input type="hidden" id="updScode" name="updScode">
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
										<th><label for="updMac">MAC<span class="key">*</span></label></th>
										<td>
											<input class="width100" name="updMac" id="updMac" maxlength=12 disabled/>
										</td>
										<th>
											<label for="updTermName">
												<spring:message code="list.deviceName" />
												<span class="key">*</span>
											</label>
										</th>
										<td>
											<input type="text" class="width100" name="updTermName" id="updTermName" maxlength=12 />
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
											<input id="updCompId" name="updCompId" disabled>
										</td>
										<th>
											<label for="updShipName">
												<spring:message code="select.shipName" />
												<span class="key">*</span>
											</label>
										</th>
										<td>
											<input id="updShipName" name="updShipName" disabled>
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