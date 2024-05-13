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

$(document).ready(function(){

	// 모달
	$('#addModal').on('hidden.bs.modal', function (e) {
		$('#addCuserForm')[0].reset();
	});

	// 검색
    $("#btnSearch").bind("click", function() {
    	$('#pageno').val(1);
    	$("#searchCuserForm").attr("action", "/cuser/cuser.do");
        $("#searchCuserForm").submit();
    });
	
 	// 추가
    $("#btnAdd").bind("click", function() {
    	addCuser();
    });
 	
 	// 수정
    $("#btnUpd").bind("click", function() {
    	updateCuser();
    });
 	
 	// 삭제
    $("#btnDel").bind("click", function() {
    	delCuser();
    });
 	
 	// 잠금 헤제
	$("#unLock").bind("click", function() {
    	cuserUnLock();
    });
	
	// excel
    $("#btnExcel").bind("click", function() {
    	
    	var msg = "";
    	
    	msg = prompt('<spring:message code="confirm.downloadExcel" />');

    	if(!msg) {
    		alert('<spring:message code="confirm.downloadCanceled" />');
    		return;
    	}   	
    	$("input[name ='excelMsg']").val(msg);
    	
   		goExcelDown();
    });
});

let success = '<spring:message code="confirm.success" />';
let error = '<spring:message code="confirm.error" />';
let erase = '<spring:message code="confirm.delete" />';
let IDexists = '<spring:message code="cuser.confirm.IDexists" />';

//paging
function goPage(pageno) {
	$("#pageno").val(pageno);
	$("#searchCuserForm").attr("action", "/cuser/cuser.do");
    $("#searchCuserForm").submit();	
}


