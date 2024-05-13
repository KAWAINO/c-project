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

// 로그아웃 후 뒤로가기 방지
/* window.history.forward();
function holding(){
	window.history.forward();
} */

$(document).ready(function(){

	// 모달
	$('#addModal').on('hidden.bs.modal', function (e) {
		$('#addSuserForm')[0].reset();
	});
	
	// 검색
    $("#btnSearch").bind("click", function() {
    	$("#searchSuserForm").attr("action", "/suser/suser.do");
        $("#searchSuserForm").submit();
    });
	
	//초기화
    $("#btnReset").bind("click", function() {
    	$("#searchUserId").val("");
    	$("#searchUserName").val("");
    });
	
 	// 추가
    $("#btnAdd").bind("click", function() {
    	addSuser();
    });
	
	// 수정
    $("#btnUpd").bind("click", function() {
    	updateSuser();
    });
	
	// 삭제
    $("#btnDel").bind("click", function() {
    	delSuser();
    });
	
	// excel
    $("#btnExcel").bind("click", function() {
    	goExcelDown();
    });
});

//paging
function goPage(pageno) {
	$("#pageno").val(pageno);
	$("#searchSuserForm").attr("action", "/suser/suser.do");
    $("#searchSuserForm").submit();	
}

function addSuser(){
	//아이디
    if ('' == $("#addUserId").val()) {
        alert("아이디가 입력되지 않았습니다. 확인 후 등록해주세요.");
        $("#addUserId").focus();
        return;
    }
    
	//비밀번호
    if ('' == $("#addPassWd").val()) {
        alert("비밀번호가 입력되지 않았습니다. 확인 후 등록해주세요.");
        $("#addPassWd").focus();
        return;
    }
    
    if ('' == $("#addPassWdChk").val()) {
        alert("비밀번호 확인이 입력되지 않았습니다. 확인 후 등록해주세요.");
        $("#addPassWdChk").focus();
        return;
    }
    
    if($("#addPassWd").val() != $("#addPassWdChk").val()){
    	alert("비밀번호를 정확히 입력 후 등록해주세요.");
    	$("#addPassWd").focus();
    	return;
    }
    
    if(!chk_pwd($("#addUserId").val(), $("#addPassWd").val())){
    	$("#addPassWd").focus();
		return;
	}
    
	//이름
    if ('' == $("#addUserName").val()) {
        alert("이름이 입력되지 않았습니다. 확인 후 등록해주세요.");
        $("#addUserName").focus();
        return;
    }
	
    var datas = $("#addSuserForm").serialize();
	$.ajax({
		type : "POST",
		url : "/suser/suserAdd.ajax",
		data : "ajax=true&" + datas,
		dataType : "json",
		error	: function (e) { alert("오류가 발생하였습니다. 관리자에게 문의 바랍니다."); },
		success : function(data) {
			if(data.result == "1") {
				alert("정상적으로 추가되었습니다.");
				document.location.reload();
			} else if(data.result == "-2") {
				alert("해당 ID가 존재합니다. 다시 입력하여주세요.");
			}else {
				alert("오류가 발생하였습니다. 관리자에게 문의 바랍니다.");
			}
		}
	});
	return;
}

function goUpdate(userId){
	$.ajax({
		type : "POST",
		url : "/suser/suserSetUpdateData.ajax",
		data : "ajax=true&userId=" + userId,
		dataType : "json",
		error	: function (e) { alert("오류가 발생하였습니다. 관리자에게 문의 바랍니다."); },
		success : function(data) {
			$('#updUserId').val(data.suserVo.userId);
			$('#u_updUserId').val(data.suserVo.userId);
			$('#updUserName').val(data.suserVo.userName);
			$('#updDescr').val(data.suserVo.descr);
			
			$("#updateModal").modal("show");
		}
	});
}

/*패스워드 변경 여부*/
function pwdflag(){
	var flag=$('#cngPwd').is(':checked');
	
	if(false == flag){
		$("#updPassWd").attr("disabled", true);
		$("#updPassWdChk").attr("disabled", true);
	}else{
		$("#updPassWd").attr("disabled", false);
		$("#updPassWdChk").attr("disabled", false);
	}
}

