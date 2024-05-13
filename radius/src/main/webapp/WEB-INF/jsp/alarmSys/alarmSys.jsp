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

/* var timer = null;

function getAlarmSysList(){  
	   	$.ajax({
	   	    url: "/alarmSys/getAlarmSysList.ajax",
	   		type: "POST", 
	   		dataType: "JSON",
	   		data: {"data" : "alarmSysList"},
	   	    chech: false,   	    
	   	    success: function(data){
	   	    	
		   		if(data.length == 0){
		   			$('#"newTbody"').append("<tr><td colspan='7'>데이터가 없습니다.</td></tr>	");	
		   		} else {	   			

		   			var result = "";
		   			
		   			for(i = 0; i < data.length; i++){
		   				
		   				result += "<tr>";
		   				result += "<td>" +
							'<input type="checkbox" class="form-check-input" id="rowChk" name="rowChk" nm='+data[i].idx+' value="'+data[i].idx+'">' +
							"</td>";
								 
		   				result += "<td>" + data[i].evt_time + "</td>";
		   				result += "<td>" + data[i].aId + "</td>";
		   				result += "<td>" + data[i].severity + "</td>";
		   				result += "<td>" + data[i].alarmLocation + "</td>";
		   				result += "<td>" + data[i].alarm_str + "</td>";
		   				result += "<td>" + data[i].add_str + "</td>";
		   				result += "<td>" + data[i].first_time + "</td>";
		   				result += "</tr>";
		   				
		   			}
		   			$("#newTbody").html(result);
	   			}
	   	    }
	   	});
	   	
	   	if(timer = null){
	     	timer = setTimeout("getAlarmSysList()",5000);	  
		}else{
			clearTimeout(timer);
			timer = setTimeout("getAlarmSysList()",5000);	 
		}
}  */

	$(function(){
		var chkList = document.getElementsByName("rowChk");
		var rowCnt = chkList.length;
		
		$("input:checkbox[name='allChk']").click(function(){
			var chkArr = $("input[name='rowChk']");
			for(var i = 0; i < chkArr.length; i++){
				chkArr[i].checked = this.checked;
			}
		});
		
		$("input[name='rowChk']").click(function(){	
			if($("input[name='rowChk']:checked").length == rowCnt) {
				$("input[name='allChk']")[0].checked = true;
			} else {
				$("input[name='allChk']")[0].checked = false;
			}
		});
	});
	
	function delAlarmSysList(){
		
		var chk = confirm("선택 알람을정말 해제하시겠습니까?");
		if(chk == true) {

			var idxArr = [];
			/* var idxArr = $('#rowChk').val(); */
			
			$("input[name='rowChk']:checked").each(function(){
				idxArr.push(this.getAttribute('nm'));
			});
			
			var List = idxArr.join(",");
			
			if(idxArr.length == ""){
				alert("선택된 알람이 없습니다. 확인 후 다시 시도해주세요.");
			} else {
				
				$.ajax({
					url : "/alarmSys/deleteAlarmSysList.ajax",
					type : "POST",
					dataType : "JSON",
					data : { "idx" : idxArr },
					error	: function (e) { alert("오류가 발생하였습니다. 관리자에게 문의 바랍니다."); },
					success : function(data) {			
						if(data.result == "1") {					
							alert("정상적으로 해제되었습니다.");
							document.location.reload();
						} else {
							alert("오류가 발생하였습니다. 관리자에게 문의 바랍니다.");
						}
					}
				});		
			}
		} else {
			document.location.reload();
		}
	}
	
	
