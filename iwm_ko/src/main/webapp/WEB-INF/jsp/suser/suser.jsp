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
		$('#addSuserForm')[0].reset();
	});
	
	// 검색
    $("#btnSearch").bind("click", function() {
    	$("#pageno").val(1);
    	$("#searchSuserForm").attr("action", "/suser/suser.do");
        $("#searchSuserForm").submit();
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
 	
 	// 잠금 헤제
	$("#unLock").bind("click", function() {
    	suserUnLock();
    });
	
	// excel
    $("#btnExcel").bind("click", function() {
    	goExcelDown();
    }); 
	
 	// 추가모달 선주사에 따른 선박명 set
    $('#addCompId').on('change', function() {
        updateShipList('#addCompId', '#addShipName');
    });

    // 수정모달 선주사에 따른 선박명 set
    $('#updCompId').on('change', function() {
        updateShipList('#updCompId', '#updScode');
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
                alert('<spring:message code="confirm.error" />');
            },
            success: function(data) {
                var result = '<option value="select"><spring:message code="select.select" /></option>';

                for(i = 0; i < data.length; i++) {
                    result += "<option value='"+data[i].s_code+"'>"+data[i].s_name+"</option>";
                }
                $(shipNameSelector).html(result);
            }
        });
    } else {
        $(shipNameSelector).html('<option value=""><spring:message code="select.select" /></option>');
    }
}


//paging
function goPage(pageno) {
	$("#pageno").val(pageno);
	$("#searchSuserForm").attr("action", "/suser/suser.do");
    $("#searchSuserForm").submit();	
}