function updateSuser(){
	
	var flag=$('#cngPwd').is(':checked');
	if(true == flag){
	    if ('' == $("#updPassWd").val()) {
	        alert("비밀번호가 입력되지 않았습니다. 확인 후 등록해주세요.");
	        $("#updPassWd").focus();
	        return;
	    }
	    
	    if ('' == $("#updPassWdChk").val()) {
	        alert("비밀번호 확인이 입력되지 않았습니다. 확인 후 등록해주세요.");
	        $("#updPassWdChk").focus();
	        return;
	    }
	    
	    if($("#updPassWd").val() != $("#updPassWdChk").val()){
	    	alert("비밀번호를 정확히 입력 후 등록해주세요.");
	    	$("#updPassWd").focus();
	    	return;
	    }
	    
	    if(!chk_pwd($("#updUserId").val(), $("#updPassWdChk").val())){
	    	$("#updPassWd").focus();
			return;
		}
	}
    
	//이름
    if ('' == $("#updUserName").val()) {
        alert("이름이 입력되지 않았습니다. 확인 후 등록해주세요.");
        $("#updUserName").focus();
        return;
    }
    
	var datas = $("#updSuserForm").serialize();
	$.ajax({
		type : "POST",
		url : "/suser/suserUpdate.ajax",
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

function delSuser(){
	var userId = $('#updUserId').val();
	
	if(!confirm(userId + "(이)가 삭제됩니다. \n해당 운영자를 정말 삭제 하시겠습니까?")){
		return;
	}
	
	$.ajax({
		type : "POST",
		url : "/suser/suserDelete.ajax",
		data : "ajax=true&userId=" + userId,
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
	$("#searchSuserForm").attr("action", "/suser/suserExcelDownload.do");
    $("#searchSuserForm").submit();
}

///////////////////////////
//패스워드 유효성
function chk_pwd(id, pwd){
	 var userID = id;
	 var password = pwd;
	  
	 //특수문자 유효여부
	 if( password.search(/[\'\\\\\-()<>]/g) != -1)
	 {
		alert("비밀번호에는 '\\-()<>를 제외한 특수문자만 포함될 수 있습니다."); 
		return false; 
	 }
	 
	 // 길이
	 if(!/^[a-zA-Z0-9!@#$%^&*.?_~]{8,20}$/.test(password))
	 { 
		alert("비밀번호는 숫자, 영문, 특수문자 조합으로 8~20자리를 사용해야 합니다."); 
		return false;
	 }
	 
	 // 영문, 숫자, 특수문자 2종 이상 혼용
	 var chk = 0;
	 if(password.search(/[0-9]/g) != -1 ) chk ++;
	 if(password.search(/[a-z]/ig)  != -1 ) chk ++;
	 if(password.search(/[!@#$%^&*?_~.]/g)  != -1  ) chk ++;
	 if(chk < 3)
	 { 
		alert("비밀번호는 숫자, 영문, 특수문자를 혼용하여야 합니다."); 
		return false;
	 }
	  
	 // 동일한 문자/숫자 4이상, 연속된 문자
	 if(/(\w)\1\1\1/.test(password) || isContinuedValue(password))
	 {
		alert("비밀번호에 4자 이상의 연속 또는 반복 문자 및 숫자를 사용하실 수 없습니다.");
	  	return false;
	 }
	  
	 // 아이디 포함 여부
	 if(password.search(userID)>-1)
	 {
		alert("ID가 포함된 비밀번호는 사용하실 수 없습니다."); 
	  	return false;
	 }
	 
	 return true;
}

function isContinuedValue(value) {
	 var intCnt1 = 0;
	 var intCnt2 = 0;
	 var temp0 = "";
	 var temp1 = "";
	 var temp2 = "";
	 var temp3 = "";
	 for (var i = 0; i < value.length-3; i++) {
	  temp0 = value.charAt(i);
	  temp1 = value.charAt(i + 1);
	  temp2 = value.charAt(i + 2);
	  temp3 = value.charAt(i + 3);

	  if (temp0.charCodeAt(0) - temp1.charCodeAt(0) == 1
	   && temp1.charCodeAt(0) - temp2.charCodeAt(0) == 1
	   && temp2.charCodeAt(0) - temp3.charCodeAt(0) == 1) {
	  intCnt1 = intCnt1 + 1;
	  }
	  if (temp0.charCodeAt(0) - temp1.charCodeAt(0) == -1
	   && temp1.charCodeAt(0) - temp2.charCodeAt(0) == -1
	   && temp2.charCodeAt(0) - temp3.charCodeAt(0) == -1) {
	  intCnt2 = intCnt2 + 1;
	  }
	 }

	 return (intCnt1 > 0 || intCnt2 > 0);
}

</script>
</head>
<body>

<!-- header -->
<%@ include file="/decorators/header.jsp"%>
<!-- //header -->
	
<div class="container min-vh-100">
	<ol class="breadcrumb mt-4 py-3 fs-5">
		<li class="breadcrumb-item active">관리</li>
		<li class="breadcrumb-item active">운영자 관리</li>
	</ol>
	<!-- 검색 -->
	<div class="card border-primary mb-4">
		<div class="card-header">상세검색</div>
		<div class="card-body">
			<form name="searchSuserForm" id="searchSuserForm" method="post">
				<input type="hidden" id="pageno" name="pageno" value="${suserVo.pageno }">
				<div class="row mt-4">
					<label class="col-search-label col-form-label me-7" for="searchUserId">운영자 ID</label>
					<div class="col-3 me-6">
						<input type="text" class="form-control " style=margin-left:-1.0rem id="searchUserId"
							name="searchUserId" value="${suserVo.searchUserId}">
					</div>
					<label class="col-search-label col-form-label me-8" for="searchUserName">운영자 이름</label>
					<div class="col-3 ">
						<input type="text" class="form-control" style=margin-left:-1.0rem id="searchUserName"
							name="searchUserName" value="${suserVo.searchUserName}">
					</div>
					<div class="row mt-5 mb-6 card-footer" style=margin-left:0.05rem></div>
					<div class="search-footer" align="center">
						<button type="button" class="btn btn-primary btn-search mb-2 " id="btnSearch">조회</button>
						<button type="button" class="btn btn-outline-primary btn-search mb-2 " id="btnReset">초기화</button>
					</div>
				</div>
			</form>
		</div>
	</div>
	<div class="container px-0">
		<div class="row justify-content-between">
			<div class="col-3 mb-6 mt-1">
				<h5>조회 결과</h5>
			</div>
			<div class="col-3" align="right">
				<button type="button" class="btn btn-outline-primary btn-addel mb-6"
					id="btnExcel">엑셀 다운로드</button>
				<button type="button" class="btn btn-primary btn-addel mb-6"
					data-bs-toggle="modal" data-bs-target="#addModal">추가</button>
			</div>
		</div>
	</div>
	<div class="row">
		<div class="col-12" >
			<div class="card-header">
				<div class="card-body">
					<table class="table table-bordered mt-3">
						<thead>
							<tr class="table-primary">
						      <th scope="col">운영자 ID</th>
						      <th scope="col">운영자 이름</th>
						      <th scope="col">설명</th>
						    </tr>
						</thead>
						<tbody>
					  	<c:choose>
					  		<c:when test="${fn:length(suserList) > 0 }">
					  			<c:forEach var="item" items="${suserList }" varStatus="idx">
					  				<tr id="list_${item.userId }" onClick="goUpdate('${item.userId}')">
					  					<td>${item.userId }</td>
					  					<td>${item.userName }</td>
					  					<td>${item.descr }</td>
					  				</tr>
					  			</c:forEach>
					  		</c:when>
					  		<c:otherwise>
								<tr>
							    	<td colspan="3">
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
				<h5>운영자 등록</h5>
				<button type="button" class="btn-close" data-bs-dismiss="modal" aria-laberl="Close">
					<span aria-hidden="true"></span>
				</button>
			</div>
			<div class="modal-body px-3">
				<form name="addSuserForm" id="addSuserForm" method="post">
					<div class="row mb-4">
						<div class="col-6">
							<label for="addUserId" class="col-form-label">운영자 ID *</label> 
							<input type="text" class="form-control" id="addUserId" name="addUserId">
						</div>
						<div class="col-6">
							<label for="addUserName" class="col-form-label">운영자 이름 *</label> 
							<input type="text" class="form-control" id="addUserName" name="addUserName">
						</div>
					</div>
					<div class="row mb-2">
						<div class="col-6 mb-3">
							<label for="addpPassWd" class="col-form-label">비밀번호 *</label> 
							<input type="password" class="form-control" id="addPassWd" name="addPassWd">
						</div>
						<div class="col-6 mb-3">
							<label for="addPassWdChk" class="col-form-label">비밀번호 확인 *</label> 
							<input type="password" class="form-control" id="addPassWdChk" name="addPassWdChk">
						</div>
					</div>
					<div class="row mb-2">
						<div class="col-12 mb-3">
							<label for="addDescr" class="col-form-label">설명</label> 
							<input type="text" class="form-control" id="addDescr" name="addDescr">
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

<!-- 수정 Modal -->
<div class="modal fade" id="updateModal" data-bs-backdrop="static">
	<div class="modal-dialog modal-lg modal-dialog-centered" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h5>운영자 수정</h5>
				<button type="button" class="btn-close" data-bs-dismiss="modal" aria-laberl="Close">
					<span aria-hidden="true"></span>
				</button>
			</div>
			<div class="modal-body px-3">
				<form name="updSuserForm" id="updSuserForm" method="post">
				<input type="hidden" id="updUserId" name="updUserId" >
					<div class="row mb-4">
						<div class="col-6">
							<label for="updUserId" class="col-form-label">운영자 ID *</label> 
							<input type="text" class="form-control" id="u_updUserId" name="u_updUserId" disabled="disabled">
						</div>
						<div class="col-6">
							<label for="updUserName" class="col-form-label">운영자 이름 *</label> 
							<input type="text" class="form-control" id="updUserName" name="updUserName">
						</div>
					</div>
					<div class="row mb-2">
						<div class="col-6 mb-3">
							<label for="updPassWd" class="col-form-label">비밀번호 *</label> 
							<input type="password" class="form-control" id="updPassWd" name="updPassWd" disabled="disabled">
							<input type="checkbox" class="form-check-input" name="cngPwd" id="cngPwd" value="cngPwd" onClick="pwdflag();">
							<label for="cngPwd">비밀번호 변경</label>
						</div>
						<div class="col-6 mb-3">
							<label for="updPassWdChk" class="col-form-label">비밀번호 확인 *</label> 
							<input type="password" class="form-control" id="updPassWdChk" name="updPassWdChk" disabled="disabled">
						</div>
					</div>
					<div class="row mb-2">
						<div class="col-12 mb-3">
							<label for="updDescr" class="col-form-label">설명</label> 
							<input type="text" class="form-control" id="updDescr" name="updDescr">
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
<!-- 수정 Modal 끝 -->

<!-- footer -->
<%@ include file="/decorators/footer.jsp" %>
<!-- //footer -->
</body>
</html>