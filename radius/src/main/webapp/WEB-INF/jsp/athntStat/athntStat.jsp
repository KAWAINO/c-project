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

//statType 드롭다운
var statType = false;

function selectStatList() {
	
    var div = document.getElementById("statListDiv");
    var search = document.getElementById("searchStatType");
    var checkboxes = document.getElementsByName('typeMultiCheck');

    var chkArray = [];
    $('input:checkbox[name="typeMultiCheck"]:checked').each(function() {
        chkArray.push("'" + this.getAttribute('nm') + "'" );
    });
    var List = chkArray.join(",");
    /* console.log("selectStatList ::"  + List); */
	
    if (chkArray.length > 0) {
        if(chkArray.length === checkboxes.length) {
        	search.value = '전체 항목';
        } else {
        	search.value = chkArray.join(",");
        }      
    } else {
    	search.value = '전체 항목';
    }
    if (!statType) {
        div.style.display = "block";
        statType = true;
    } else {
        div.style.display = "none";
        statType = false;
    }
};

//replyList 드롭다운
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
    /* console.log("selectReplyList ::"  + List); */

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

// causeList 드롭다운
var cause = false;

function selectCauseList() {
    var div = document.getElementById("causeDiv");
    var search = document.getElementById("searchCause");
    var checkboxes = document.getElementsByName('causeMultiCheck');

    var chkArray = [];
    $('input:checkbox[name=causeMultiCheck]:checked').each(function() {
        chkArray.push( "'" + this.getAttribute('nm') + "'" );
    });
    var List = chkArray.join(",");
    /* console.log("selectCauseList ::"  + List); */

    if (chkArray.length > 0) {
        if(chkArray.length === checkboxes.length) {
        	search.value = '전체 항목';
        } else {
        	search.value = chkArray.join(",");
        }
    } else {
    	search.value = '전체 항목';
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
	
	// typeAllChk  
	$("#typeAllChk").click(function() {
		
		if($(this).is(":checked")) {
			$("input[name=typeMultiCheck]").prop("checked", true);
		} else {
			$("input[name=typeMultiCheck]").prop("checked", false);
		}
	});

	$("input[name=typeMultiCheck]").click(function() {
		
		var all = $("input[name=typeMultiCheck]").length;
		var chked = $("input[name=typeMultiCheck]:checked").length;
		var chkedCount = 0;

		if(all == chked) {
			$("#typeAllChk").prop("checked", true);
		} else {
			$("#typeAllChk").prop("checked", false); 
		}
	});
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
	
	// 검색 시간 일, 주, 월 일경우 시간,분 disabled
    var StartHour = $('#searchStartHour').val();
    var StartMin = $('#searchStartMin').val();
    var EndHour = $('#searchEndHour').val();
    var EndMin = $('#searchEndMin').val();
    var searchUnits = $('#searchUnits').val();

	if (searchUnits == 'day' || searchUnits == 'week' || searchUnits == 'month') {
	  
	  	StartHour = $('#searchStartHour').val();
	    StartMin = $('#searchStartMin').val();
	    EndHour = $('#searchEndHour').val();
	    EndMin = $('#searchEndMin').val();
    
    $('#searchStartHour, #searchStartMin, #searchEndHour, #searchEndMin').prop('disabled', true).val('00');
    
	} else {
	
	    $('#searchStartHour').val(StartHour);
	    $('#searchStartMin').val(StartMin);
	    $('#searchEndHour').val(EndHour);
	    $('#searchEndMin').val(EndMin);
	    $('#searchStartHour, #searchStartMin, #searchEndHour, #searchEndMin').prop('disabled', false);
	}
	
	$('#searchUnits').change(function() {
		
	    if ($(this).val() == 'day' || $(this).val() == 'week' || $(this).val() == 'month') {
	    	
	        StartHour = $('#searchStartHour').val();
	        StartMin = $('#searchStartMin').val();
	        EndHour = $('#searchEndHour').val();
	        EndMin = $('#searchEndMin').val();
	        
	       $('#searchStartHour, #searchStartMin, #searchEndHour, #searchEndMin').prop('disabled', true).val('00');
	        
	    } else {
	        $('#searchStartHour').val(StartHour);
	        $('#searchStartMin').val(StartMin);
	        $('#searchEndHour').val(EndHour);
	        $('#searchEndMin').val(EndMin);
	        $('#searchStartHour, #searchStartMin, #searchEndHour, #searchEndMin').prop('disabled', false);
	    }
	});
	
	// statType 체크상태
	var dataArray = document.getElementById("searchStatType").value
    /* console.log(dataArray); */

    if(dataArray !== 'null'){
       var checkboxes = document.getElementsByName('typeMultiCheck');
       var checkedCount = 0;

       for(var i=0; i < checkboxes.length; i++) {
       	   if(dataArray.includes(checkboxes[i].value) || dataArray.includes() || dataArray.includes('전체 항목')) {
               checkboxes[i].checked = true;
               checkedCount++;
           }
       	   /* console.log(dataArray); */
       	   if(dataArray.includes('전체 항목') || dataArray.includes('인증결과', 'NAS IP', '결과원인')) {
	   	   	   $("#typeAllChk").prop("checked", true);
	   	   }
       }
       if(checkedCount === checkboxes.length) {
           document.getElementById("searchStatType").value = '전체 항목';
       } 
    };
	
	// replyList 체크상태
	var dataArray = document.getElementById("searchReply").value
    /* console.log(dataArray); */
    if(dataArray !== 'null'){
       var checkboxes = document.getElementsByName('replyMultiCheck');
       var checkedCount = 0;

       for(var i=0; i < checkboxes.length; i++) {
       	   if(dataArray.includes(checkboxes[i].value) || dataArray.includes() || dataArray.includes('전체 항목')) {
               checkboxes[i].checked = true;
               checkedCount++;
           }
       	   if(dataArray.includes('전체 항목')) {
 	   	   	   $("#replyAllChk").prop("checked", true);
 	   	   }
       }
       if(checkedCount === checkboxes.length) {
           document.getElementById("searchReply").value = '전체 항목';
       }  
    };
	
	// causeList 체크상태
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
       	   if(dataArray.includes('전체 항목')) {
	   	   	   $("#causeAllChk").prop("checked", true);
	   	   }
       }
       if(checkedCount === checkboxes.length) {
           document.getElementById("searchCause").value = '전체 항목';
       } 
    }; 
	   
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
        $("#chkedStatType").val("true");
        $("#chkedReplyList").val("true");
        $("#chkedNasIpList").val("true");
        $("#chkedCauseList").val("true");
		$("#searchAthntStatForm").attr("action", "/athntStat/athntStat.do");
		$("#searchAthntStatForm").submit();	
		
		/* var cnt = ${totalCnt}
	 	if(cnt === 0){
	 		alert("선택하신 기간에는 데이터가 존재하지 않습니다. 검색기간 변경 후 다시 조회해주세요.");
	 	}  */	
	});

	//초기화
	$("#btnReset").bind("click", function() {
		$("#searchNasIp").val("");	
		$("#startDate").val("${timeDefaultStart}");
        $("#endDate").val("${timeDefaultEnd}");
        $('#searchStatType').val("전체 항목");
        $('#searchReply').val("전체 항목");
        $('#searchCause').val("전체 항목");
        $('input:checkbox[name=typeAllChk]').prop("checked", true);
        $('input:checkbox[name=replyAllChk]').prop("checked", true);
        $('input:checkbox[name=causeAllChk]').prop("checked", true);
        $('input:checkbox[name=typeMultiCheck]').prop("checked", true);
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
	$("#searchAthntStatForm").attr("action", "/athntStat/athntStat.do");
	$("#searchAthntStatForm").submit();
}

//엑셀 다운로드
function goExcelDown() {
	$("#searchAthntStatForm").attr("action", "/athntStat/athntStatExcelDownload.do");
    $("#searchAthntStatForm").submit();
}

</script>
</head>

<body>

<!-- header -->
<%@ include file="/decorators/header.jsp"%>
<!-- //header -->

<div class="container min-vh-100">
	<ol class="breadcrumb mt-4 py-3 fs-5">
		<li class="breadcrumb-item active">통계</li>
		<li class="breadcrumb-item active">인증 통계</li>
	</ol>
	<!-- 검색 -->
	<div class="card border-primary mb-4">
		<div class="card-header mb-3">상세검색</div>
		<div class="card-body">
			<form name="searchAthntStatForm" id=searchAthntStatForm method="post">
				<input type="hidden" id="pageno" name="pageno" value="${athntStatVo.pageno }"/>
				<input type="hidden" id="tableName" name="tableName" value="${athntStatVo.tableName}">
				<input type="hidden" id="searchChecked" name="searchChecked" value="false">				
                <input type="hidden" id="chkedReplyList" name="chkedReplyList" value="true">
                <input type="hidden" id="chkedNasIpList" name="chkedNasIpList" value="true">
                <input type="hidden" id="chkedCauseList" name="chkedCauseList" value="true">
				<div class="row">	
					<label class="col-form-label col-1 mb-1 offset-12" for="searchUnits" >단위</label>		
					<div class="col-15-9" >
						<select class="form-select-check" id="searchUnits" size="1" name="searchUnits" >
							<option value="min" ${'min' eq athntStatVo.searchUnits ? 'selected' : ''}>5분</option>
							<option value="hour" ${'hour' eq athntStatVo.searchUnits ? 'selected' : ''}>시간</option>
							<option value="day" ${'day' eq athntStatVo.searchUnits ? 'selected' : ''}>일간</option>
							<option value="week" ${'week' eq athntStatVo.searchUnits ? 'selected' : ''}>주간</option>
							<option value="month" ${'month' eq athntStatVo.searchUnits ? 'selected' : ''}>월간</option>
						</select>	
					</div>
					<label class="col-form-label col-1 mb-1 offset-12">통계 항목</label>		
					<div class="col-15-7" >
						<input class="form-select-check " type="text" id="searchStatType" name="searchStatType" readonly="readonly"
							onclick="selectStatList()" value="${athntStatVo.searchStatType }">
						<div id="statListDiv" class="multi form-control" style="display: none; position: absolute; width: 13.5%;"> 
							<div class="mt-1 col-12">	
								<div class="mb-2">
									<label>
										<input type="checkbox" class="form-check-input" id="typeAllChk" name="typeAllChk" value="전체">
										<small>전체</small>
									</label>
								</div>
								<div>					
									<label for="typeReply">
										<input type="checkbox" class="form-check-input" id="typeReply" name="typeMultiCheck" nm="인증결과" value="인증결과">
										<small>인증 결과</small>
									</label>	
								</div>
								<div>
									<label for="typeNasIp">
										<input type="checkbox" class="form-check-input" id="typeNasIp" name="typeMultiCheck" nm="NAS IP" value="NAS IP">
										<small>NAS IP</small>
									</label>
								</div>
								<div>
									<label for="typeCause">
										<input type="checkbox" class="form-check-input" id="typeCause" name="typeMultiCheck" nm="결과원인" value="결과원인">
										<small>결과 원인</small>
									</label>		
								</div>
							</div>
							<div style="margin-top : 5%; font-size:12px;"> 
        						<input type="button" class="form-control btn btn-primary " style=";"value="확인" onclick="selectStatList()"> 
    						</div> 
						</div>
					</div>
					<label class="col-form-label col-1 mb-1 offset-12">인증 결과</label>		
					<div class="col-15-7" >
						<input class="form-select-check" type="text" id="searchReply" name="searchReply" readonly="readonly"
							onclick="selectReplyList()" value="${athntStatVo.searchReply != null ? athntStatVo.searchReply : '전체 항목'}">
						<div id="replyDiv" class="multi form-control" style="display: none; position: absolute; width: 13.5%;"> 
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
											id="checkbox_${replyList.reply}" name="replyMultiCheck" value="${replyList.reply }" />
										<small>${replyList.reply}</small>
									</label>
								</c:forEach>
							</div>
							<div style="margin-top : 5%; font-size:12px;"> 
        						<input type="button" class="form-control btn btn-primary " id="typeChk" style=";"value="확인" onclick="selectReplyList()"/> 
    						</div> 
						</div>						
					</div>
					<label class="col-form-label col-1 mb-1 offset-12" for="" >NAS IP</label>	
					<div class="col-15-8" >
						<input type="text" class="form-control" id="searchNasIp" name="searchNasIp" value="${athntStatVo.searchNasIp}">
					</div>
				</div>
				<div class="row mt-4">
					<label class="col-form-label col-1 mb-1 offset-12" for="" >검색 기간</label>		
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
                                        <option value="${setHour}" ${setHour eq athntStatVo.searchStartHour ? 'selected' : ''}>
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
                                        <option value="${setMin}" ${setMin eq athntStatVo.searchStartMin ? 'selected' : ''}>
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
                                        <option value="${setHour}" ${setHour eq athntStatVo.searchEndHour ? 'selected' : ''}>
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
                                        <option value="${setMin}" ${setMin eq athntStatVo.searchEndMin ? 'selected' : ''}>
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
							onclick="selectCauseList()" value="${athntStatVo.searchCause != null ? athntStatVo.searchCause : '전체 항목'}">
						<div id="causeDiv" class="multi form-control" style="display: none; position: absolute; width: 13.5%;"> 
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
							<button type="button" class="btn btn-primary btn-search mb-2 " id="btnSearch" onclick="addTable()" >조회</button>
							<button type="button" class="btn btn-outline-primary btn-search mb-2 " id="btnReset">초기화</button>
						</div>	
				</div>
			</form>
		</div>
	</div>
	<div class="container px-0">
		<div class="row justify-content-between">
			<div class="col-3 mb-6 scroll-x">
				<h5>조회 결과</h5>
			</div>
			<div class="col-3" align="right">
				<button type="button" class="btn btn-outline-primary btn-addel mb-6" id="btnExcel">엑셀 다운로드</button>		
			</div>
		</div>
	</div>
	<div class="row">
			<div class="col-12 left-table" >
				<div class="card-header scroll-x">
					<div class="card-body">
						<table class="table table-bordered table-hover mt-3" >
							 <%-- <colgroup>
								<col style="auto;">
								<c:if test="${athntStatVo.chkedReplyList }" >
								<col style="auto;">
								</c:if>		
								<c:if test="${athntStatVo.chkedNasIpList }" >wi
								<col style="auto;">
								</c:if>
								<c:if test="${athntStatVo.chkedCauseList }" >
								<col style="auto;">
								</c:if>		
								<col style="auto;">
								<col style="auto;">
								<col style="auto;">					
							</colgroup>  --%>
							<thead >					
								<tr class="table-primary" >
									<th scope="col">시간</th>	

									<c:if test="${athntStatVo.chkedReplyList }">
										<th scope="col" >인증 결과</th>
									</c:if>
									<c:if test="${athntStatVo.chkedNasIpList }">
										<th scope="col">NAS IP</th>
									</c:if>
									<c:if test="${athntStatVo.chkedCauseList }">
										<th scope="col">결과 원인</th>
									</c:if> 

									<th scope="col">인증 시도수</th>
									<th scope="col">성공수</th>
									<th scope="col">실패수</th>
								</tr>
							</thead>
							<tbody >
								<c:choose>
									<c:when test="${fn:length(statList) > 0 }">
										<c:forEach var="item" items="${statList }" varStatus="idx">		
											<tr >	
												<td >${item.stat_time }</td>
												
												<c:if test="${athntStatVo.chkedReplyList }">
													<td >${item.reply }</td>
												</c:if>
												<c:if test="${athntStatVo.chkedNasIpList }">
													<td >${item.nas_ip_address}</td>
												</c:if>
												<c:if test="${athntStatVo.chkedCauseList }"> 
													<td >${item.cause_name }</td>
												</c:if> 
												
												<td>${item.auth_cnt}</td>
												<td>${item.success_cnt}</td>
												<td>${item.fail_cnt}</td>
											</tr>
									</c:forEach>
								</c:when> 
								<c:otherwise>
                                	<tr >
	                                	<td colspan="7" >       
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