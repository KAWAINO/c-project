<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<!-- meta tag -->
<%@ include file="/decorators/meta.jsp" %>
<!-- //meta tag -->
<meta charset="UTF-8">
<title>RADIUS</title>

<script type="text/javascript">

$(document).ready(function() { 
	
	// 모달
	$('#addModal').on('hidden.bs.modal', function (e) {
		$('#addInmarsatForm')[0].reset();
	});
	
	// 추가
    $("#btnAdd").bind("click", function() {
    	addInmarsat();
    });
	
    // 수정
    $("#btnUpd").bind("click", function() {
    	updateInmarsat();
    });
	
	// 삭제
    $("#btnDel").bind("click", function() {
    	delInmarsat();
    });
	
	// excel
    $("#btnExcel").bind("click", function() {
    	goExcelDown();
    });
});

//paging
function goPage(pageno) {
	$("#pageno").val(pageno);
	$("#inmarsatForm").attr("action", "/inmarsat/inmarsat.do");
    $("#inmarsatForm").submit();	
}

function addInmarsat(){

	var datas = $("#addInmarsatForm").serialize();
	$.ajax({
		type : "POST",
		url : "/inmarsat/inmarsatAdd.ajax",
		data : "ajax=true&" + datas,
		dataType : "json",
		error	: function (e) { alert("오류가 발생하였습니다. 관리자에게 문의 바랍니다."); },
		success : function(data) {
			if(data.result == "1") {
				alert("정상적으로 추가되었습니다.");
				document.location.reload();
			} else if(data.result == "-2") {
				alert("'Groupname'과 'Attribute' 두 가지 모두를 중복으로 등록할 수 없습니다.")
			} else {
				alert("오류가 발생하였습니다. 관리자에게 문의 바랍니다.");
			}
		}
			
	});
	return;
}

function goUpdate(groupName, attribute){
	$.ajax({
		type : "POST",
		url : "/inmarsat/inmarsatSetUpdateData.ajax",
		data : { 
					'groupName' : groupName,
					'attribute' : attribute 
				},
		dataType : "json",
		error	: function (e) { alert("오류가 발생하였습니다. 관리자에게 문의 바랍니다."); },
		success : function(data) {
			$('#updGroupName').val(data.inmarsatVo.groupName);
			$('#u_updGroupName').val(data.inmarsatVo.groupName);
			$('#updAttribute').val(data.inmarsatVo.attribute);
			$('#u_updAttribute').val(data.inmarsatVo.attribute);
			$('#updOp').val(data.inmarsatVo.op);
			$('#updValue').val(data.inmarsatVo.value);
			
			$("#updateModal").modal("show");
		}
	});
}

function updateInmarsat(){
 	
	var datas = $("#updInmarsatForm").serialize();
	$.ajax({
		type : "POST",
		url : "/inmarsat/inmarsatUpdate.ajax",
		data : "ajax=true&" + datas,
		dataType : "json",
		error	: function (e) { alert("오류가 발생하였습니다. 관리자에게 문의 바랍니다."); },
		success : function(data) {
			if(data.result == "1") {
				alert("정상적으로 수정되었습니다.");
				document.location.reload();
			}else {
				alert("오류가 발생하였습니다. 관리자에게 문의 바랍니다.");
			}
		}
	});
	return;
}

function delInmarsat(){
	var groupName = $('#updGroupName').val();
	var attribute = $('#updAttribute').val();
	
	if(!confirm("해당 알람설정이 삭제됩니다. 삭제를 진행하시겠습니까?")){
		return;
	}
	
	$.ajax({
		type : "POST",
		url : "/inmarsat/inmarsatDelete.ajax",
		data : { 
					'groupName' : groupName,
					'attribute' : attribute 
				},
		dataType : "json",
		error	: function (e) { alert("오류가 발생하였습니다. 관리자에게 문의 바랍니다."); },
		success : function(data) {
			if(data.result == "1") {
				alert("정상적으로 삭제되었습니다.");
				document.location.reload();
			}else {
				alert("오류가 발생하였습니다. 관리자에게 문의 바랍니다.");
			}
		}
	});
}

//엑셀 다운로드
function goExcelDown() {
	$("#updInmarsatForm").attr("action", "/inmarsat/inmarsatExcelDownload.do");
    $("#updInmarsatForm").submit();
}

</script>

</head>
<body>

<!-- header -->
<%@ include file="/decorators/header.jsp"%>
<!-- //header -->

<form name="alarmConfForm" id="alarmConfForm" method="post">
	<input type="hidden" id="pageno" name="pageno" value="${alarmConfVo.pageno }"/>
</form>