function addSuser(){
	
	//아이디
    if ('' == $("#addUserId").val()) {
        alert('<spring:message code="cuser.confirm.enterID" />');
        $("#addUserId").focus();
        return;
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
    
	if($('#addShipName').val() == "select"){
		alert('<spring:message code="confirm.select.ship" />');
		$('#addShipName').focus();
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

    var datas = $("#addSuserForm").serialize();
	$.ajax({
		type : "POST",
		url : "/suser/suserAdd.ajax",
		data : "ajax=true&" + datas,
		dataType : "json",
		error	: function (e) { alert('<spring:message code="confirm.error" />');},		
		success : function(data) {		
			if(data.result == "1") {
				alert('<spring:message code="confirm.success" />');
				document.location.reload();
			} else if(data.result == "-2") {
				alert('<spring:message code="cuser.confirm.IDexists" />');
			} else { 
				alert('<spring:message code="confirm.error" />');
			}
		}
	});
	return;
}

function goUpdate(user_id, s_code){
	
	$.ajax({
		type : "POST",
		url : "/suser/suserUpdateData.ajax",
		data : {
					'user_id' : user_id,
					's_code' : s_code
			   },
		dataType : "json",
		error	: function (e) { alert('<spring:message code="confirm.error" />'); },
		success : function(data) {
			
			/* console.log("   user_id : " + data.suserVo.user_id);
			console.log(" user_name : " + data.suserVo.user_name);
			console.log("user_grade : " + data.suserVo.user_grade);
			console.log("     gname : " + data.suserVo.gname);
			console.log("     scode : " + data.suserVo.s_code);
			console.log("    s_name : " + data.suserVo.s_name);
			console.log("   comp_id : " + data.suserVo.comp_id);
			console.log(" comp_name : " + data.suserVo.comp_name);
			console.log("    length : " + data.shipList.length);
			console.log(""); */
			
			// 운영자 정보
			$('#updUserId').val(data.suserVo.user_id);
			$('#u_updUserId').val(data.suserVo.user_id);
			
			/* 선주명에 따른 선박 리스트 */
			var updScode = $('#updScode');
			updScode.empty();
			updScode.append('<option value=""><spring:message code="select.select" /></option>');
			
			for(var i = 0; i < data.shipList.length; i++){	
				updScode += "<option value='"+data.shipList[i].s_code+"'>"+data.shipList[i].s_name+"</option>";	
			}
			$('#updScode').html(updScode); 
			$('#updScode2').val(data.suserVo.s_code); 
			/* 선주명에 따른 선박 리스트 끝 */		
			
			/* updScode 드롭다운 selected 설정 */
			let scode = document.getElementById('updScode');
			let len = scode.options.length;
			let str = document.getElementById('updScode2');

			for (let i=0; i<len; i++){  
				if(scode.options[i].value == str.value){
					scode.options[i].selected = true;
				}
			}

			$('#updUserName').val(data.suserVo.user_name);
			$('#updCompId').val(data.suserVo.comp_id);
			$('#updUserGrade').val(data.suserVo.user_grade);

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

function updateSuser(s_code){
	
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
	
	if($('#updScode').val() == "select" || $('#updScode').val() == ""){
		alert('<spring:message code="confirm.select.ship" />');
		$('#updScode').focus();
		return;
	}
	
	var datas = $("#updSuserForm").serialize();
	$.ajax({
		type : "POST",
		url : "/suser/suserUpdate.ajax",
		data : "ajax=true&" + datas,
		dataType : "json",
		error	: function (e) { alert('<spring:message code="confirm.error" />'); },
		success : function(data) {
			if(data.result == "1") {
				alert('<spring:message code="confirm.success" />');
				document.location.reload();
			}else {
				alert('<spring:message code="confirm.error" />');
			}
		}
	});
	return;
}

function delSuser(){
	var user_id = $('#updUserId').val();
	
	if(!confirm(user_id + '<spring:message code="cuser.confirm.deleteID" />')){
		return;
	}
	
	$.ajax({
		type : "POST",
		url : "/suser/suserDelete.ajax",
		data : "ajax=true&user_id=" + user_id,
		dataType : "json",
		error	: function (e) { alert('<spring:message code="confirm.error" />');},
		success : function(data) {
			if(data.result == "1") {
				alert('<spring:message code="confirm.delete" />');
				document.location.reload();
			}else {
				alert('<spring:message code="confirm.error" />');
			}
		}
	});
}

//엑셀 다운로드
function goExcelDown() {
	
	var msg = "";
	
	msg = prompt('<spring:message code="confirm.downloadExcel" />', '');

	if(!msg) {
		alert('<spring:message code="confirm.downloadCanceled" />');
		return;
	}
	
	$("input[name ='excelMsg']").val(msg);
	
	$("#searchSuserForm").attr("action", "/suser/suserExcelDownload.do");
    $("#searchSuserForm").submit();
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
			<h2><spring:message code="suser.owmOperatorManagement" /></h2>
		</header>
		<form name="searchSuserForm" id="searchSuserForm" method="post">
			<input type="hidden" id="pageno" name="pageno" value="${suserVo.pageno }"/>
			<input type="hidden" id="excelMsg" name="excelMsg" value="">
			<input type="hidden" id="compId" name="compId" value="">
			<div class="search-area">
				<select id="searchCompId" name="searchCompId">
					<option value=""><spring:message code="select.shipOwner" /></option>
					<c:forEach var="ship" items="${compList }">
						<option value="${ship.comp_id}" <c:if test="${ship.comp_id eq suserVo.searchCompId }">selected</c:if> >
							<c:out value="${ship.comp_name}" />
						</option>
					</c:forEach>
				</select>
				<input type="text" class="w150" maxlength="" id=searchSname name="searchSname" value="${suserVo.searchSname }" 
					placeholder="<spring:message code="suser.enterShipName" />">
				<select  id="searchGrade" name="searchGrade">
					<option value=""><spring:message code="list.grade" /></option>
					<c:forEach var="ship" items="${gradeList }">
						<option value="${ship.grade }" <c:if test="${ship.grade eq suserVo.searchGrade }">selected</c:if>>
							<c:out value="${ship.gname }" />
						</option>
					</c:forEach>
				</select>
				<input type="text" class="w150" maxlength="" id="searchUserId" name="searchUserId" value="${suserVo.searchUserId }" 
					placeholder="<spring:message code="cuser.enterOperatorID" />">
				<input type="text" class="w180" maxlength="" id="searchUserName" name="searchUserName" value="${suserVo.searchUserName }" 
					placeholder="<spring:message code="cuser.enterOperatorNickName" />">
				<button class="btn-md white" type="button" id="btnSearch">
					<span><spring:message code="button.search" /></span>
				</button>
			</div>
			<div class="board-area">
				<table class="tbl-default bg-on">
					<colgroup>
						<col style="width:22%;">
						<col style="width:20%;">
						<col style="width:20%;">
						<col style="width:22%;">
						<col style="">
					</colgroup>
					<thead>
						<tr>
							<th scope="col"><spring:message code="select.shipOwner" /></th>
							<th scope="col"><spring:message code="select.shipName" /></th>
							<th scope="col"><spring:message code="list.nickName" /></th>
							<th scope="col"><spring:message code="list.operatorID" /></th>
							<th scope="col"><spring:message code="list.grade" /></th>
						</tr>
					</thead>
					<tbody>
						<c:choose>
							<c:when test="${fn:length(suserList) > 0 }">
								<c:forEach var="item" items="${suserList }" varStatus="idx" >
									<tr id="list_${item.user_id }" onClick="goUpdate('${item.user_id}', '${item.s_code}')" >							
					  					<td>${item.comp_name }</td>
					  					<td>${item.s_name }</td>
					  					<td>${item.user_name }</td>
					  					<td>${item.user_id }</td>
					  					<td>${item.gname }</td>
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
				    	<button type="button" class="btn-lg blue" id="add" data-bs-toggle="modal" data-bs-target="#addModal">
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
						<h2><spring:message code="suser.owmOperatorManagement" /></h2>
						<div class="cancelArea" data-bs-dismiss="modal">
							<img class="cancelX" src="../web/images/common/btn-layer-close.png">
						</div>
					</header>
					<form name="addSuserForm" id="addSuserForm" method="post">
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
											<label for="addUserId" style="text-align:left;"><spring:message code="cuser.operatorID" />
												<span class="key">*</span>
											</label>
										</th>
										<td colspan="3">
											<input type="text" name="addUserId" id="addUserId" class="width100"/>
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
										<th><label for="addCompId">
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
											<label for="addShipName"><spring:message code="select.shipName" />
												<span class="key">*</span>
											</label>
										</th>
										<td id="sname">
											<select id="addShipName" name="addShipName">
												<option value="select"><spring:message code="select.select" /></option>
											</select>
										</td>
									</tr>
									<tr>
										<th>
											<label for="addUserName"><spring:message code="list.nickName" />
												<span class="key">*</span>
											</label>
										</th>
										<td>
											<input class="width100" type="text" id="addUserName" name="addUserName" >
										</td>
										<th>
											<label for="addUserGrade"><spring:message code="list.grade" />
												<span class="key">*</span>
											</label>
										</th>
										<td>
											<select id="addUserGrade" name="addUserGrade">
												<c:forEach var="suser" items="${gradeList }">
													<option value="${suser.grade }">
														<c:out value="${suser.gname }" />
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
						<h2><spring:message code="suser.owmOperatorManagement" /></h2>
						<div class="cancelArea" data-bs-dismiss="modal">
							<img class="cancelX" src="../web/images/common/btn-layer-close.png">
						</div>
					</header>
					<form name="updSuserForm" id="updSuserForm" method="post">
					 <input type="hidden" id="updUserId" name="updUserId">
					 <input type="hidden" id="updScode2" name="updScode2"/>
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
											<label for="updUserId" style="text-align:left;"><spring:message code="list.operatorID" />
												<span class="key">*</span>
											</label>
										</th>
										<td colspan="3">
											<input name="u_updUserId" id="u_updUserId" class="width100"/>
										</td>
									</tr>
									<tr>
										<th>
											<label for="updPassWd"><spring:message code="cuser.password" /> 
												<span class="key">*</span>
											</label>
										</th>
										<td>
											<input class="width100" type="password" id="updPassWd" name="updPassWd" disabled="disabled">
											<div class="mt05">
												<span class="check-box1">
													<input type="checkbox" id="cngPwd" name="cngPwd" onClick="pwdflag();" />
													<label for="cngPwd" class="fs-11">
														<spring:message code="cuser.change" /> 
													</label>
												</span>
											</div>
										</td>
										<th>
											<label for="chkUpdPassWd"><spring:message code="cuser.confirmPassword" /> 
												<span class="key">*</span>
											</label>
										</th>
										<td>
											<input class="width100" type="password" id="chkUpdPassWd" name="chkUpdPassWd" disabled="disabled">
										</td>
									</tr>
									<tr>
										<th>
											<label for="updCompId"><spring:message code="select.shipOwner" /> 	
												<span class="key">*</span>
											</label>
										</th>
										<td>
											<select id="updCompId" name="updCompId">
												<option value=""><spring:message code="select.select" /></option>
												<c:forEach var="ship" items="${compList }">
													<option value="${ship.comp_id}" <%-- <c:if test="${ship.comp_id eq cuserVo.searchCompId }">selected</c:if> --%>>
														<c:out value="${ship.comp_name}" />
													</option>
												</c:forEach>
											</select>
										</td>
										<th>
											<label for="updScode"><spring:message code="select.shipName" />
												<span class="key">*</span>
											</label>
										</th>
										<td id="sname">
											<select id="updScode" name="updScode">
											</select>
										</td>
									</tr>
									<tr>
										<th>
											<label for="updUserName"><spring:message code="list.nickName" /> 
												<span class="key">*</span>
											</label>
										</th>
										<td>
											<input class="width100" type="text" id="updUserName" name="updUserName" >
										</td>
										<th>
											<label for="updUserGrade"><spring:message code="list.grade" /> 
												<span class="key">*</span>
											</label>
										</th>
										<td>
											<select id="updUserGrade" name="updUserGrade">
												<c:forEach var="gradeListCd" items="${gradeList }">
													<option value="${gradeListCd.grade }" >
														<c:out value="${gradeListCd.gname }" />
													</option>
												</c:forEach>
											</select>
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