<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<!-- meta tag -->
<%@ include file="/decorators/meta.jsp" %>
<!-- //meta tag -->
<meta charset="UTF-8">
<title>APMS</title>
<script type="text/javascript">
$(document).ready(function(){
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
    	$("#searchGrade").val("");
    	$("#searchUserId").val("");
    	$("#searchUserName").val("");
    });
	
	// 추가
    $("#btnAdd").bind("click", function() {
    	addUser();
    });
	
	// 수정
    $("#btnUpd").bind("click", function() {
    	updateUser();
    });
	
	// 삭제
    $("#btnDel").bind("click", function() {
    	delUser();
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

function addUser(){
	//아이디
    if ('' == $("#addUserId").val()) {
        alert("아이디가 입력되지 않았습니다. 확인 후 등록해주세요.");
        $("#addUserId").focus();
        return;
    }
    
	//비밀번호
    if ('' == $("#addUserPwd").val()) {
        alert("비밀번호가 입력되지 않았습니다. 확인 후 등록해주세요.");
        $("#addUserPwd").focus();
        return;
    }
    
    if ('' == $("#addUserPwdChk").val()) {
        alert("비밀번호 확인이 입력되지 않았습니다. 확인 후 등록해주세요.");
        $("#addUserPwdChk").focus();
        return;
    }
    
    if($("#addUserPwd").val() != $("#addUserPwdChk").val()){
    	alert("비밀번호를 정확히 입력 후 등록해주세요.");
    	$("#addUserPwd").focus();
    	return;
    }
    
    if(!chk_pwd($("#addUserId").val(), $("#addUserPwd").val())){
    	$("#addUserPwd").focus();
		return;
	}
    
	//이름
    if ('' == $("#addUserName").val()) {
        alert("이름이 입력되지 않았습니다. 확인 후 등록해주세요.");
        $("#addUserName").focus();
        return;
    }
	
    //등급
	if($("[name=addUserGrade] option:selected").val() == ""){
		alert("운영자 등급을 선택하여주세요.");
        $("#addUserGrade").focus();
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
				alert("해당  ID가 존재합니다. 다시 입력하여주세요.");
			}else {
				alert("오류가 발생하였습니다. 관리자에게 문의 바랍니다.");
			}
		}
	});
	return;
}

function goUpdate(user_id){
	$.ajax({
		type : "POST",
		url : "/suser/suserSetUpdateData.ajax",
		data : "ajax=true&user_id=" + user_id,
		dataType : "json",
		error	: function (e) { alert("오류가 발생하였습니다. 관리자에게 문의 바랍니다."); },
		success : function(data) {
			$('#updUserId').val(data.suserVo.user_id);
			$('#u_updUserId').val(data.suserVo.user_id);
			$('#updUserName').val(data.suserVo.user_name);
			$('#updUserGrade').val(data.suserVo.user_grade);
			
			$("#updateModal").modal("show");
		}
	});
}

/*패스워드 변경 여부*/
function pwdflag(){
	var flag=$('#cngPwd').is(':checked');
	
	if(false == flag){
		$("#updUserPwd").attr("disabled", true);
		$("#updUserPwdChk").attr("disabled", true);
	}else{
		$("#updUserPwd").attr("disabled", false);
		$("#updUserPwdChk").attr("disabled", false);
	}
}