function addCuser(){	
	
	
	//아이디
    if ('' == $("#addUserId").val()) {
        alert('<spring:message code="cuser.confirm.enterID" />');
        $("#addUserId").focus();
        return ;
    }
    
	//비밀번호
    if ('' == $("#addPassWd").val()) {
        alert('<spring:message code="cuser.confirm.enterPassword" />');
        $("#addPassWd").focus();
        return;
    }
    
    if ('' == $("#chkAddPassWd").val()) {
        alert('<spring:message code="cuser.confirm.confirmPassword" />');
        $("#chkAddPassWd").focus();
        return;
    }
    
    if($("#addPassWd").val() != $("#chkAddPassWd").val()){
    	alert('<spring:message code="cuser.confirm.differentPassword" />');
    	$("#addPassWd").focus();
    	return;
    }
    
    if(!chk_pwd($("#addUserId").val(), $("#addPassWd").val())){
    	$("#addPassWd").focus();
		return;
	}
    
	//이름
    if ('' == $("#addUserName").val()) {
        alert('<spring:message code="cuser.confirm.enterName" />');
        $("#addUserName").focus();
        return;
    }
	
	let id = $("#addUserId").val();
    if( id.search(/[\'\\\\\-()<>]/g) != -1) {
		alert("<spring:message code='cuser.confirm.IDsCanOnly' />"); 
		$("#addUserId").focus();
		return; 
	}
    
    var datas = $("#addCuserForm").serialize();
	$.ajax({
		type : "POST",
		url : "/cuser/cuserAdd.ajax",
		data : "ajax=true&" + datas,
		dataType : "json",
		error	: function (e) { alert(error);},		
		success : function(data) {
			if(data.result == "1") {
				alert(success);
				document.location.reload();
			} else if(data.result == "-2") {
				alert(IDexists);
			} else {
				alert(error);
			}
		}
	});
	return;
}

function goUpdate(user_id){
	
	$.ajax({
		type : "POST",
		url : "/cuser/cuserUpdateData.ajax",
		data : "ajax=true&user_id=" + user_id, 
		dataType : "json",
		asysnc : false,
		error	: function (e) { alert(error); },
		success : function(data) {
			
			// fail_cnt에 따른 버튼 생성
			var failCnt = data.failCnt;
			var userFailCnt = data.cuserVo.fail_cnt;
			var lockCencel = document.getElementById('unLock');

			if(userFailCnt != null && failCnt <= userFailCnt) {
				lockCencel.style.display = 'block';
			} else {
				lockCencel.style.display = 'none';
			}
			
			// 운영자 정보
			$('#updUserId').val(data.cuserVo.user_id);
			$('#u_updUserId').val(data.cuserVo.user_id);
			$('#updUserName').val(data.cuserVo.user_name);
			$('#updCompId').val(data.cuserVo.comp_id);
			$('#updUserGrade').val(data.cuserVo.user_grade);
			$('#updGname').val(data.cuserVo.gname);
			
			$("#updateModal").modal("show");
			
		}
	});
	
}

/*패스워드 변경 여부*/
function pwdflag(){
	var flag=$('#cngPwd').is(':checked');
	
	if(false == flag){
		$("#updPassWd").attr("disabled", true);
		$("#chkUpdPassWd").attr("disabled", true);
	}else{
		$("#updPassWd").attr("disabled", false);
		$("#chkUpdPassWd").attr("disabled", false);
	}
}

function updateCuser(){

	var flag=$('#cngPwd').is(':checked');
	if(true == flag){
	    if ('' == $("#updPassWd").val()) {
	        alert('<spring:message code="cuser.confirm.enterPassword" />');
	        $("#updPassWd").focus();
	        return;
	    }
	    
	    if ('' == $("#chkUpdPassWd").val()) {
	        alert('<spring:message code="cuser.confirm.confirmPassword" />');
	        $("#chkUpdPassWd").focus();
	        return;
	    }
	    
	    if($("#updPassWd").val() != $("#chkUpdPassWd").val()){
	    	alert('<spring:message code="cuser.confirm.differentPassword" />');
	    	$("#updPassWd").focus();
	    	return;
	    }
	    
	    if(!chk_pwd($("#updUserId").val(), $("#updPassWd").val())){
	    	$("#updPassWd").focus();
			return;
		}
	}
    
	//이름
    if ('' == $("#updUserName").val()) {
        alert('<spring:message code="cuser.confirm.enterName" />');
        $("#updUserName").focus();
        return;
    }
	
	var datas = $("#updCuserForm").serialize();
	$.ajax({
		type : "POST",
		url : "/cuser/cuserUpdate.ajax",
		data : "ajax=true&" + datas,
		dataType : "json",
		error	: function (e) { alert(error); },
		success : function(data) {
			if(data.result == "1") {
				alert(success);
				document.location.reload();
			}else {
				alert(error);
			}
		}
	});
	return;
}

function delCuser(){
	let user_id = $('#updUserId').val();	
	
	if(!confirm(user_id + ' <spring:message code="cuser.confirm.deleteID" />')){
		return;
	}
	
	$.ajax({
		type : "POST",
		url : "/cuser/cuserDelete.ajax",
		data : "ajax=true&user_id=" + user_id,
		dataType : "json",
		error	: function (e) { alert(error);},
		success : function(data) {
			if(data.result == "1") {
				alert(erase);
				document.location.reload();
			}else {
				alert(error);
			}
		}
	});
}

function cuserUnLock(){
	
	let unlockMessage = '<spring:message code="cuser.confirm.unlock" />'
	let unlockAccount = '<spring:message code="cuser.confirm.account" />'	
	let unlockText = document.getElementById('unLock');
	let user_id = $('#updUserId').val();
	
	if(unlockText.innerText === '계정 잠금 해제') {
		if(!confirm(user_id + unlockMessage)){
			return;
		}
	} else { 
		if(!confirm(unlockMessage + " " + user_id + " " + unlockAccount)){
			return;
		}
	}
	//console.log(user_id);
	$.ajax({
		type : "POST",
		url : "/cuser/cuserUnLock.ajax",
		data : "ajax=true&user_id=" + user_id,
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
	$("#searchCuserForm").attr("action", "/cuser/cuserExcelDownload.do");
    $("#searchCuserForm").submit();
}

//패스워드 유효성
function chk_pwd(id, pwd){
	 var userID = id;
	 var password = pwd;
	  console.log(id);
	  console.log(pwd);
	 //특수문자 유효여부
	 if( password.search(/[\'\\\\\-()<>]/g) != -1)
	 {
		alert("<spring:message code='cuser.confirm.passwordCanOnly' />"); 
		return false; 
	 }
	 
	 // 길이
	 if(!/^[a-zA-Z0-9!@#$%^&*.?_~]{8,20}$/.test(password))
	 { 
		alert('<spring:message code="cuser.confirm.passwordMustBe" />'); 
		return false;
	 }
	 
	 // 영문, 숫자, 특수문자 2종 이상 혼용
	 var chk = 0;
	 if(password.search(/[0-9]/g) != -1 ) chk ++;
	 if(password.search(/[a-z]/ig)  != -1 ) chk ++;
	 if(password.search(/[!@#$%^&*?_~.]/g)  != -1  ) chk ++;
	 if(chk < 3)
	 { 
		alert('<spring:message code="cuser.confirm.passwordCombination" />'); 
		return false;
	 }
	  
	 // 동일한 문자/숫자 4이상, 연속된 문자
	 if(/(\w)\1\1\1/.test(password) || isContinuedValue(password))
	 {
		alert('<spring:message code="cuser.confirm.passwordCharactersAndNumber" />');
	  	return false;
	 }
	  
	 // 아이디 포함 여부
	 if(password.search(userID)>-1)
	 {
		alert('<spring:message code="cuser.confirm.passwordNotAllowed" />'); 
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
	
	<jsp:include page="../header.jsp" flush="false"/>
	
	<div class="contents-area">
		<header>
			<i class="icon-list"><span>icon</span></i>
			<h2><spring:message code="cuser.iwmOperatorManagement" /></h2>
		</header>
		<form name="searchCuserForm" id="searchCuserForm" method="post">
			<input type="hidden" id="pageno" name="pageno" value="${cuserVo.pageno }"/>
			<input type="hidden" id="excelMsg" name="excelMsg" value="">
			<div class="search-area">
				<label class="blind" for="searchCompId"><spring:message code="select.shipOwner" /></label>
				<select  id="searchCompId" name="searchCompId">
					<option value=""><spring:message code="select.shipOwner" /></option>
					<c:choose>
						<c:when test="${sessionScope.comp_id == '0' }">
							<option value="0" ${cuserVo.searchCompId == '0' ? 'selected' : ''}><spring:message code="cuser.noShipOwner" /></option>
						</c:when>
					</c:choose>
					<c:forEach var="ship" items="${compList }">
						<option value="${ship.comp_id }" <c:if test="${ship.comp_id eq cuserVo.searchCompId }">selected</c:if>>
							<c:out value="${ship.comp_name }" />
						</option>
					</c:forEach>
				</select>
				<input class="w180" type="text" id="searchUserName" name="searchUserName"  
					placeholder="<spring:message code="cuser.enterOperatorNickName" />" value="${cuserVo.searchUserName}">
				<input class="w160" type="text" id="searchUserId" name="searchUserId"  
					placeholder="<spring:message code="cuser.enterOperatorID" />" value="${cuserVo.searchUserId}">
				<select  id="searchGrade" name="searchGrade">
					<option value="" selected="selected"><spring:message code="cuser.grade" /></option>
					<c:forEach var="cuser" items="${gradeList }">
						<option value="${cuser.grade }" <c:if test="${cuser.grade eq cuserVo.searchGrade }">selected</c:if>>
							<c:out value="${cuser.gname }" />
						</option>
					</c:forEach>
				</select>
				<button class="btn-md white" type="button" id="btnSearch"><span><spring:message code="button.search" /></span></button>
			</div>	
			<div class="board-area">
				<table class="tbl-default bg-on">
					<colgroup>
						<col style="width:20%;">
						<col style="width:20%;">
						<col style="width:20%;">
						<col style="width:40%;">
					</colgroup>
					<thead>
						<tr>
							<th scope="col"><spring:message code="select.shipOwner" /></th>
							<th scope="col"><spring:message code="cuser.nickName" /></th>
							<th scope="col"><spring:message code="cuser.operatorID" /></th>
							<th scope="col"><spring:message code="cuser.grade" /></th>
						</tr>
					</thead>
					<tbody>
						<c:choose>
							<c:when test="${fn:length(cuserList) > 0 }">
								<c:forEach var="item" items="${cuserList }" varStatus="idx" >
									<tr id="list_${item.user_id }" onClick="goUpdate('${item.user_id}')" >							
					  					<td>${item.comp_name }</td>
					  					<td>${item.user_name }</td>
					  					<td>${item.user_id }</td>
					  					<td>${item.gname }</td>
					  				</tr>
								</c:forEach>
							</c:when>						
							<c:otherwise>
								<tr>
							    	<td colspan="4">
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
		</form>
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
	</div>

	<jsp:include page="../footer.jsp" flush="false"/>

</body>

	<!-- 추가 모달 -->
	<div class="modal fade" id="addModal" data-bs-backdrop="static"> 
    	<div class="modal-dialog modal-lg modal-dialog-centered " role="document">    	
        	<div class=" modal-content">       	
				<section class="contents-area ">
			       	<header>
					<i class="icon-write"><span>icon</span></i>
					<h2><spring:message code="cuser.iwmOperatorManagement" /></h2>				
					<div class="cancelArea" data-bs-dismiss="modal">
						<img class="cancelX" src="../web/images/common/btn-layer-close.png">
					</div>
					<!-- <i class="gray" style="width:76.5%;">
						<img class="img-0 " src="../web/images/common/btn-layer-close.png" data-bs-dismiss="modal" style="cursor:pointer;">
					</i> -->				
					</header>
					<form name="addCuserForm" id="addCuserForm" method="post">
						<div class="board-area">
							<table class="tbl-write">
								<colgroup>
								<col style="width:15%;">
								<col>
								<col style="width:15%;">
								<col style="width:35%;">
								</colgroup>
								<tbody>
									<tr>
										<th>
											<label for="addUserId" style="text-align:left;"><spring:message code="cuser.operatorID" />
												<span class="key">*</span>
											</label>
										</th>
										<td>
											<input class="width100" type="text" name="addUserId" id="addUserId" />
										</td>
										<th>
											<label for="addCompId"><spring:message code="select.shipOwner" />
												<span class="key">*</span>
											</label>
										</th>
										<td>
											<select id="addCompId" name="addCompId">
												<option value="0"><spring:message code="cuser.notChoosing" /></option>
												<c:forEach var="ship" items="${compList }">
													<option value="${ship.comp_id }" <%-- <c:if test="${ship.comp_id eq cuserVo.searchCompId }">selected</c:if> --%>>
														<c:out value="${ship.comp_name }" />
													</option>
												</c:forEach>
											</select>
										</td>
									</tr>
									<tr>
										<th>
											<label for="addPassWd"><spring:message code="cuser.password" /> 
												<span class="key">*</span>
											</label>
										</th>
										<td>
											<input class="width100" type="password" id="addPassWd" name="addPassWd" >
										</td>
										<th>
											<label for="chkAddPassWd"><spring:message code="cuser.confirmPassword" />
												<span class="key">*</span>
											</label>
										</th>
										<td>
											<input class="width100" type="password" id="chkAddPassWd" name="chkAddPassWd" >
										</td>
									</tr>
									<tr>
										<th>
											<label for="addUserName"><spring:message code="cuser.nickName" /> 
												<span class="key">*</span>
											</label>
										</th>
										<td>
											<input class="width100" type="text" id="addUserName" name="addUserName" >
										</td>
										<th>
											<label for="addUserGrade"><spring:message code="cuser.grade" /> 
												<span class="key">*</span>
											</label>
										</th>
										<td>
											<select id="addUserGrade" name="addUserGrade">
												<c:forEach var="cuser" items="${gradeList }">
													<option value="${cuser.grade }" <c:if test="${cuser.grade eq cuserVo.user_grade }">selected</c:if>>
														<c:out value="${cuser.gname }" />
													</option>
												</c:forEach>
											</select>
										</td>
									</tr>
							</table>
						</div>
					</form>
					<div class="board-btm-area">
						<div class="btn-area">
							<div class="fright">
								<button class="btn-lg burgundy btn-modal" id=btnAdd>
									<span>
										<spring:message code="button.save" />
									</span>
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
   <!-- 추가 모달 종료 --> 
   
   <!-- 수정 모달 -->
   <div class="modal fade" id="updateModal" data-bs-backdrop="static">
   	   <div class="modal-dialog modal-lg modal-dialog-centered" role="document">
	   	   <div class="modal-content">
			   <form id="updCuserForm" name="updCuserForm" method="post">
			   <input type="hidden" id="updUserId" name="updUserId" >
					<section class="contents-area"> 	
				       	<header>
						<i class="icon-write"><span>icon</span></i>
						<h2 id="updCuserLabel"><spring:message code="cuser.iwmOperatorManagement" /></h2>
						<div class="cancelArea" data-bs-dismiss="modal">
							<img class="cancelX" src="../web/images/common/btn-layer-close.png">
						</div>
						<button type="button" class="btn-close" data-bs-dismiss="modal" aria-laberl="Close">
							<span aria-hidden="true"></span>
						</button>
						</header>				
							<div class="board-area">
								<table class="tbl-write">
									<colgroup>
									<col style="width:15%;">
									<col>
									<col style="width:15%;">
									<col style="width:35%;">
									</colgroup>
									<tbody>
										<tr>
											<th>
												<label for="updUserId"><spring:message code="cuser.operatorID" /> 
													<span class="key">*</span>
												</label>
											</th>
											<td>
												<input class="width100" name="u_updUserId" id="u_updUserId" disabled="disabled"/>
											</td>
											<th>
												<label for="updCompId"><spring:message code="select.shipOwner" /> 
													<span class="key">*</span>
												</label>
											</th>
											<td>
												<select id="updCompId" name="updCompId">
													<option value="0"><spring:message code="cuser.notChoosing" /></option>
													<c:forEach var="ship" items="${compList }">
														<option value="${ship.comp_id }" <c:if test="${ship.comp_id eq cuserVo.searchCompId }">selected</c:if>>
															<c:out value="${ship.comp_name }" />
														</option>
													</c:forEach>
												</select>
											</td>
										</tr>
										<tr>
											<th>
												<label for="updPassWd">
													<spring:message code="cuser.password" /> 
													<span class="key">*</span>
												</label>
											</th>
											<td>
												<input class="width100" type="password" id="updPassWd" name="updPassWd" disabled="disabled">
												<div class="mt05">
													<span class="check-box1">
														<input type="checkbox" id="cngPwd" name="cngPwd" onClick="pwdflag();" />
														<label for="cngPwd" class="fs-11"><spring:message code="cuser.change" /></label>
													</span>
												</div>
											</td>
											<th>
												<label for="chkUpdPassWd">
													<spring:message code="cuser.confirmPassword" />
													<span class="key">*</span>
												</label>
											</th>
											<td>
												<input class="width100" type="password" id="chkUpdPassWd" name="chkUpdPassWd" disabled="disabled">
											</td>
										</tr>
										<tr>
											<th>
												<label for="updUserName">
													<spring:message code="cuser.nickName" /> 
													<span class="key">*</span>
												</label>
											</th>
											<td>
												<input class="width100" type="text" id="updUserName" name="updUserName" >
											</td>
											<th>
												<label for="updUserGrade">
	 												<spring:message code="cuser.grade" />
													<span class="key">*</span>
												</label>
											</th>
											<td>
												<select id="updUserGrade" name="updUserGrade">
													<c:forEach var="cuser" items="${gradeList }">
														<option value="${cuser.grade }" <c:if test="${cuser.grade eq cuserVo.user_grade }">selected</c:if>>
															<c:out value="${cuser.gname }" />
														</option>
													</c:forEach>
												</select>
											</td>
										</tr>
										
								</table>
							</div>
							<div class="board-btm-area">
								<div class="btn-area">
									<div class="fleft" id="fleft">
										<button type="button" class="btn-lg yellow" id="btnDel">
											<span>
												<spring:message code="button.delete" />
											</span>
										</button>
										<button type="button" class="btn-lg white" id="unLock" style="position:absolute; left:120px; bottom:13.77777px;">
											<span><spring:message code="cuser.accountUnlock" /></span>
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
				</form>
			</div>
		</div>
	</div>
   <!-- 수정 모달 종료 -->
</html>