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
    	var startDate = $('#startDate').val() + $('#StartHour').val() + $('#StartMin').val() + '00';
    	var endDate = $('#endDate').val() + $('#EndHour').val() + $('#EndMin').val() + '59';
    	
    	// 날짜 검색 조건
        if (startDate >= endDate) {
        	alert("검색 기간 설정이 잘못되었습니다. 시작날짜가 종료날짜보다 늦습니다.");
            return;
        }

    	$("#pageno").val(1);
        $("#searchChked").val("true");
    	$("#searchCrewDataForm").attr("action", "/crewdata/crewdata.do");
        $("#searchCrewDataForm").submit();
	});
	
	// excel
    $("#btnExcel").bind("click", function() {	
    	goExcelDown();
    });
});

//선박명 리스트 생성 ajax
function getShipList(searchCompId){
	
	// submit 후 selected 유지를 위해 이전에 선택된 s_code 값을 변수에 저장
	let selectedScode = $('#s_code').val(); 
	
	if(searchCompId !== ""){	
		$.ajax({
			type : "POST",
			url : "/apinfo/shipList.ajax",
			data : { "searchCompId" : searchCompId },
			dataType : "json",
			error	: function (e) { alert("오류가 발생하였습니다. 관리자에게 문의 바랍니다.");},		
			success : function(data) {		
				
				var result = "<option value=''><spring:message code="select.shipName" /></option>";
				
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
	$("#searchCrewDataForm").attr("action", "/crewdata/crewdata.do");
    $("#searchCrewDataForm").submit();	
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
	
	$("#searchCrewDataForm").attr("action", "/crewdata/crewDataExcelDownload.do");
    $("#searchCrewDataForm").submit();
}

</script>

</head>

<body>

	<jsp:include page="../header.jsp" flush="false"/>
	
	<div class="contents-area">
		<header>
			<i class="icon-list"><span>icon</span></i>
			<h2><spring:message code="crewdata.addedCrewUsageDetails" /></h2>
		</header>
		<form id="searchCrewDataForm" name="searchCrewDataForm" method="post">
			<input type="hidden" id="pageno" name="pageno" value="${crewDataVo.pageno }"/>
			<input type="hidden" id="searchChked" name="searchChked" value="false">
			<input type="hidden" id="s_code" name="s_code" value=${s_code }>
			<input type="hidden" id="excelMsg" name="excelMsg" value="">
			<div class="search-area">
				<select  id="searchCompId" name="searchCompId">
					<option value=""><spring:message code="select.shipOwner" /></option>
					<c:forEach var="ship" items="${compList }">
						<option value="${ship.comp_id }" <c:if test="${ship.comp_id eq crewdataVo.searchCompId }">selected</c:if>>
							<c:out value="${ship.comp_name }" />
						</option>
					</c:forEach>
				</select>
				<select id="searchShipName" name="searchShipName" >
				</select>
				<input class="w160" type="text" id="searchUserId" name="searchUserId" 
					placeholder="<spring:message code="cuser.enterOperatorID" />" value="${crewdataVo.searchUserId }">
				<input class="w180" type="text" id="searchUserName" name="searchUserName" 
					placeholder="<spring:message code="cuser.enterOperatorNickName" />" value="${crewdataVo.searchUserName }">
				
			</div>
			<div class="search-area">
				<span><strong><spring:message code="time.date" /></strong></span>
				<input type="text" class="inp-date inp-date-picker w150" id="startDate" name="startDate" 
					value="${defaultStartDate}" autocomplete="off"/>
				<select class="form-select" id="startHour" name="startHour" >					
					<c:forEach begin="0" end="23" var="hour">
                    	<c:set var="setHour" value="${hour}" />
                        	<c:if test="${hour < 10}">
                            	<c:set var="setHour" value='0${hour}' />
                            </c:if>
                            <option value="${setHour}" ${setHour eq crewdataVo.startHour ? 'selected' : ''}>
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
                           	<option value="${setMin}" ${setMin eq crewdataVo.startMin ? 'selected' : ''}>
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
                            <option value="${setHour}" ${setHour eq crewdataVo.endHour ? 'selected' : ''}>
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
                            <option value="${setMin}" ${setMin eq crewdataVo.endMin ? 'selected' : ''}>
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
						<col style="width:12%;">
						<col style="width:12%;">
						<col style="width:12%;">
						<col style="width:12%;">
						<col style="width:10%;">
						<col style="width:12%;">
						<col style="width:10%;">
						<col style="width:10%;">
						<col style="width:10%;">
						<col style="width:10%;">
						<col style="">
					</colgroup>
					<thead>
						<tr>
							<th scope="col"><spring:message code="time.date" /></th>
							<th scope="col"><spring:message code="select.shipOwner" /></th>
							<th scope="col"><spring:message code="select.shipName" /></th>
							<th scope="col"><spring:message code="list.operatorID" /></th>
							<th scope="col"><spring:message code="list.operatorNickName" /></th>
							<th scope="col"><spring:message code="list.crewID" /></th>
							<th scope="col"><spring:message code="list.crewNickName" /></th>
							<th scope="col"><spring:message code="list.previousDataUsage" /></th>	
							<th scope="col"><spring:message code="list.dataUsage" /></th>	
							<th scope="col"><spring:message code="list.dataUsageDifference" /></th>	
						</tr>
					</thead>
					<tbody>
						<c:choose>
							<c:when test="${fn:length(logList) > 0 }">
								<c:forEach var="item" items="${logList }" >
									<tr>
										<td>${item.log_date }</td>
										<td>${item.comp_name }</td>
										<td>${item.s_name }</td>
										<td>${item.user_id }</td>
										<td>${item.user_name }</td>
										<td>${item.crew_id }</td>
										<td>${item.crew_name }</td>
										<td>
											<fmt:formatNumber type="number" maxFractionDigits="3" value="${item.pre_data }" /> MB
										</td>
										<td>
											<fmt:formatNumber type="number" maxFractionDigits="3" value="${item.data }" /> MB
										</td>
										<td>
											${item.diff_data }<%-- <fmt:formatNumber type="number" maxFractionDigits="3" value="${item.diff_data }" /> MB --%>
										</td>
									</tr>
								</c:forEach>
							</c:when>
							<c:otherwise>
		                       	<tr >
		                           	<td colspan="10" >       
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