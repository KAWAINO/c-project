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

    let searchCompId = $('#searchCompId option:selected').val();
	getShipList(searchCompId);

	// 선주사박스 change event
	$('#searchCompId').on('change', function() {
		$('#s_code').val('');
		searchCompId = $('#searchCompId option:selected').val();
		getShipList(searchCompId);
	});
	
	$("#btnSearch").bind("click", function() {
    	let startDate = $('#startDate').val() + $('#StartHour').val() + $('#StartMin').val() + '00';
    	let endDate = $('#endDate').val() + $('#EndHour').val() + $('#EndMin').val() + '59';
    	
    	// 날짜 검색 조건
        if (startDate >= endDate) {
        	alert("시작날짜가 종료 날짜보다  큽니다.");
            return;
        }

        $("#pageno").val(1);
        $("#searchChked").val("true");
    	$("#searchSlogForm").attr("action", "/slog/slog.do");
        $("#searchSlogForm").submit();      
	});			

	// excel
    $("#btnExcel").bind("click", function() {
    	goExcelDown();
    });
});

	//paging
	function goPage(pageno) {
		$("#pageno").val(pageno);
		$("#searchSlogForm").attr("action", "/slog/slog.do");
	    $("#searchSlogForm").submit();	
	}

	//선박명 리스트 생성 ajax
	function getShipList(searchCompId) {

		// submit 후 selected 유지를 위해 이전에 선택된 s_code 값을 변수에 저장
		let selectedScode = $('#s_code').val(); 
		
		if(searchCompId !== ''){	
			$.ajax({
				type : "POST",
				url : "/apinfo/shipList.ajax",
				data : { "searchCompId" : searchCompId, },
				dataType : "json",
				error	: function (e) { alert("오류가 발생하였습니다. 관리자에게 문의 바랍니다.");},		
				success : function(data) {		
					
					let result = "<option value=''><spring:message code="select.shipName" /></option>";
					
					for(i = 0; i < data.length; i++){
						// 이전에 선택된 s_code 값 selected 유지 및 선박명 리스트 생성
						let isSelected = data[i].s_code === selectedScode ? " selected" : "";		
						result += "<option value='"+data[i].s_code+"'"+isSelected+">"+data[i].s_name+"</option>";
					} 
					$('#searchShipName').html(result);
				} 
			});
		} else {
		    $('#searchShipName').html("<option value=''><spring:message code="select.shipName" /></option>");
		}
	}
	
	//paging
	function goPage(pageno) {
		$("#pageno").val(pageno);
		$("#searchChked").val("true");
		$("#searchSlogForm").attr("action", "/slog/slog.do");
	    $("#searchSlogForm").submit();	 
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
		
		$("#searchSlogForm").attr("action", "/slog/slogExcelDownload.do");
	    $("#searchSlogForm").submit();
	}

</script>

</head>

