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

$(document).ready(function(){

	$("#btnSearch").bind("click", function() {
    	let startDate = $('#startDate').val() + $('#StartHour').val() + $('#StartMin').val() + '00';
    	let endDate = $('#endDate').val() + $('#EndHour').val() + $('#EndMin').val() + '59';
    	
    	// 날짜 검색 조건
        if (startDate >= endDate) {
        	alert('<spring:message code="confirm.dateGreater" />');
            return;
        }
    	
    	$("#pageno").val(1);
        $("#searchChked").val("true");
    	$("#searchClogForm").attr("action", "/clog/clog.do");
        $("#searchClogForm").submit();
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
	$("#searchClogForm").attr("action", "/clog/clog.do");
    $("#searchClogForm").submit();	
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
	
	$("#searchClogForm").attr("action", "/clog/clogExcelDownload.do");
    $("#searchClogForm").submit();
}

</script>

</head>

<body>
	<jsp:include page="../header.jsp" flush="false"/>
	
	<div class="contents-area">
		<header>
			<i class="icon-list"><span>icon</span></i>
			<h2><spring:message code="clog.iwmWorkHistory" /></h2>
		</header>
		<form id="searchClogForm" name="searchClogForm" method="post">
			<input type="hidden" id="pageno" name="pageno" value="${clogVo.pageno }"/>
			<input type="hidden" id="searchChked" name="searchChked" value="false">
			<input type="hidden" id="excelMsg" name="excelMsg" value="">
			<div class="search-area">
				<select  id="searchCompId" name="searchCompId">
					<option value=""><spring:message code="select.shipOwner" /></option>
					<c:choose>
						<c:when test="${sessionScope.comp_id == '0' }">
							<option value="0" ${clogVo.searchCompId == '0' ? 'selected' : ''}>
								<spring:message code="cuser.noShipOwner" />
							</option>
						</c:when>
					</c:choose>
					<c:forEach var="ship" items="${compList }">
						<option value="${ship.comp_id }" <c:if test="${ship.comp_id eq clogVo.searchCompId }">selected</c:if>>
							<c:out value="${ship.comp_name }" />
						</option>
					</c:forEach>
				</select>
				<input class="w180" type="text" id="searchUserName" name="searchUserName" 
					placeholder="<spring:message code="cuser.enterOperatorNickName" />" value="${clogVo.searchUserName }">
				<input class="w160" type="text" id="searchUserId" name="searchUserId" 
					placeholder="<spring:message code="cuser.enterOperatorID" />" value="${clogVo.searchUserId }">
				<select  id="searchGuiCode" name="searchGuiCode">
					<option value=""><spring:message code="clog.menuMyung" /></option>
					<c:choose>
						<c:when test="${sessionScope.language == 'korean' }">
							<c:forEach var="ship" items="${getMenuList }">
								<option value="${ship.gui_code }" <c:if test="${ship.gui_code eq clogVo.searchGuiCode }">selected</c:if>>
									<c:out value="${ship.gui_name }" />
								</option>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<c:forEach var="ship" items="${getMenuList }">
								<option value="${ship.gui_code }" <c:if test="${ship.gui_code eq clogVo.searchGuiCode }">selected</c:if>>
									<c:out value="${ship.en_gui_name }" />
								</option>
							</c:forEach>
						</c:otherwise>
					</c:choose>
					
				</select>
				<select id="searchJobName" name="searchJobName">
					<option value=""><spring:message code="clog.history" /></option>
					<option value="로그인" ${'로그인' eq clogVo.searchJobName ? 'selected' : ''}>
						<spring:message code="clog.login" />
					</option>
					<option value="로그아웃" ${'로그아웃' eq clogVo.searchJobName ? 'selected' : ''}>
						<spring:message code="clog.logOut" />
					</option>
					<option value="조회" ${'조회' eq clogVo.searchJobName ? 'selected' : ''}>
						<spring:message code="clog.search" />
					</option>
					<option value="추가" ${'추가' eq clogVo.searchJobName ? 'selected' : ''}>
						<spring:message code="clog.add" />
					</option>
					<option value="수정" ${'수정' eq clogVo.searchJobName ? 'selected' : ''}>
						<spring:message code="clog.modify" />
					</option>
					<option value="삭제" ${'삭제' eq clogVo.searchJobName ? 'selected' : ''}>
						<spring:message code="clog.delete" />
					</option>
					<option value="엑셀" ${'엑셀' eq clogVo.searchJobName ? 'selected' : ''}>
						<spring:message code="clog.excel" />
					</option>
					<option value="Import" ${'import' eq clogVo.searchJobName ? 'selected' : ''}>
						<spring:message code="clog.import" />
					</option>
					<option value="보고서 출력" ${'보고서 출력' eq clogVo.searchJobName ? 'selected' : ''}>
						<spring:message code="clog.printReport" />
					</option>
				</select>
			</div>
			<div class="search-area">
				<span><strong><spring:message code="time.date" /></strong></span>
				<input type="text" class="inp-date inp-date-picker w150" id="startDate" name="startDate" value="${defaultStartDate}" autocomplete="off"/>
				<select class="form-select" id="startHour" name="startHour" >					
					<c:forEach begin="0" end="23" var="hour">
                    	<c:set var="setHour" value="${hour}" />
                        	<c:if test="${hour < 10}">
                            	<c:set var="setHour" value='0${hour}' />
                            </c:if>
                            <option value="${setHour}" ${setHour eq clogVo.startHour ? 'selected' : ''}>
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
                           	<option value="${setMin}" ${setMin eq clogVo.startMin ? 'selected' : ''}>
                               	<c:out value="${setMin}" />
                            </option>
					</c:forEach>
				</select>
				<label><spring:message code="time.min" /> ~</label>
				<input type="text" class="inp-date inp-date-picker w150" id="endDate" name="endDate" value="${defaultEndDate}" autocomplete="off"/>
				<select class="form-select" id="endHour" name="endHour">					
					<c:forEach begin="0" end="23" var="hour">
                    	<c:set var="setHour" value="${hour}" />
                        	<c:if test="${hour < 10}">
                            	<c:set var="setHour" value='0${hour}' />
                            </c:if>
                            <option value="${setHour}" ${setHour eq clogVo.endHour ? 'selected' : ''}>
                            	<c:out value="${setHour}" />
                            </option>
                    </c:forEach>
				</select>
				<label><spring:message code="time.hour" /></label>
				<select class="form-select" id="endMin" name="endMin" >
					<c:forEach begin="0" end="59" var="min">
                    	<c:set var="setMin" value="${min}" />
                        	<c:if test="${min < 10}">
                            	<c:set var="setMin" value='0${min}' />
                            </c:if>
                            <option value="${setMin}" ${setMin eq clogVo.endMin ? 'selected' : ''}>
                            	<c:out value="${setMin}" />
                            </option>
                    </c:forEach>
				</select>
				<label><spring:message code="time.min" /></label>
				<button class="btn-md white" type="button" id="btnSearch"><span><spring:message code="button.search" /></span></button>
			</div>
			<div class="board-area">
				<table class="tbl-default bg-on ">
					<colgroup>
						<col style="width:15%;">
						<col style="width:10%;">
						<col style="width:10%;">
						<col style="width:10%;">
						<col style="width:15%;">
						<col style="width:5%;">
						<col style="">
					</colgroup>
					<thead>
						<tr>
							<th scope="col"><spring:message code="time.date" /></th>
							<th scope="col"><spring:message code="select.shipOwner" /></th>
							<th scope="col"><spring:message code="list.id" /></th>
							<th scope="col"><spring:message code="list.nickName" /></th>
							<th scope="col"><spring:message code="list.menual" /></th>
							<th scope="col"><spring:message code="list.history" /></th>
							<th scope="col"><spring:message code="list.detail" /></th>
						</tr>
					</thead>
					<tbody>
						<c:choose>
							<c:when test="${fn:length(cLogList) > 0 }">
								<c:forEach var="item" items="${cLogList }" >
									<tr>
										<td>${item.log_date }</td>
										<td>${item.comp_name }</td>
										<td>${item.user_id }</td>
										<td>${item.user_name }</td>
										<c:choose>
											<c:when test="${sessionScope.language == 'korean' }">
												<td>${item.gui_name }</td>
											</c:when>
											<c:otherwise>
												<td>${item.en_gui_name }</td>
											</c:otherwise>
										</c:choose>
										<td>${item.job_name }</td>
										<td>${item.gui_remark }</td>
									</tr>
								</c:forEach>
							</c:when>
							<c:otherwise>
		                       	<tr >
		                           	<td colspan="7" >       
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

</html>