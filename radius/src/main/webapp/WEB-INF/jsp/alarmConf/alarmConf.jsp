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
		$('#addAlarmConfForm')[0].reset();
	});
	
	// 추가
    $("#btnAdd").bind("click", function() {
    	addAlarmConf();
    });
	
    // 수정
    $("#btnUpd").bind("click", function() {
    	updateAlarmConf();
    });
	
	// 삭제
    $("#btnDel").bind("click", function() {
    	delAlarmConf();
    });
	
	// excel
    $("#btnExcel").bind("click", function() {
    	goExcelDown();
    });
		
});

//paging
function goPage(pageno) {
	$("#pageno").val(pageno);
	$("#alarmConfForm").attr("action", "/alarmConf/alarmConf.do");
    $("#alarmConfForm").submit();	
}

// 영문자만 허용(한글, 숫자, 특수문자 안써짐)
function handleOnInput(e)  {
	e.value = e.value.replace(/[^A-Za-z]/ig, '')
}

function addAlarmConf(){
	
	// 감시주기
    if ('' == $("#addPeriod").val()) {
        alert("감시 주기가 입력되지 않았습니다. 확인 후 등록해주세요.");
        $("#addPeriod").focus();
        return;
    } 
 	// 감시주기
    if (1 > $("#addPeriod").val()) {
        alert("감시 주기는 1(분)부터 입력 가능합니다.");
        $("#addPeriod").focus();
        return;
    } 
 	// 감시주기
    if (!$.isNumeric($("#addPeriod").val())) {
		alert("감시 주기는 숫자로만 입력 가능합니다.");
		$("#addPeriod").focus();
		return;
	}

 	// 임계치
    if ('' == $("#addAuthCnt").val()) {
		alert("임계치가 입력되지 않았습니다. 확인 후 등록해주세요.");
		$("#addAuthCnt").focus();
		return;
	}
 	// 임계치
    if (!$.isNumeric($("#addAuthCnt").val())) {
		alert("임계치는 숫자로만 입력 가능합니다.");
		$("#addAuthCnt").focus();
		return;
	}

	var datas = $("#addAlarmConfForm").serialize();
	$.ajax({
		type : "POST",
		url : "/alarmConf/alarmConfAdd.ajax",
		data : "ajax=true&" + datas,
		dataType : "json",
		error	: function (e) { alert("오류가 발생하였습니다. 관리자에게 문의 바랍니다."); },
		success : function(data) {
			if(data.result == "1") {
				alert("정상적으로 추가되었습니다.");
				document.location.reload();
			} else if(data.result == "-2") {
				alert("'감시주기(분)'와 '알람발생사유' 두 가지 모두를 중복으로 등록할 수 없습니다.")
			} else {
				alert("오류가 발생하였습니다. 관리자에게 문의 바랍니다.");
			}
		}
			
	});
	return;
}

function goUpdate(period, auth_flag){
	$.ajax({
		type : "POST",
		url : "/alarmConf/alarmConfSetUpdateData.ajax",
		data : {
				'period' : period,
				'auth_flag' : auth_flag
			   },
		dataType : "json",
		error	: function (e) { alert("오류가 발생하였습니다. 관리자에게 문의 바랍니다."); },
		success : function(data) {
			$('#updPeriod').val(data.alarmConfVo.period);
			$('#u_updPeriod').val(data.alarmConfVo.period);
			$('#updAuthFlag').val(data.alarmConfVo.auth_flag);
			$('#u_updAuthFlag').val(data.alarmConfVo.auth_flag);
			$('#updAuthCnt').val(data.alarmConfVo.auth_cnt);
			$('#updUseYn').val(data.alarmConfVo.use_yn);
			$('#updDescr').val(data.alarmConfVo.descr);
			
			$("#updateModal").modal("show");
		}
	});
}

