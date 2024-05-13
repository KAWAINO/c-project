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
		<div class="board-area">
			<table class="tbl-default">
				<colgroup>
					<col style="width:25%;">
					<col style="width:25%;">
					<col style="width:25%;">
					<col style="">
				</colgroup>
				<thead>
				<tr>
					<th scope="col"><spring:message code="active.serverName" /></th>
					<th scope="col"><spring:message code="list.status" /></th>
					<th scope="col"><spring:message code="active.authorizedIP" /></th>
					<th scope="col"><spring:message code="active.updateTime" /></th>
				</tr>
			</thead>
			<tbody>
					<c:choose>
						<c:when test="${fn:length(getAlarmActiveList) > 0 }">
							<c:forEach var="item" items="${getAlarmActiveList }" >
								<tr>
									<td>${item.alias_name }</td>
									<td>${item.ha_status  eq "A" ? "Active" : "Standby"}</td>
									<td>${item.ip_addr }</td>
									<td>${item.update_time }</td>
								</tr>
							</c:forEach>
						</c:when>
						<c:otherwise>
	                       	<tr >
	                           	<td colspan="4" >       
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