$(document).ready(function() { 

	/* getAlarmSysList(); */

	// 검색
	$("#btnSearch").bind("click", function() {
    	$("#searchAlarmSysForm").attr("action", "/alarmSys/alarmSys.do");
       	$("#searchAlarmSysForm").submit();
    });
	
  	//초기화
    $("#btnReset").bind("click", function() {
    	$("#searchAId").val("");
    	$("#searchSeverity").val("");
    	$("#searchLocA").val("");
    	$("#searchAlarmStr").val("");
    	$("#searchAddStr").val("");
    });
  
  	// 삭제
  	$("#delAlarm").bind("click", function() {
  		delAlarmSysList();
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

//엑셀 다운로드
function goExcelDown() {
	$("#searchAlarmSysForm").attr("action", "/alarmSys/alarmSysExcelDownload.do");
    $("#searchAlarmSysForm").submit();
}

</script>
</head>
<body >

<!-- header -->
<%@ include file="/decorators/header.jsp"%>
<!-- //header -->

<div class="container min-vh-100">
	<ol class="breadcrumb mt-4 py-3 fs-5">
		<li class="breadcrumb-item active">상태</li>
		<li class="breadcrumb-item active">알람 현황</li>
	</ol>
	<!-- 검색 -->
	<div class="card border-primary mb-4">
		<div class="card-header">상세검색</div>
		<div class="card-body" >
			<form name="searchAlarmSysForm" id="searchAlarmSysForm" method="post">
				<input type="hidden" id="pageno" name="pageno" value="${alarmSysVo.pageno }"/>
				<div class="row mt-4">				
					<label class="col-form-label col-sys-label-4">알람 아이디</label>	
					<div class="col-sys-input-8" >
						<select class="form-select" id="searchAId" name="searchAId">
							<option value="">전체</option>
							<c:forEach var="sys" items="${alarmIdList }">
								<option value="${sys.aId }" <c:if test="${sys.aId eq alarmSysVo.searchAId }">selected</c:if>>
									<c:out value="${sys.aId }" />
								</option>
							</c:forEach>
						</select>
					</div>
					<label class="col-form-label col-sys-label-3 ">심각도</label>	
					<div class="col-sys-input-9" >
						<select class="form-select" id="searchSeverity" name="searchSeverity">
							<option value="">전체</option>
							<option value="C">CRITICAL</option>
							<option value="M">MAJOR</option>
							<option value="N">MINOR</option>
						</select>
					</div>
					<label class="col-form-label col-sys-label-3">알람위치</label>	
					<div class="col-sys-input-10" >
						<input type="text" class="form-control" id="searchLocA" name="searchLocA" value="${alarmSysVo.searchLocA }">
					</div>
					<label class="col-form-label col-sys-label-3">알람메세지</label>	
					<div class="col-sys-input-11" >
						<input type="text" class="form-control" id="searchAlarmStr" name="searchAlarmStr" value="${alarmSysVo.searchAlarmStr }">
					</div>
					<label class="col-form-label col-sys-label-3">알람원인</label>	
					<div class="col-sys-input-10" >
						<input type="text" class="form-control" id="searchAddStr" name="searchAddStr" value="${alarmSysVo.searchAddStr }">
					</div>
					<div class="row mt-5 mb-6 card-footer" style=margin-left:0.05rem></div>
					<div class="search-footer mb-1" align="center">
						<button type="button" class="btn btn-primary btn-search mb-2 " id="btnSearch" >조회</button>
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
				<button type="button" class="btn btn-primary btn-addel mb-6" id="delAlarm" >선택 알람 해제</button>		
				<button type="button" class="btn btn-outline-primary btn-addel mb-6" id="btnExcel">엑셀 다운로드</button>		
			</div>
		</div>
	</div>
	<div class="row mt-1">
		<div class="col-12" >
			<div class="card-header left-table mb-3">
				<div class=" scroll-x" >
					<table class="table table-hover table-bordered mt-3">
						<colgroup>
							<col style="width:80px;">
							<col style="width:170px;">
							<col style="width:140px;">
							<col style="width:120px;">
							<col style="width:500px;">
							<col style="width:170px;">
							<col style="width:420px;">
							<col style="width:170px;">							
						</colgroup>
						<thead>
							<tr class="table-primary scroll-x">
								<th scope="col"><input type="checkbox" class="form-check-input" id="allChk" name="allChk"></th>
								<th scope="col">발생 시간</th>
								<th scope="col">알람 아이디</th>
								<th scope="col">심각도</th>
								<th scope="col">알람 위치</th>
								<th scope="col">알람 메세지</th>
								<th scope="col">알람 원인</th>
								<th scope="col">최초 발생시간</th>
							</tr>
						</thead>
						<tbody  id="newTbody">
							<c:choose>
								<c:when test="${fn:length(alarmSysList) > 0 }">
									<c:forEach var="item" items="${alarmSysList }" >		
										<tr>	
											<td>
												<input type="checkbox" class="form-check-input" id="rowChk" name="rowChk" 
													nm="${item.idx }" value="<c:out value='${item.idx }'/>">
											</td>
											<td>${item.evt_time }</td>
											<td>${item.aId }</td>
											<td id="sev">
												<input type="hidden" class="form-check-input" id="sev" name="sev" nm="${item.severity }" value="${item.severity }">
												<c:set var="sys" value="${item.severity }"/>
												<c:choose>
													<c:when test="${sys == 'C' }" >CRITICAL</c:when>
													<c:when test="${sys == 'M' }">MAJOR</c:when>
													<c:when test="${sys == 'N' }">MINOR</c:when>
												</c:choose>
											</td>
											<td>${item.alarmLocation }</td>
											<td>${item.alarm_str }</td>
											<td>${item.add_str }</td>
											<td>${item.first_time }</td>
										</tr>
									</c:forEach>
								</c:when>
								<c:otherwise>
	                                <tr >
		                                <td colspan="8" >       
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