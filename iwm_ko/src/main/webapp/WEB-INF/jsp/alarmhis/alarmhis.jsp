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
<script type="text/javascript" src="/web/js/jquery-ui-1.12.1/jquery-ui.js"></script>
<script type="text/javascript" >

	let downloadExcelMessage = '<spring:message code="confirm.downloadExcel" />';
	let downloadCanceledMessage = '<spring:message code="confirm.downloadCanceled" />';

$(document).ready(function(){

	$("#btnSearch").bind("click", function() {
    	var startDate = $('#startDate').val() + $('#startHour').val() + $('#startMin').val() + '00';
    	var endDate = $('#endDate').val() + $('#endHour').val() + $('#endMin').val() + '59';

    	// 날짜 검색 조건
        if (startDate >= endDate) {
        	alert('<spring:message code="confirm.wrong.greeterThanEndTime" />');
            return;
        }

    	$('#pageno').val(1);
    	$("#searchChked").val("true");
    	$("#searchAlarmHisForm").attr("action", "/alarmhis/alarmhis.do");
        $("#searchAlarmHisForm").submit();
	});
	
	// excel
    $("#btnExcel").bind("click", function() {
    	goExcelDown();
    });
});

//paging
function goPage(pageno) {
	$("#pageno").val(pageno);
	$("#searchChked").val("true");
	$("#searchAlarmHisForm").attr("action", "/alarmhis/alarmhis.do");
    $("#searchAlarmHisForm").submit();	
}

//엑셀 다운로드
function goExcelDown() {
	
	var msg = "";
	
	msg = prompt(downloadExcelMessage);

	if(!msg) {
		alert(downloadCanceledMessage);
		return;
	}
	
	$("input[name ='excelMsg']").val(msg);
	
	$("#searchAlarmHisForm").attr("action", "/alarmhis/alarmHisExcelDownload.do");
    $("#searchAlarmHisForm").submit();
}

