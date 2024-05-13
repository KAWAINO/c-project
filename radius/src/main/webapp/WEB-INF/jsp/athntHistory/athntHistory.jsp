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

//replyList
var reply = false;

function selectReplyList() {
    var div = document.getElementById("replyDiv");
    var search = document.getElementById("searchReply");
    var checkboxes = document.getElementsByName('replyMultiCheck');

    var chkArray = [];
    $('input:checkbox[name="replyMultiCheck"]:checked').each(function() {
        chkArray.push("'" + this.getAttribute('nm') + "'" );
    });
    var List = chkArray.join(",");
    /* console.log("protocolList ::"  + List); */

    if (chkArray.length > 0) {
        if(chkArray.length === checkboxes.length) {
        	search.value = '전체 항목';
        } else {
        	search.value = chkArray.join(",");
        }
    } else {
    	search.value = '전체 항목';
    }
    if (!reply) {
        div.style.display = "block";
        reply = true;
    } else {
        div.style.display = "none";
        reply = false;
    }
};

// cause
var cause = false;

function selectCauseList() {
    var div = document.getElementById("causeDiv");
    var search = document.getElementById("searchCause");
    var checkboxes = document.getElementsByName('causeMultiCheck');

    var chkArray = [];
    $('input:checkbox[name=causeMultiCheck]:checked').each(function() {
        chkArray.push( "'" + this.getAttribute('nm') + "'" );
        console.log(this);
    });
    var List = chkArray.join(",");
    /* console.log("protocolList ::"  + List); */

    if (chkArray.length > 0) {
        if(chkArray.length === checkboxes.length) {
        	search.value = '전체 항목';
        } else {
        	search.value = chkArray.join(",");
        }
    } else {
    	search.value = "전체 항목";
    }
    if (!cause) {
        div.style.display = "block";
        cause = true;
    } else {
        div.style.display = "none";
        cause = false;
    }
};

