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
	
	//날짜 선택
    $("#startDate, #endDate").datepicker({
        format: 'yyyy-mm-dd',
        autoclose: true
    });

	// 검색
    $("#btnSearch").bind("click", function() {
    	
    	// 발생시간 날짜 설정
        var start_date = document.getElementById("startDate").value + document.getElementById("searchStartHour").value + document.getElementById("searchStartMin").value + "00";
        var end_date = document.getElementById("endDate").value + document.getElementById("searchEndHour").value + document.getElementById("searchEndMin").value + "59";
        
     	// 날짜 검색 조건
        if (start_date >= end_date) {
        	alert("검색 기간 설정이 잘못되었습니다. 시작날짜가 종료날짜보다 늦습니다.");
            return;
        }
    	
        $("#searchChked").val("true");
    	$("#searchAlarmHisForm").attr("action", "/alarmHis/alarmHis.do");
        $("#searchAlarmHisForm").submit();
    });
	
  	//초기화
    $("#btnReset").bind("click", function() {
    	
    	$("#searchAId").val("");
    	$("#searchSeverity").val("");
    	$("#searchLocA").val("");
    	$("#searchAlarmStr").val("");
    	$("#searchAddStr").val("");
    	$("#startDate").val("${timeDefaultStart}");
        $("#endDate").val("${timeDefaultEnd}");
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
	$("#searchSuserForm").attr("action", "/suser/suser.do");
    $("#searchSuserForm").submit();	
}

//엑셀 다운로드
function goExcelDown() {
	$("#searchAlarmHisForm").attr("action", "/alarmHis/alarmHisExcelDownload.do");
    $("#searchAlarmHisForm").submit();
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
		<li class="breadcrumb-item active">알람 이력</li>
	</ol>
	<!-- 검색 -->
	<div class="card border-primary mb-4">
		<div class="card-header mb-3">상세검색</div>
		<div class="card-body" >
			<form name="searchAlarmHisForm" id="searchAlarmHisForm" method="post">
				<input type="hidden" id="pageno" name="pageno" value="${alarmHisVo.pageno }"/>
				
				<input type="hidden" id="searchChked" name="searchChked" value="false">	
				<div class="row mt-1">				
					<label class="col-form-label col-sys-label-6 ">발생 시간</label>		
					<div class="col-sys-input-15">
						<input type="text" class="form-control datePicker" id="startDate" name="startDate" value="${timeDefaultStart}"/>
					</div>
					<div class="col-sys-input-1">						
						<select class="form-select" id="searchStartHour" name="searchStartHour" >					
							<!-- <option value="" selected disabled hidden>00</option> -->
								<c:forEach begin="0" end="23" var="hour">
                                	<c:set var="setHour" value="${hour}" />
                                    	<c:if test="${hour < 10}">
                                        	<c:set var="setHour" value='0${hour}' />
                                        </c:if>
                                        <option value="${setHour}" ${setHour eq alarmHisVo.searchStartHour ? 'selected' : ''}>
                                        	<c:out value="${setHour}" />
                                        </option>
                                </c:forEach>
						</select>
					</div>
					<div class="col-sys-label-1">
						<label class="col-form-label" >시</label>
					</div>
					<div class="col-sys-input-2">						
						<select class="form-select" id="searchStartMin" name="searchStartMin">					
							<!-- <option value="" selected disabled hidden>00</option> -->
								<c:forEach begin="0" end="59" var="min">
                                	<c:set var="setMin" value="${min}" />
                                   		<c:if test="${min < 10}">
                                        	<c:set var="setMin" value='0${min}' />
                                        </c:if>
                                        <option value="${setMin}" ${setMin eq alarmHisVo.searchStartMin ? 'selected' : ''}>
                                        	<c:out value="${setMin}" />
                                        </option>
								</c:forEach>
						</select>
					</div>
					<div class="col-sys-label-1">
						<label class="col-form-label" >분</label>
					</div>
					<div class="col-sys-label-0-0">
						<label class="col-form-label" >~</label>
					</div>
					<div class="col-sys-input-0">
						<input type="text" class="form-control datePicker" id="endDate" name="endDate" value="${timeDefaultEnd}"/>
					</div>
					<div class="col-sys-input-1">						
						<select class="form-select" id="searchEndHour" name="searchEndHour">					
							<!-- <option value="" selected disabled hidden>00</option> -->
								<c:forEach begin="0" end="23" var="hour">
                                	<c:set var="setHour" value="${hour}" />
                                    	<c:if test="${hour < 10}">
                                        	<c:set var="setHour" value='0${hour}' />
                                        </c:if>
                                        <option value="${setHour}" ${setHour eq alarmHisVo.searchEndHour ? 'selected' : ''}>
                                        	<c:out value="${setHour}" />
                                       	</option>
                                </c:forEach>
						</select>
					</div>
					<div class="col-sys-label-1">
						<label class="col-form-label" >시</label>
					</div>
					<div class="col-sys-input-2">						
						<select class="form-select" id="searchEndMin" name="searchEndMin" >					
							<!-- <option value="" selected disabled hidden>00</option> -->
								<c:forEach begin="0" end="59" var="min">
                                	<c:set var="setMin" value="${min}" />
                                    	<c:if test="${min < 10}">
                                        	<c:set var="setMin" value='0${min}' />
                                        </c:if>
                                        <option value="${setMin}" ${setMin eq alarmHisVo.searchEndMin ? 'selected' : ''}>
                                        	<c:out value="${setMin}" />
                                        </option>
                                </c:forEach>
						</select>
					</div>	
					<div class="col-sys-label-1">
						<label class="col-form-label" >분</label>
					</div>
				</div>	
				<div class="row mt-4">
					<label class="col-form-label col-sys-label-4">알람 아이디</label>	
					<div class="col-sys-input-8" >
						<select class="form-select" id="searchAId" name="searchAId">
							<option value="">전체</option>
							<c:forEach var="sys" items="${alarmIdList }">
								<option value="${sys.aId }" <c:if test="${sys.aId eq alarmHisVo.searchAId }">selected</c:if>>
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
							<%-- <c:forEach var="his" items="${severityList }">
								<option value="${his.severity }" <c:if test="${his.severity eq alarmHisVo.searchSeverity }">selected</c:if>>
									<c:out value="${his.severity }" />
								</option>
							</c:forEach> --%>
						</select>
					</div>
					<label class="col-form-label col-sys-label-3">알람위치</label>	
					<div class="col-sys-input-10" >
						<input type="text" class="form-control" id="searchLocA" name="searchLocA" value="${alarmHisVo.searchLocA }">
					</div>
					<label class="col-form-label col-sys-label-3">알람메세지</label>	
					<div class="col-sys-input-11" >
						<input type="text" class="form-control" id="searchAlarmStr" name="searchAlarmStr" value="${alarmHisVo.searchAlarmStr }">
					</div>
					<label class="col-form-label col-sys-label-3">알람원인</label>	
					<div class="col-sys-input-10" >
						<input type="text" class="form-control" id="searchAddStr" name="searchAddStr" value="${alarmHisVo.searchAddStr }">
					</div>
				</div>
				<div class="row mt-4 mb-6 card-footer" style=margin-left:0.08rem></div>
					<div class="search-footer mb-1" align="center">
						<button type="button" class="btn btn-primary btn-search mb-2 " id="btnSearch" >조회</button>
						<button type="button" class="btn btn-outline-primary btn-search mb-2 " id="btnReset">초기화</button>
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
	<div class="row mt-1">
		<div class="col-12" >
			<div class="card-header left-table mb-3">
				<div class=" scroll-x" >
					<table class="table table-hover table-bordered mt-3">
						<colgroup>
							<col style="width:170px;">
							<col style="width:140px;">
							<col style="width:120px;">
							<col style="width:500px;">
							<col style="width:170px;">
							<col style="width:420px;">
							<col style="width:170px;">							
							<col style="width:170px;">							
							<col style="width:150px;">							
						</colgroup>
						<thead>
							<tr class="table-primary scroll-x">
								<th scope="col">발생 시간</th>
								<th scope="col">알람 아이디</th>
								<th scope="col">심각도</th>
								<th scope="col">알람 위치</th>
								<th scope="col">알람 메세지</th>
								<th scope="col">알람 원인</th>
								<th scope="col">최초 발생시간</th>
								<th scope="col">해제 시간</th>
								<th scope="col">사용자</th>
							</tr>
						</thead>
						<tbody>
							<c:choose>
								<c:when test="${fn:length(alarmHisList) > 0 }">
									<c:forEach var="item" items="${alarmHisList }" >		
										<tr>	
											<td>${item.evt_time }</td>
											<td>${item.aId }</td>
											<td>
												<c:set var="his" value="${item.severity }"/>
												<c:choose>
													<c:when test="${his == 'C' }">CRITICAL</c:when>
													<c:when test="${his == 'M' }">MAJOR</c:when>
													<c:when test="${his == 'N' }">MINOR</c:when>
												</c:choose>
											</td>
											<td>${item.alarmLocation }</td>
											<td>${item.alarm_str }</td>
											<td>${item.add_str }</td>
											<td>${item.first_time }</td>
											<td>${item.clr_time }</td>
											<td>${item.clr_user }</td>
										</tr>
									</c:forEach>
								</c:when>
								<c:otherwise>
	                                <tr >
		                                <td colspan="9" >       
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
</div>


<!-- footer -->
<%@ include file="/decorators/footer.jsp"%>
<!-- //footer -->					

</body>

</html>