<div class="container min-vh-100">
	<ol class="breadcrumb mt-4 py-3 fs-5">
		<li class="breadcrumb-item active">관리</li>
		<li class="breadcrumb-item active">inmarsat 설정 관리</li>
	</ol>
	<div class="container px-0 mt-5">
		<div class="row justify-content-between">
			<div class="col-6 mt-1 scroll-x" style="margin-bottom:-5%;">
				<h5>설정 내역</h5>
			</div>
			<div class="col-3" align="right">
				<button type="button" class="btn btn-primary btn-addel mb-6" data-bs-toggle="modal" data-bs-target="#addModal">추가</button>
				<button type="button" class="btn btn-outline-primary btn-addel mb-6" id="btnExcel">엑셀 다운로드</button>		
			</div>
		</div>
	</div>
	<div class="row mt-1">
		<div class="col-12" >
			<div class="card-header">
				<div class="card-body" >			
					<table class="table table-bordered mt-3" >
				    	<colgroup>
								<col style="width:25%;">
								<col style="width:25%;">									
								<col style="width:10%;;">
								<col style="width:20%;;">
						</colgroup>
						<thead>
							<tr class="table-primary" >
								<th scope="col">Groupname</th>
								<th scope="col">Attribute</th>
								<th scope="col">OP</th>
								<th scope="col">Value</th>
							</tr>
						</thead>
						<tbody>
							<c:choose>
								<c:when test="${fn:length(inmarsatList) > 0}">
									<c:forEach var="item" items="${inmarsatList }" varStatus="idx">
										<tr id="list_${item.id }" onClick="goUpdate('${item.groupName}', '${item.attribute}')">									
											<td>${item.groupName }</td>
											<td>${item.attribute }</td>
											<td>${item.op }</td>
											<td>${item.value }</td>
										</tr>
									</c:forEach>
								</c:when>
								<c:otherwise>
                                	<tr>
                                    	<td colspan="4">
                                        	<div class="my-4">
                                            	데이터가 없습니다.
                                            </div>
                                        </td>
                                    </tr>
                            	</c:otherwise>
							</c:choose>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
<!-- paging -->
${pagingHTML}
<!-- paging -->
</div>

<!-- 추가 Modal -->
<div class="modal fade" id="addModal" data-bs-backdrop="static">
	<div class="modal-dialog modal-lg modal-dialog-centered" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h5>inmarsat 설정 등록</h5>
				<button type="button" class="btn-close" data-bs-dismiss="modal" aria-laberl="Close">
					<span aria-hidden="true"></span>
				</button>
			</div>
			<div class="modal-body px-3">
				<form name="addInmarsatForm" id="addInmarsatForm" method="post">
				
					<div class="row mb-4">
						<div class="col-6">
							<label for="addGroupName" class="col-form-label">Groupname</label> 
							<input type="text" class="form-control" id="addGroupName" name="addGroupName" disabled="disabled" value="inmarsat">
						</div>
						<div class="col-6">
							<label for="addAttribute" class="col-form-label">Attribute</label> 
							<input type="text" class="form-control" id="addAttribute" name="addAttribute">
						</div>
					</div>
					<div class="row mb-4">
						<div class="col-6">
							<label for="addOp" class="col-form-label">OP</label> 
							<input type="text" class="form-control" id="addOp" name="addOp" disabled="disabled" value=":=">
						</div>
						<div class="col-6">
							<label for="addValue" class="col-form-label">Value</label> 
							<input type="text" class="form-control" id="addValue" name="addValue">
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<div class="col" align="left"></div>
				<div class="col" align="right">
					<button type="button" class="btn btn-primary btn-modal" id="btnAdd">추가</button>
					<button type="button" class="btn btn-secondary btn-modal" data-bs-dismiss="modal">닫기</button>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- 추가 Modal 끝 -->

<!-- 수정 Modal 시작 -->
<div class="modal fade" id="updateModal" data-bs-backdrop="static">
	<div class="modal-dialog modal-lg modal-dialog-centered" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h5>inmarsat 설정 수정</h5>
				<button type="button" class="btn-close" data-bs-dismiss="modal" aria-laberl="Close">
					<span aria-hidden="true"></span>
				</button>
			</div>
			<div class="modal-body px-3">
				<form name="updInmarsatForm" id="updInmarsatForm" method="post">
				<input type="hidden" id="updGroupName" name="updGroupName" >
				<input type="hidden" id="updAttribute" name="updAttribute" >
					<div class="row mb-4">
						<div class="col-6">
							<label for="updGroupName" class="col-form-label">Groupname</label> 
							<input type="text" class="form-control" id="u_updGroupName" name="u_updGroupName" disabled="disabled" value="inmarsat">
						</div>
						<div class="col-6">
							<label for="updAttribute" class="col-form-label">Attribute</label> 
							<input type="text" class="form-control" id="u_updAttribute" name="u_updAttribute" disabled="disabled">
						</div>
					</div>
					<div class="row mb-4">
						<div class="col-6">
							<label for="updOp" class="col-form-label">OP</label> 
							<input type="text" class="form-control" id="updOp" name="updOp" disabled="disabled" value=":=">
						</div>
						<div class="col-6">
							<label for="updValue" class="col-form-label">Value</label> 
							<input type="text" class="form-control" id="updValue" name="updValue">
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
		      	<div class="col" align="left">
					<button type="button" class="btn btn-outline-primary btn-modal" id="btnDel">삭제</button>
				</div>
				<div class="col" align="right">
				  <button type="button" class="btn btn-primary btn-modal" id="btnUpd">수정</button>
				  <button type="button" class="btn btn-secondary btn-modal" data-bs-dismiss="modal">닫기</button>
				</div>
			</div>
		</div>
	</div>
</div>

<!-- footer -->
<%@ include file="/decorators/footer.jsp"%>
<!-- //footer -->	

</body>
</html>