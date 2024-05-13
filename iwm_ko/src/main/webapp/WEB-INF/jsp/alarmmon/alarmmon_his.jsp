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

</head>
<body>

	<section class="contents-area">
		<header>
			<div class="monitoring-controller">
				<button type="button" id="startAlarmHis" onClick="setHisTimer('start');" class="start on">Start</button>
				<button type="button" id="stopAlarmHis" onClick="setHisTimer('stop');" class="stop">Stop</button>
			</div>
		</header>
		<div class="board-area">
			<table class="tbl-default">
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
						<th scope="col"><spring:message code="list.duplicates" /></th>
						<th scope="col"><spring:message code="list.actionAlarmhis" /></th>						
						<th scope="col"><spring:message code="list.clearTime" /></th>
						<th scope="col"><spring:message code="list.operator" /></th>
					</tr>
				</thead>
				<tbody>
					<c:choose>
						<c:when test="${fn:length(getAlarmHisList) > 0 }">
							<c:forEach var="item" items="${getAlarmHisList }" >
								<tr>
									<td>${item.evt_time != null ? item.evt_time : ""}</td>
									<td>${item.severity != null ? item.severity : ""}</td>
									<td>${item.loc != null ? item.loc : ""}</td>
									<td>${item.alarm_str != null ? item.alarm_str : ""}</td>
									<td>${item.first_time != null ? item.first_time : ""}</td>
									<td>${item.dup_cnt != null ? item.dup_cnt : ""}</td>
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
		</div>
	</section>

</body>
</html>