function updateUser(){
	//비밀번호
	var flag=$('#cngPwd').is(':checked');
	if(true == flag){
	    if ('' == $("#updUserPwd").val()) {
	        alert("비밀번호가 입력되지 않았습니다. 확인 후 등록해주세요.");
	        $("#updUserPwd").focus();
	        return;
	    }
	    
	    if ('' == $("#updUserPwdChk").val()) {
	        alert("비밀번호 확인이 입력되지 않았습니다. 확인 후 등록해주세요.");
	        $("#updUserPwdChk").focus();
	        return;
	    }
	    
	    if($("#updUserPwd").val() != $("#updUserPwdChk").val()){
	    	alert("비밀번호를 정확히 입력 후 등록해주세요.");
	    	$("#updUserPwd").focus();
	    	return;
	    }
	    
	    if(!chk_pwd($("#updUserId").val(), $("#updUserPwd").val())){
	    	$("#updUserPwd").focus();
			return;
		}
	}
    
	//이름
    if ('' == $("#updUserName").val()) {
        alert("이름이 입력되지 않았습니다. 확인 후 등록해주세요.");
        $("#updUserName").focus();
        return;
    }
	
    //등급
	if($("[name=updUserGrade] option:selected").val() == ""){
		alert("운영자 등급을 선택하여주세요.");
        $("#updUserGrade").focus();
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

function delUser(){
	var user_id = $('#updUserId').val();
	
	if(!confirm(user_id + "(이)가 삭제됩니다. \n해당 운영자를 정말 삭제 하시겠습니까?")){
		return;
	}
	
	$.ajax({
		type : "POST",
		url : "/suser/suserDelete.ajax",
		data : "ajax=true&user_id=" + user_id,
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
<%@ include file="/decorators/header.jsp" %>
<!-- //header -->
<div class="container min-vh-100">
	<ol class="breadcrumb mt-4 py-3 fs-5">
	  <li class="breadcrumb-item active">Setting</li>
	  <li class="breadcrumb-item active">OWM 운영자 관리</li>
	</ol>
	<div class="card border-primary mb-4">
	  <div class="card-header">상세검색</div>
	  <div class="card-body">
	  	<form name="searchSuserForm" id="searchSuserForm" method="post">
	  		<input type="hidden" id="pageno" name="pageno" value="${suserVo.pageno }">
	  		<div class="row">
	  			<label class="col-search-label col-form-label" for="searchGrade">등급</label>
		    	<div class="col-2">
					<select class="form-select" id="searchGrade" name="searchGrade">
						<option value="">전체</option>
						<c:forEach var="gradeListCd" items="${gradeList }">
							<option value="${gradeListCd.grade }" <c:if test="${gradeListCd.grade eq suserVo.searchGrade }"> selected</c:if>>
								<c:out value="${gradeListCd.gname }" />
							</option>
						</c:forEach>
					</select>
	  			</div>
	  		</div>
	  		<div class="row mt-3">
	  			<label class="col-search-label col-form-label" for="searchUserId">운영자 ID</label>
		    	<div class="col-2">
	  				<input type="text" class="form-control" id="searchUserId" name="searchUserId" value="${suserVo.searchUserId }">
	  			</div>
	  			<label class="col-search-label col-form-label" for="searchUserName">운영자 이름</label>
		    	<div class="col-2">
	  				<input type="text" class="form-control" id="searchUserName" name="searchUserName" value="${suserVo.searchUserName }">
	  			</div>
	  		</div>
	  		<div class="row mt-4 card-footer">
	    	<div class="search-footer" align="center">
	    		<button type="button" class="btn btn-primary btn-search" id="btnSearch">조회</button>
	    		<button type="button" class="btn btn-outline-primary btn-search" id="btnReset">초기화</button>
	    	</div>
	    </div>
	  	</form>
	  </div>
	</div>
	<div class="container px-0">
		<div class="row justify-content-between">
			<div class="col-3">
				<h5>조회 결과</h5>
			</div>
			<div class="col-3" align="right">
				<button type="button" class="btn btn-outline-primary btn-addel" id="btnExcel">엑셀 다운로드</button>
				<button type="button" class="btn btn-primary btn-addel" data-bs-toggle="modal" data-bs-target="#addModal">추가</button>
			</div>
		</div>
	</div>
	<table class="table table-hover table-bordered text-center mt-2">
		 <thead>
		    <tr class="table-primary">
		      <th scope="col">운영자 ID</th>
		      <th scope="col">이름</th>
		      <th scope="col">등급</th>
		    </tr>
		  </thead>
		  <tbody>
		  	<c:choose>
		  		<c:when test="${fn:length(suserList) > 0 }">
		  			<c:forEach var="item" items="${suserList }" varStatus="idx">
		  				<tr id="list_${item.user_id }" onClick="goUpdate('${item.user_id}')">
		  					<td>${item.user_id }</td>
		  					<td>${item.user_name }</td>
		  					<td>${item.gname }</td>
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
<!-- paging -->
${pagingHTML}
<!-- paging -->
</div>
<!-- 추가 Modal -->
<div class="modal fade" id="addModal" data-bs-backdrop="static">
	<div class="modal-dialog modal-lg modal-dialog-centered" role="document">
	    <div class="modal-content">
			<div class="modal-header">
		    	<h5 class="modal-title">운영자 추가</h5>
		        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close">
		          <span aria-hidden="true"></span>
		        </button>
			</div>
			<div class="modal-body px-3">
				<form name="addSuserForm" id="addSuserForm" method="post">
					<div class="row">
						<div class="col-12 mb-3">
				            <label for="addUserId" class="col-form-label">운영자 ID *</label>
				            <input type="text" class="form-control" id="addUserId" name="addUserId"">
				        </div>
					</div>
			      	<div class="row">
				      	<div class="col-6 mb-3">
				            <label for="addUserPwd" class="col-form-label">비밀번호 *</label>
				            <input type="password" class="form-control" id="addUserPwd" name="addUserPwd">
				        </div>
				      	<div class="col-6 mb-3">
				            <label for="addUserPwdChk" class="col-form-label">비밀번호 확인 *</label>
				            <input type="password" class="form-control" id="addUserPwdChk" name="addUserPwdChk">
				        </div>
			        </div>
			      	<div class="row">
				      	<div class="col-6 mb-3">
				            <label for="addUserName" class="col-form-label">이름 *</label>
				            <input type="text" class="form-control" id="addUserName" name="addUserName">
				        </div>
				      	<div class="col-6 mb-3">
				            <label for="addUserGrade" class="col-form-label">등급 *</label>
				            <select class="form-select" id="addUserGrade" name="addUserGrade">
								<c:forEach var="gradeListCd" items="${gradeList }">
									<option value="${gradeListCd.grade }">
										<c:out value="${gradeListCd.gname }" />
									</option>
								</c:forEach>
							</select>
				        </div>
			        </div>
				</form>
		 	</div>
	      	<div class="modal-footer">
	      		<div class="col" align="left">
				</div>
				<div class="col" align="right">
				  <button type="button" class="btn btn-primary btn-modal" id="btnAdd">추가</button>
				  <button type="button" class="btn btn-secondary btn-modal" data-bs-dismiss="modal">닫기</button>
				</div>
	      	</div>
	    </div>
    </div>
</div>
<!-- //추가 Modal -->
<!-- 수정 Modal -->
<div class="modal fade" id="updateModal" data-bs-backdrop="static">
	<div class="modal-dialog modal-lg modal-dialog-centered" role="document">
	    <div class="modal-content">
			<div class="modal-header">
		    	<h5 class="modal-title">운영자 수정</h5>
		        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close">
		          <span aria-hidden="true"></span>
		        </button>
			</div>
			<div class="modal-body px-3">
				<form name="updSuserForm" id="updSuserForm" method="post">
				<input type="hidden" id="updUserId" name="updUserId">
					<div class="row">
						<div class="col-12 mb-3">
				            <label for="updUserId" class="col-form-label">운영자 ID *</label>
				            <input type="text" class="form-control" id="u_updUserId" name="u_updUserId" disabled="disabled">
				        </div>
					</div>
			      	<div class="row">
				      	<div class="col-6 mb-3">
				            <label for="updUserPwd" class="col-form-label">비밀번호 *</label>
				            <input type="password" class="form-control" id="updUserPwd" name="updUserPwd" disabled="disabled">
				            <input type="checkbox" class="form-check-input" name="cngPwd" id="cngPwd" value="cngPwd" onClick="pwdflag();">
				            <label for="cngPwd">비밀번호 변경</label>
				        </div>
				      	<div class="col-6 mb-3">
				            <label for="updUserPwdChk" class="col-form-label">비밀번호 확인 *</label>
				            <input type="password" class="form-control" id="updUserPwdChk" name="updUserPwdChk" disabled="disabled">
				        </div>
			        </div>
			      	<div class="row">
				      	<div class="col-6 mb-3">
				            <label for="updUserName" class="col-form-label">이름 *</label>
				            <input type="text" class="form-control" id="updUserName" name="updUserName">
				        </div>
				      	<div class="col-6 mb-3">
				            <label for="updUserGrade" class="col-form-label">등급 *</label>
				            <select class="form-select" id="updUserGrade" name="updUserGrade">
								<c:forEach var="gradeListCd" items="${gradeList }">
									<option value="${gradeListCd.grade }">
										<c:out value="${gradeListCd.gname }" />
									</option>
								</c:forEach>
							</select>
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
<!-- //수정 Modal -->
<!-- footer -->
<%@ include file="/decorators/footer.jsp" %>
<!-- //footer -->
</body>
</html>