$(document).ready(function() { 
	
	// replyAllChk  
	$("#replyAllChk").click(function() {
		
		if($(this).is(":checked")) {
			$("input[name=replyMultiCheck]").prop("checked", true);
		} else {
			$("input[name=replyMultiCheck]").prop("checked", false);
		}
	});

	$("input[name=replyMultiCheck]").click(function() {
		
		var all = $("input[name=replyMultiCheck]").length;
		var chked = $("input[name=replyMultiCheck]:checked").length;
		var chkedCount = 0;

		if(all == chked) {
			$("#replyAllChk").prop("checked", true);
		} else {
			$("#replyAllChk").prop("checked", false); 
		}
	});
	
	// causeAllChk  
	$("#causeAllChk").click(function() {
		
		if($(this).is(":checked")) {
			$("input[name=causeMultiCheck]").prop("checked", true);
		} else {
			$("input[name=causeMultiCheck]").prop("checked", false);
		}
	});

	$("input[name=causeMultiCheck]").click(function() {
		
		var all = $("input[name=causeMultiCheck]").length;
		var chked = $("input[name=causeMultiCheck]:checked").length;
		var chkedCount = 0;

		if(all == chked) {
			$("#causeAllChk").prop("checked", true);
		} else {
			$("#causeAllChk").prop("checked", false); 
		}
	});

	// replyList
	var dataArray = document.getElementById("searchReply").value
    /* console.log(dataArray); */
    if(dataArray !== 'null'){
       var checkboxes = document.getElementsByName('replyMultiCheck');
       var replyAllChk = document.getElementsByName('replyAllChk');
       var checkedCount = 0;

       for(var i=0; i < checkboxes.length; i++) {
       	   if(dataArray.includes(checkboxes[i].value) || dataArray.includes() || dataArray.includes('전체 항목')) {
               checkboxes[i].checked = true;
               checkedCount++;       
       	   }
       	   if(dataArray.includes('전체 항목')){
       	   	   $("#replyAllChk").prop("checked", true);
       	   }
       }
	   if(checkedCount === checkboxes.length) {
	           document.getElementById("searchReply").value = '전체 항목';
	   }
    };
	
	// causeList
    var dataArray = document.getElementById("searchCause").value
    /* console.log(dataArray); */
    if(dataArray !== 'null'){
       var checkboxes = document.getElementsByName('causeMultiCheck');
       var checkedCount = 0;

       for(var i=0; i < checkboxes.length; i++) {
       	   if(dataArray.includes(checkboxes[i].value) || dataArray.includes() || dataArray.includes('전체 항목')) {
               checkboxes[i].checked = true;
               checkedCount++;
           }
       	   if(dataArray.includes('전체 항목')){
    	   	   $("#causeAllChk").prop("checked", true);
    	   }
       }
       if(checkedCount === checkboxes.length) {
           document.getElementById("searchCause").value = '전체 항목';
       }  
    };

    //날짜 선택
    $("#startDate, #endDate").datepicker({
        format: 'yyyy-mm-dd',
        autoclose: true
    });
    
	//검색
	$("#btnSearch").bind("click", function() {

		// 날짜 설정
        var start_date = document.getElementById("startDate").value + document.getElementById("searchStartHour").value + document.getElementById("searchStartMin").value + "00";
        var end_date = document.getElementById("endDate").value + document.getElementById("searchEndHour").value + document.getElementById("searchEndMin").value + "59";
        
     	// 날짜 검색 조건
        if (start_date >= end_date) {
        	alert("검색 기간 설정이 잘못되었습니다. 시작날짜가 종료날짜보다 늦습니다.");
            return;
        }
		
		$("#pageno").val(1);
        $("#searchChecked").val("true");
		$("#searchAthntHistoryForm").attr("action", "/athntHistory/athntHistory.do");
		$("#searchAthntHistoryForm").submit();
	});

	//초기화
	$("#btnReset").bind("click", function() {	

		$('#searchMsisdn').val("");
		$('#searchImsi').val("");
		$("#searchUserName").val("");	
		$("#startDate").val("${timeDefaultStart}");
        $("#endDate").val("${timeDefaultEnd}");
        $('#searchReply').val("전체 항목");
        $('#searchCause').val("전체 항목");
        $('input:checkbox[name=replyAllChk]').prop("checked", true);
        $('input:checkbox[name=causeAllChk]').prop("checked", true);
        $('input:checkbox[name=replyMultiCheck]').prop("checked", true);
        $('input:checkbox[name=causeMultiCheck]').prop("checked", true);
        $("#searchStartHour").val("00");
        $("#searchStartMin").val("00");
        $("#searchEndHour").val("23");
        $("#searchEndMin").val("59");
	});
	
	// excel
	$("#btnExcel").bind("click", function() {
		goExcelDown();
	});

});

//paging
function goPage(pageno) {
	$("#pageno").val(pageno);
	$("#searchChecked").val("true");
	$("#searchAthntHistoryForm").attr("action", "/athntHistory/athntHistory.do");
	$("#searchAthntHistoryForm").submit();
}

//엑셀 다운로드
function goExcelDown() {
	$("#searchAthntHistoryForm").attr("action", "/athntHistory/athntHistoryExcelDownload.do");
    $("#searchAthntHistoryForm").submit();
}

</script>
</head>

<body>

<!-- header -->
<%@ include file="/decorators/header.jsp"%>
<!-- //header -->