</script>
</head>
<body>

	<jsp:include page="../header.jsp" flush="false"/>
	
	<section class="contents-area">
		<header>
			<i class="icon-list"><span>icon</span></i>
			<h2><spring:message code="alarmhis.alarmHistory" /></h2>
		</header>
		<form id="searchAlarmHisForm" name="searchAlarmHisForm" method="post">
			<input type="hidden" id="pageno" name="pageno" value="${alarmHisVo.pageno }"/>
			<input type="hidden" id="searchChked" name="searchChked" value="false">
			<input type="hidden" id="excelMsg" name="excelMsg" value="">
			<div class="search-area">
				<span><strong><spring:message code="search.startTime2" /></strong></span>
				<input type="text" class="inp-date inp-date-picker w150" id="startDate" name="startDate" value="${defaultStartDate}" autocomplete="off"/>
				<select class="form-select" id="startHour" name="startHour" >					
					<c:forEach begin="0" end="23" var="hour">
                    	<c:set var="setHour" value="${hour}" />
                        	<c:if test="${hour < 10}">
                            	<c:set var="setHour" value='0${hour}' />
                            </c:if>
                            <option value="${setHour}" ${setHour eq alarmHisVo.startHour ? 'selected' : ''}>
                            	<c:out value="${setHour}" />
                            </option>
                   	</c:forEach>
				</select>
				<label><spring:message code="time.hour" /></label>
				<select class="form-select" id="startMin" name="startMin">					
					<c:forEach begin="0" end="59" var="min">
                    	<c:set var="setMin" value="${min}" />
                        	<c:if test="${min < 10}">
                            	<c:set var="setMin" value='0${min}' />
                            </c:if>
                           	<option value="${setMin}" ${setMin eq alarmHisVo.startMin ? 'selected' : ''}>
                               	<c:out value="${setMin}" />
                            </option>
					</c:forEach>
				</select>
				<label><spring:message code="time.minute" /></label>
			</div>
			<div class="search-area">
				<span><strong><spring:message code="search.endTime2" /></strong></span>
				<input type="text" class="inp-date inp-date-picker w150" id="endDate" name="endDate" value="${defaultEndDate}" autocomplete="off"/>
				<select class="form-select" id="endHour" name="endHour" >					
					<c:forEach begin="0" end="23" var="hour">
                    	<c:set var="setHour" value="${hour}" />
                        	<c:if test="${hour < 10}">
                            	<c:set var="setHour" value='0${hour}' />
                            </c:if>
                            <option value="${setHour}" ${setHour eq alarmHisVo.endHour ? 'selected' : ''}>
                            	<c:out value="${setHour}" />
                            </option>
                   	</c:forEach>
				</select>
				<label><spring:message code="time.hour" /></label>
				<select class="form-select" id="endMin" name="endMin">					
					<c:forEach begin="0" end="59" var="min">
                    	<c:set var="setMin" value="${min}" />
                        	<c:if test="${min < 10}">
                            	<c:set var="setMin" value='0${min}' />
                            </c:if>
                           	<option value="${setMin}" ${setMin eq alarmHisVo.endMin ? 'selected' : ''}>
                               	<c:out value="${setMin}" />
                            </option>
					</c:forEach>
				</select>
				<label><spring:message code="time.minute" /></label>
			</div>
			<div class="search-area">
				<select id="searchAlarmCause" name = "searchAlarmCause">
					<option value=""><spring:message code="select.alarmCause" /></option>
					<c:forEach var="cause" items="${alarmCauseList }">
						<option value="${cause.alarm_id }" <c:if test="${cause.alarm_id eq alarmHisVo.searchAlarmCause }">selected</c:if>>
							<c:out value="${cause.alarm_name }" />
						</option>
					</c:forEach>
				</select>
				<button class="btn-md white" type="button" id="btnSearch">
					<span><spring:message code="button.search" /></span>
				</button>
			</div>
			<div class="board-area">
				<table class="tbl-default bg-on ">
					<colgroup>
						<col style="width:12%;">
						<col style="width:8%;">
						<col style="">
						<col style="width:17%;">
						<col style="width:12%;">
						<col style="width:5%;">
						<col style="width:8%;">
						<col style="width:12%;">
						<col style="width:8%;">
					</colgroup>
					<thead>
						<tr>
							<th scope="col"><spring:message code="list.eventTime" /></th>
							<th scope="col"><spring:message code="list.severity" /></th>
							<th scope="col"><spring:message code="list.location" /></th>
							<th scope="col"><spring:message code="list.alarmMessage" /></th>
							<th scope="col"><spring:message code="list.firstTime" /></th>
							<th scope="col" style="word-break:break-all"><spring:message code="list.duplicates" /></th>
							<th scope="col"><spring:message code="list.actionAlarmhis" /></th>
							<th scope="col"><spring:message code="list.clearTime" /></th>
							<th scope="col"><spring:message code="list.operator" /></th>
						</tr>
					</thead>
					<tbody>
						<c:choose>
							<c:when test="${fn:length(alarmHisList) > 0 }">
								<c:forEach var="item" items="${alarmHisList }" >
									<tr>
										<td>${item.evt_time }</td>
										<td>${item.severity }</td>
										<td>${item.loc }</td>
										<td>${item.alarm_str }</td>
										<td>${item.first_time }</td>
										<td>${item.dup_cnt }</td>
										<td>${item.clr_flag }</td>
										<td>${item.clr_time }</td>
										<td>${item.clr_user }</td>
									</tr>
								</c:forEach>
							</c:when>
							<c:otherwise>
		                       	<tr >
		                           	<td colspan="9" >       
		                             	<div class="no-data">
		                                	<p><spring:message code="noData" /></p>
		                                </div>                               
		                 				</td>
		                           </tr>
		                       </c:otherwise>
						</c:choose>
					</tbody>
				</table>
				<div class="board-btm-area">
					<!-- paging -->
					${pagingHTML}
					<!-- //paging -->
					<div class="btn-area">
						<div class="fright">
							<button type="button" id="btnExcel" name="btnExcel" class="btn-lg green">
								<span><spring:message code="button.exportToExcel" /></span>
							</button>
						</div>	
					</div>	
				</div>
			</div>
		</form>
	</section>
	
	<jsp:include page="../footer.jsp" flush="false"/>


</body>
</html>