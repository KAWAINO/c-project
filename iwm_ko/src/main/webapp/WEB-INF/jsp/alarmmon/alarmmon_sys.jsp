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

<script>

</script>

</head>
<body>

	<section class="contents-area">
		<header>
			<div class="monitoring-controller">
				<button type="button" id="startAlarmSys" onClick="setSysTimer('start');" class="start on">Start</button>
				<button type="button" id="stopAlarmSys" onClick="setSysTimer('stop');" class="stop">Stop</button>
			</div>
		</header>
		<div class="rt-area" style="right:220px;">
			<ul class="monitoring-alarm">
				<li>
					<div class="status critical">Critical</div>
					<span id="sysAlarmCritical" >&nbsp;${critical }<spring:message code="header.severity" /></span>
				</li>
				<li>
					<div class="status major">Major</div>
					<span id="sysAlarmMajor" >&nbsp;${major }<spring:message code="header.severity" /></span>
				</li>
				<li>
					<div class="status minor">Minor</div>
					<span id="sysAlarmMinor">&nbsp;${minor }<spring:message code="header.severity" /></span>
				</li>
			</ul>		
		</div>	
	
		<div class="board-area">
			<table class="tbl-default">
				<colgroup>
				<col style="width:14%;">
				<col style="width:10%;">
				<col style="width:20%;">
				<col style="">
				<col style="width:14%;">
				<col style="width:7%;">
				<col style="width:7%;">
				<col style="width:7%;">
				</colgroup>
				<thead>
					<tr>
						<th scope="col"><spring:message code="list.eventTime" /></th>
						<th scope="col"><spring:message code="list.severity" /></th>
						<th scope="col"><spring:message code="list.location" /></th>
						<th scope="col"><spring:message code="list.alarmMessage" /></th>
						<th scope="col"><spring:message code="list.firstTime" /></th>
						<th scope="col"><spring:message code="list.duplicates" /></th>
						<th scope="col"><spring:message code="list.sound" /></th>						
						<th scope="col"><spring:message code="list.clear" /></th>
					</tr>
				</thead>
				<tbody>
					<c:choose>
						<c:when test="${fn:length(getAlarmSysList) > 0 }">
							<c:forEach var="item" items="${getAlarmSysList }" >
								<tr>
									<td>${item.evt_time != null ? item.evt_time : ""}</td>
									<td>${item.severity != null ? item.severity : ""}</td>
									<td>${item.loc != null ? item.loc : ""}</td>
									<td>${item.alarm_str != null ? item.alarm_str : ""}</td>
									<td>${item.first_time != null ? item.first_time : ""}</td>
									<td><fmt:formatNumber type="number" maxFractionDigits="3" value="${item.dup_cnt }"/></td>
									<td>
										<c:choose>
									        <c:when test="${item.snd_yn == 'Y'}">
									            <button type="button" class="btn-sm skyblue" id="delSound" name="delSound" onClick="delSound(${item.seq });">
									            	<span><spring:message code="alarmsys.ok" /></span>
									            </button>
									        </c:when>
									        <c:otherwise>		
									        				        	
									        </c:otherwise>
									    </c:choose>
									</td>
	
									<td>
										<button type="button" class="btn-sm skyblue" id="delAlarm" name="delAlarm" onClick="delAlarm(${item.seq });">
											<span><spring:message code="button.delete" /></span>
										</button>
									</td>
								</tr>
							</c:forEach>
						</c:when>
						<c:otherwise>
	                       	<tr >
	                           	<td colspan="8" >       
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