<div class="container min-vh-100">
	<ol class="breadcrumb mt-4 py-3 fs-5">
		<li class="breadcrumb-item active">이력</li>
		<li class="breadcrumb-item active">인증 이력</li>
	</ol>
	<!-- 검색 -->
	<div class="card border-primary mb-4">
		<div class="card-header mb-3">상세검색</div>
		<div class="card-body" >
			<form name="searchAthntHistoryForm" id="searchAthntHistoryForm" method="post">
				<input type="hidden" id="pageno" name="pageno" value="${athntHistoryVo.pageno }"/>
				<input type="hidden" id="searchChecked" name="searchChecked" value="false">		
				<div class="row">	
					<label class="col-form-label col-1 mb-1 offset-12">인증 결과</label>		
					<div class="col-15-6 " >
						<input class="form-select-check " type="text" id="searchReply" name="searchReply" readonly="readonly"
							onclick="selectReplyList()" value="${athntHistoryVo.searchReply != null ? athntHistoryVo.searchReply : '전체 항목'}">
						<div id="replyDiv" class="form-control" style="display: none; position: absolute; width: 13.5%;">
							<div class="mt-1 col-12">
								<div class="mb-2">
									<label>
										<input type="checkbox" class="form-check-input" id="replyAllChk" name="replyAllChk" value="전체">
										<small>전체</small>
									</label>
								</div>
								<c:forEach var="replyList" items="${replyList }">
									<label for="checkbox_${replyList.reply}">
										<input type="checkbox" class="form-check-input" nm="${replyList.reply}"
											id="checkbox_${replyList.reply}" name="replyMultiCheck" value="${replyList.reply}">
										<small>${replyList.reply}</small>
									</label>
								</c:forEach>
							</div>
							<div style="margin-top : 5%; font-size:12px;"> 
        						<input type="button" class="form-control btn btn-primary " style=";"value="확인" onclick="selectReplyList()"/> 
    						</div> 
						</div>						
					</div>
					<label class="col-form-label col-17 mb-1" for="" >MSISDN</label>		
					<div class="col-15-6">
						<input type="text" class="form-control" id="searchMsisdn" name="searchMsisdn" value="${athntHistoryVo.searchMsisdn}"/>
					</div>
					<label class="col-form-label col-16 mb-1" for="" >IMSI</label>		
					<div class="col-15-6">
						<input type="text" class="form-control" id="searchImsi" name="searchImsi" value="${athntHistoryVo.searchImsi}"/>
					</div>
					<label class="col-form-label col-1 mb-1" for="" >Username</label>		
					<div class="col-15-6">
						<input type="text" class="form-control" id="searchUserName" name="searchUserName" value="${athntHistoryVo.searchUserName}"/>
					</div>
				</div>
				<div class="row mt-4">
					<label class="col-form-label col-1 mb-1 offset-12" for="searchDate1" >검색 기간</label>		
					<div class="col-15">
						<input type="text" class="form-control datePicker" id="startDate" name="startDate" value="${timeDefaultStart}"/>
					</div>
					<div class="col-18">						
						<select class="form-select" id="searchStartHour" name="searchStartHour" >					
							<!-- <option value="" selected disabled hidden>00</option> -->
								<c:forEach begin="0" end="23" var="hour">
                                	<c:set var="setHour" value="${hour}" />
                                    	<c:if test="${hour < 10}">
                                        	<c:set var="setHour" value='0${hour}' />
                                        </c:if>
                                        <option value="${setHour}" ${setHour eq athntHistoryVo.searchStartHour ? 'selected' : ''}>
                                        	<c:out value="${setHour}" />
                                        </option>
                                </c:forEach>
						</select>
					</div>
					<div class="col-19">
						<label class="col-form-label" >시</label>
					</div>
					<div class="col-18">						
						<select class="form-select" id="searchStartMin" name="searchStartMin">					
							<!-- <option value="" selected disabled hidden>00</option> -->
								<c:forEach begin="0" end="59" var="min">
                                	<c:set var="setMin" value="${min}" />
                                   		<c:if test="${min < 10}">
                                        	<c:set var="setMin" value='0${min}' />
                                        </c:if>
                                        <option value="${setMin}" ${setMin eq athntHistoryVo.searchStartMin ? 'selected' : ''}>
                                        	<c:out value="${setMin}" />
                                        </option>
								</c:forEach>
						</select>
					</div>
					<div class="col-19">
						<label class="col-form-label" >분 ~</label>
					</div>
					<div class="col-15-5">
						<input type="text" class="form-control datePicker" id="endDate" name="endDate" value="${timeDefaultEnd}"/>
					</div>
					<div class="col-18">						
						<select class="form-select" id="searchEndHour" name="searchEndHour">					
							<!-- <option value="" selected disabled hidden>00</option> -->
								<c:forEach begin="0" end="23" var="hour">
                                	<c:set var="setHour" value="${hour}" />
                                    	<c:if test="${hour < 10}">
                                        	<c:set var="setHour" value='0${hour}' />
                                        </c:if>
                                        <option value="${setHour}" ${setHour eq athntHistoryVo.searchEndHour ? 'selected' : ''}>
                                        	<c:out value="${setHour}" />
                                       	</option>
                                </c:forEach>
						</select>
					</div>
					<div class="col-19">
						<label class="col-form-label" >시</label>
					</div>
					<div class="col-18">						
						<select class="form-select" id="searchEndMin" name="searchEndMin" >					
							<!-- <option value="" selected disabled hidden>00</option> -->
								<c:forEach begin="0" end="59" var="min">
                                	<c:set var="setMin" value="${min}" />
                                    	<c:if test="${min < 10}">
                                        	<c:set var="setMin" value='0${min}' />
                                        </c:if>
                                        <option value="${setMin}" ${setMin eq athntHistoryVo.searchEndMin ? 'selected' : ''}>
                                        	<c:out value="${setMin}" />
                                        </option>
                                </c:forEach>
						</select>
					</div>	
					<div class="col-19">
						<label class="col-form-label" >분</label>
					</div>
						<label class="col-form-label col-1 mb-1 offset-13">결과 원인</label>		
					<div class="col-15" >
						<input class="form-select-check" type="text" id="searchCause" name="searchCause" readonly="readonly"
							onclick="selectCauseList()" value="${athntHistoryVo.searchCause != null ? athntHistoryVo.searchCause : '전체 항목'}">
						<div id="causeDiv" class="form-control-0" style="display: none; position: absolute; width: 13.5%;"> 
							<div class="mt-1 col-12">
								<div class="mb-2">
									<label>
										<input type="checkbox" class="form-check-input" id="causeAllChk" name="causeAllChk" value="전체">
										<small>전체</small>
									</label>
								</div>
								<c:forEach var="causeList" items="${causeList }">
									<label for="checkbox_${causeList.cause_name}">
										<input type="checkbox" class="form-check-input" nm="${causeList.cause_name}"
											id="checkbox_${causeList.cause_name}" name="causeMultiCheck" value="${causeList.cause_name }" />
										<small>${causeList.cause_name}</small>
									</label>
								</c:forEach>
							</div>
							<div style="margin-top : 5%; font-size:12px;"> 
        						<input type="button" class="form-control btn btn-primary " style=";"value="확인" onclick="selectCauseList()"/> 
    						</div> 
						</div>
					</div>
					<div class="row mt-4 mb-6 card-footer" style=margin-left:0.05rem></div>
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
			<div class="col-3 mb-6">
				<h5>조회 결과</h5>
			</div>
			<div class="col-3" align="right">
				<button type="button" class="btn btn-outline-primary btn-addel mb-6" id="btnExcel">엑셀 다운로드</button>		
			</div>
		</div>
	</div>
	<div class="row">
			<div class="col-12" >
				<div class="card-header">
					<div class="card-body">
						<table class="table table-bordered mt-3">
							<colgroup>
								<col style="width:170px;"> 
								<col style="width:160px;"> 
								<col style="width:160px;"> 
								<col style="width:110px;"> 
								<col style="width:140px;">
								<col style="width:150px;">
								<col style="width:150px;">
								<col style="width:110px;"> 
								<col style="width:150px;"> 
							</colgroup>
							<thead>
								<tr class="table-primary">
									<th scope="col">시간</th>
									<th scope="col">MSISDN</th>
									<th scope="col">IMSI</th>
									<th scope="col">Username</th>
									<th scope="col">인증 결과</th>
									<th scope="col">IP Address</th>
									<th scope="col">NAS IP</th>
									<th scope="col">NAS Port</th>
									<th scope="col">결과 원인</th>
								</tr>
							</thead>
						<tbody >
							<c:choose>
								<c:when test="${fn:length(athntHistoryList) > 0 }">
									<c:forEach var="item" items="${athntHistoryList }" varStatus="idx">		
										<tr id="list_${item.id }">	
											<td>${item.authdate}</td>
											<td>${item.msisdn}</td>
											<td>${item.gpp_imsi}</td>
											<td>${item.userName}</td>
											<td>${item.reply}</td>
											<td>${item.framed_ip_address}</td>
											<td>${item.nas_ip_address}</td>
											<td>${item.nas_port_no}</td>
											<td>${item.cause_name }</td>
										</tr>
									</c:forEach>
								</c:when>
								<c:otherwise>
                                	<tr>
                                    	<td colspan="9">
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
	<!-- footer -->
	<%@ include file="/decorators/footer.jsp"%>
	<!-- //footer -->	

</body>
</html>