function updateAlarmConf(){
	
 	// 임계치
    if ('' == $("#updAuthCnt").val()) {
		alert("임계치가 입력되지 않았습니다. 확인 후 등록해주세요.");
		$("#updAuthCnt").focus();
		return;
	}
 	// 임계치
    if (!$.isNumeric($("#updAuthCnt").val())) {
		alert("임계치는 숫자로만 입력 가능합니다.");
		$("#updAuthCnt").focus();
		return;
	}
 	
	var datas = $("#updAlarmConfForm").serialize();
	$.ajax({
		type : "POST",
		url : "/alarmConf/alarmConfUpdate.ajax",
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

function delAlarmConf(){
	var period = $('#updPeriod').val();
	var auth_flag = $('#updAuthFlag').val();
	
	if(!confirm("해당 알람설정이 삭제됩니다. 삭제를 진행하시겠습니까?")){
		return;
	}
	
	$.ajax({
		type : "POST",
		url : "/alarmConf/alarmConfDelete.ajax",
		data : {
					'period' : period ,
					'auth_flag' : auth_flag
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
	$("#updAlarmConfForm").attr("action", "/alarmConf/alarmConfExcelDownload.do");
    $("#updAlarmConfForm").submit();
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

<form name="alarmConfForm" id="alarmConfForm" method="post">
	<input type="hidden" id="pageno" name="pageno" value="${alarmConfVo.pageno }"/>
</form>

<div class="container min-vh-100">
	<ol class="breadcrumb mt-4 py-3 fs-5">
		<li class="breadcrumb-item active">관리</li>
		<li class="breadcrumb-item active">알람 설정</li>
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
								<col style="width:20%;">
								<col style="width:20%;">									
								<col style="width:20%;;">
								<col style="width:20%;;">
								<col style="width:40%;;">					
						</colgroup>
						<thead>
							<tr class="table-primary" >
								<th scope="col">감시 주기(분)</th>
								<th scope="col">알람 발생 사유</th>
								<th scope="col">임계치</th>
								<th scope="col">사용 여부</th>
								<th scope="col">설명</th>
							</tr>
						</thead>
						<tbody>
							<c:choose>
								<c:when test="${fn:length(alarmConfList) > 0}">
									<c:forEach var="item" items="${alarmConfList }" varStatus="idx">
										<tr id="list_${item.period }" onClick="goUpdate('${item.period}', '${item.auth_flag}')">									
											<td>${item.period }</td>
											<td>
												<c:set var="flag" value="${item.auth_flag }"/>
												<c:choose>
													<c:when test="${flag == 'A' }">인증 시도수 초과</c:when>
													<c:when test="${flag == 'F' }">인증 실패수 초과</c:when>
													<c:when test="${flag == 'S' }">인증 성공수 초과</c:when>
												</c:choose>
											</td>
											<td>${item.auth_cnt }</td>
											<td>
												<c:set var="flag" value="${item.use_yn }"/>
												<c:choose>
													<c:when test="${flag == 'Y' }">사용</c:when>
													<c:when test="${flag == 'N' }">미사용</c:when>
												</c:choose>
											</td>
											<td>${item.descr }</td>
										</tr>
									</c:forEach>
								</c:when>
								<c:otherwise>
                                	<tr>
                                    	<td colspan="5">
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
				<h5>알람설정 등록</h5>
				<button type="button" class="btn-close" data-bs-dismiss="modal" aria-laberl="Close">
					<span aria-hidden="true"></span>
				</button>
			</div>
			<div class="modal-body px-3">
				<form name="addAlarmConfForm" id="addAlarmConfForm" method="post">
					<div class="row mb-4">
						<div class="col-6">
							<label for="addPeriod" class="col-form-label">감시 주기(분)</label> 
							<input type="text" class="form-control" id="addPeriod" name="addPeriod">
						</div>
						<div class="col-6">
							<label for="addAuthFlag" class="col-form-label">알람 발생 사유</label> 
							<select class="form-select" id="addAuthFlag" name="addAuthFlag">
								<option value="A">인증 시도수 초과</option>
								<option value="F">인증 실패수 초과</option>
								<option value="S">인증 성공수 초과</option>
								<%-- <c:forEach var="conf" items="${authFlagList }">
									<option value="${conf.auth_flag }">
										<c:choose>
											<c:when test="${conf.auth_flag == 'A'}" >인증 시도수 초과</c:when>									
											<c:when test="${conf.auth_flag == 'F'}" >인증 실패수 초과</c:when>									
											<c:when test="${conf.auth_flag == 'S'}" >인증 성공수 초과</c:when>									
										</c:choose>
									</option>
								</c:forEach> --%>
							</select>
						</div>		
					</div>
					<div class="row mb-4 mt-2">
						<div class="col-6">
							<label for="addAuthCnt" class="col-form-label">임계치</label> 
							<input type="text" class="form-control" id="addAuthCnt" name="addAuthCnt">
						</div>
						<div class="col-6">
							<label for="addUseYn" class="col-form-label">사용 여부</label> 
							<select class="form-select" id="addUseYn" name="addUseYn">
								<option value="Y">사용</option>
								<option value="N">미사용</option>
							</select>
						</div>
					</div>
					<div class="row mb-4">
						<div class="col-12">
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

<!-- 수정 Modal 시작 -->
<div class="modal fade" id="updateModal" data-bs-backdrop="static">
	<div class="modal-dialog modal-lg modal-dialog-centered" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h5>알람 설정 수정</h5>
				<button type="button" class="btn-close" data-bs-dismiss="modal" aria-laberl="Close">
					<span aria-hidden="true"></span>
				</button>
			</div>
			<div class="modal-body px-3">
				<form name="updAlarmConfForm" id="updAlarmConfForm" method="post">
				<input type="hidden" id="updPeriod" name="updPeriod" >
				<input type="hidden" id="updAuthFlag" name="updAuthFlag" >
					<div class="row mb-4">
						<div class="col-6">
							<label for="updPeriod" class="col-form-label">감시 주기</label> 
							<input type="text" class="form-control" id="u_updPeriod" name="u_updPeriod" disabled="disabled">
						</div>
						<div class="col-6">
							<label for="updAuthFlag" class="col-form-label">알람 발생 사유</label> 
							<select class="form-select" id="u_updAuthFlag" name="u_updAuthFlag" disabled="disabled">
								<option value="A">인증 시도수 초과</option>
								<option value="F">인증 실패수 초과</option>
								<option value="S">인증 성공수 초과</option>
							</select>
						</div>	
					</div>
					<div class="row mb-4 mt-2">
						<div class="col-6">
							<label for="updAuthCnt" class="col-form-label">임계치</label> 
							<input type="text" class="form-control" id="updAuthCnt" name="updAuthCnt">
						</div>
						<div class="col-6">
							<label for="updUseYn" class="col-form-label">사용 여부</label> 
							<select class="form-select" id="updUseYn" name="updUseYn">
								<option value="Y">사용</option>
								<option value="N">미사용</option>
							</select>
						</div>
					</div>
					<div class="row mb-4">
						<div class="col-12">
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

<!-- footer -->
<%@ include file="/decorators/footer.jsp"%>
<!-- //footer -->	

</body>
</html>