<body>

	<jsp:include page="../header.jsp" flush="false"/>
	
	<section class="contents-area">
		<header>
			<i class="icon-list"><span>icon</span></i>
			<h2><spring:message code="slog.owmWorkHistory" /></h2>
		</header>
		<form id="searchSlogForm" name="searchSlogForm" method="post">
			<input type="hidden" id="pageno" name="pageno" value="${slogVo.pageno }"/>
			<input type="hidden" id="searchChked" name="searchChked" value="true">     			
			<input type="hidden" id="s_code" name="s_code" value=${s_code }>
			<input type="hidden" id="excelMsg" name="excelMsg" value="">
			<div class="search-area">
				<select  id="searchCompId" name="searchCompId">			
					<option value=""><spring:message code="select.shipOwner" /></option>
					<c:forEach var="ship" items="${compList }">
						<option value="${ship.comp_id }" <c:if test="${ship.comp_id eq slogVo.searchCompId }">selected</c:if>>
							<c:out value="${ship.comp_name }" />
						</option>
					</c:forEach>
				</select>
				<select id="searchShipName" name="searchShipName" >
					<option value=""><spring:message code="select.shipName" /></option>
				</select>
				<input class="w160" type="text" id="searchUserId" name="searchUserId" 
					placeholder="<spring:message code="cuser.enterOperatorID" />" value="${slogVo.searchUserId }">
				<input class="w180" type="text" id="searchUserName" name="searchUserName" 
					placeholder="<spring:message code="cuser.enterOperatorNickName" />" value="${slogVo.searchUserName }">
				<select  id="searchGuiCode" name="searchGuiCode">
					<option value=""><spring:message code="clog.menuMyung" /></option>
					<c:forEach var="ship" items="${getMenuList }">
						<option value="${ship.gui_code }" <c:if test="${ship.gui_code eq slogVo.searchGuiCode }">selected</c:if>>
							<c:out value="${ship.gui_name }" />
						</option>
					</c:forEach>
				</select>
				<select id="searchJobName" name="searchJobName">
					<option value=""><spring:message code="clog.history" /></option>
					<option value="로그인" ${'로그인' eq slogVo.searchJobName ? 'selected' : ''}><spring:message code="clog.login" /></option>
					<option value="로그아웃" ${'로그아웃' eq slogVo.searchJobName ? 'selected' : ''}><spring:message code="clog.logOut" /></option>
					<option value="조회" ${'조회' eq slogVo.searchJobName ? 'selected' : ''}><spring:message code="clog.search" /></option>
					<option value="추가" ${'추가' eq slogVo.searchJobName ? 'selected' : ''}><spring:message code="clog.add" /></option>
					<option value="수정" ${'수정' eq slogVo.searchJobName ? 'selected' : ''}><spring:message code="clog.modify" /></option>
					<option value="삭제" ${'삭제' eq slogVo.searchJobName ? 'selected' : ''}><spring:message code="clog.delete" /></option>
					<option value="엑셀" ${'엑셀' eq slogVo.searchJobName ? 'selected' : ''}><spring:message code="clog.excel" /></option>
					<option value="Import" ${'import' eq slogVo.searchJobName ? 'selected' : ''}><spring:message code="clog.import" /></option>
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
                            <option value="${setHour}" ${setHour eq slogVo.startHour ? 'selected' : ''}>
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
                           	<option value="${setMin}" ${setMin eq slogVo.startMin ? 'selected' : ''}>
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
                            <option value="${setHour}" ${setHour eq slogVo.endHour ? 'selected' : ''}>
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
                            <option value="${setMin}" ${setMin eq slogVo.endMin ? 'selected' : ''}>
                            	<c:out value="${setMin}" />
                            </option>
                    </c:forEach>
				</select>
				<label><spring:message code="time.min" /></label>
				<button class="btn-md white" type="button" id="btnSearch"><span><spring:message code="button.search" /></span></button>
			</div>
			<div class="board-area">
				<table class="tbl-default bg-on">
					<colgroup>
						<col style="width:12%;">
						<col style="width:12%;">
						<col style="width:12%;">
						<col style="width:12%;">
						<col style="width:10%;">
						<col style="width:12%;">
						<col style="width:10%;">
						<col style="">
					</colgroup>
					<thead>
						<tr>
							<th scope="col"><spring:message code="time.date" /></th>
							<th scope="col"><spring:message code="select.shipOwner" /></th>
							<th scope="col"><spring:message code="select.shipName" /></th>
							<th scope="col"><spring:message code="list.nickName" /></th>
							<th scope="col"><spring:message code="list.id" /></th>
							<th scope="col"><spring:message code="list.menual" /></th>
							<th scope="col"><spring:message code="list.history" /></th>
							<th scope="col"><spring:message code="list.detail" /></th>	
						</tr>
					</thead>
					<tbody>
						<c:choose>
							<c:when test="${fn:length(slogList) > 0 }">
								<c:forEach var="item" items="${slogList }" >
									<tr>
										<td>${item.log_date }</td>
										<td>${item.comp_name }</td>
										<td>${item.s_name }</td>
										<td>${item.user_name }</td>
										<td>${item.user_id }</td>
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