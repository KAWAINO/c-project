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
	let select = '<spring:message code="select.select" />';
	let success = '<spring:message code="confirm.success" />';
	let del = '<spring:message code="confirm.delete" />';
	let error = '<spring:message code="confirm.error" />';

$(document).ready(function(){
	
	searchCompId = $('#searchCompId option:selected').val();
	getShipList(searchCompId);
	
	// 선주사박스 change event
	$('#searchCompId').on('change', function() {
		$('#s_code').val(''); 
		searchCompId = $('#searchCompId option:selected').val();
		getShipList(searchCompId);
	});

	/* 체크박스 제어 및 상태유지 */
	let conn = $('#conn_temp').val();
	if(conn != null && conn == 'on'){
		$('#conn').prop('checked', true);
	} else {
		$('#conn').prop('checked', false);
	}
	
	let disconn = $('#disconn_temp').val();
	if(disconn != null && disconn == 'on'){
		$('#disconn').prop('checked', true);
	} else {
		$('#disconn').prop('checked', false);
	}
	/* 체크박스 제어 및 상태유지 */
	
	$("#btnSearch").bind("click", function() {
    	let conStrDate = $('#conStrDate').val() + $('#conStrHour').val() + $('#conStrMin').val() + '00';
    	let conEndDate = $('#conEndDate').val() + $('#conEndHour').val() + $('#conEndMin').val() + '59';
    	let disconStrDate = $('#disconStrDate').val() + $('#disconStrHour').val() + $('#disconStrMin').val() + '00';
    	let disconEndDate = $('#disconEndDate').val() + $('#disconEndHour').val() + $('#disconEndMin').val() + '59';
	
    	// 날짜 검색 조건
        if (conStrDate >= conEndDate) {
        	alert('<spring:message code="confirm.wrong.greeterThanAccessDate" />');
            return;
        }

   		if (disconStrDate >= disconEndDate) {
   	        alert('<spring:message code="confirm.wrong.greeterThanDisconnectDate" />');
           	return;
       	} 

        $("#pageno").val(1);
    	$("#searchChked").val("true");
    	$("#searchCrewAuthForm").attr("action", "/crewauth/crewauth.do");
        $("#searchCrewAuthForm").submit();
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
				error	: function (e) { alert(error);},		
				success : function(data) {		
					
					var result = "<option value=''>" + select + "</option>";
					
					for(i = 0; i < data.length; i++){
						// 이전에 선택된 s_code 값 selected 유지 및 선박명 리스트 생성
						let isSelected = data[i].s_code === selectedScode ? " selected" : "";		
						result += "<option value='"+data[i].s_code+"'"+isSelected+">"+data[i].s_name+"</option>";
					}
					$('#searchShipName').html(result);
				} 
			});
		} else {
		    $('#searchShipName').html("<option value=''>" + select + "</option>");
		}
	}
	
	//paging
	function goPage(pageno) {
		$("#pageno").val(pageno);
		$("#searchChked").val("true");
		$("#searchCrewAuthForm").attr("action", "/crewauth/crewauth.do");
	    $("#searchCrewAuthForm").submit();	
	}

</script> 
</head>
<body>

	<jsp:include page="../header.jsp" flush="false"/>
	
	<div class="contents-area">
		<header>
			<i class="icon-list"><span>icon</span></i>
			<h2><spring:message code="crewauth.crewAuthenticationInfo" /></h2>
		</header>
		<form id="searchCrewAuthForm" name="searchCrewAuthForm" method="post">
			<input type="hidden" id="pageno" name="pageno" value="${crewAuthVo.pageno }"/>
			<input type="hidden" id="searchChked" name="searchChked" value="false">
			<input type="hidden" id="excelMsg" name="excelMsg" value="">
			<input type="hidden" id="s_code" name="s_code" value="${s_code }">
			<input type="hidden" id="conn_temp" name="conn_temp" value="${conn }">
			<input type="hidden" id="disconn_temp" name="disconn_temp" value="${disconn }">
			<input type="hidden" id="comp_id" name="comp_id" value="">
			<div class="search-area">
				<select  id="searchCompId" name="searchCompId">
					<option value=""><spring:message code="select.shipOwner" /></option>
					<c:forEach var="ship" items="${compList }">
						<option value="${ship.comp_id }" <c:if test="${ship.comp_id eq crewAuthVo.searchCompId }">selected</c:if> >
							<c:out value="${ship.comp_name }" />
						</option>
					</c:forEach>
				</select>
				<select id="searchShipName" name="searchShipName" >
					<option><spring:message code="select.shipName" /></option>
				</select>
				<input class="w200" type="text" id="searchCrewId" name="searchCrewId" 
					placeholder="<spring:message code="search.enterIDorNickname" />"
					value="${!empty crewAuthVo.searchCrewId ? crewAuthVo.searchCrewId : crewAuthVo.searchCrewName}">
				<select id="searchStatus" name="searchStatus">
					<option value="">
						<spring:message code="select.deviceStatus" />
					</option>
					<option value="1" ${'1' eq crewAuthVo.searchStatus ? 'selected' : ''}>
						<spring:message code="select.online" />
					</option>
					<option value="2" ${'2' eq crewAuthVo.searchStatus ? 'selected' : ''}>
						<spring:message code="select.offline" />
					</option>
					<option value="3" ${'3' eq crewAuthVo.searchStatus ? 'selected' : ''}>
						<spring:message code="select.blocked" />
					</option>
				</select>
				<select id="searchDisconFlag" name="searchDisconFlag">
					<option value=""><spring:message code="select.disconnectionCause" /></option>
					<option value="1" ${'1' eq crewAuthVo.searchDisconFlag ? 'selected' : ''}><spring:message code="select.normal" /></option>
					<option value="2" ${'2' eq crewAuthVo.searchDisconFlag ? 'selected' : ''}><spring:message code="select.deliveryExceeded" /></option>
					<option value="3" ${'3' eq crewAuthVo.searchDisconFlag ? 'selected' : ''}><spring:message code="select.limitedExceeded" /></option>
					<option value="4" ${'4' eq crewAuthVo.searchDisconFlag ? 'selected' : ''}><spring:message code="select.notInUseTime" /></option>
					<option value="5" ${'5' eq crewAuthVo.searchDisconFlag ? 'selected' : ''}><spring:message code="select.noDayOfUse" /></option>
					<option value="6" ${'6' eq crewAuthVo.searchDisconFlag ? 'selected' : ''}><spring:message code="select.notInUsePeriod" /></option>
					<option value="7" ${'7' eq crewAuthVo.searchDisconFlag ? 'selected' : ''}><spring:message code="select.usageExceeded" /></option>
					<option value="8" ${'8' eq crewAuthVo.searchDisconFlag ? 'selected' : ''}><spring:message code="select.useTimeout" /></option>
					<option value="9" ${'9' eq crewAuthVo.searchDisconFlag ? 'selected' : ''}><spring:message code="select.disconnectionByUse" /></option>
					<option value="10" ${'10' eq crewAuthVo.searchDisconFlag ? 'selected' : ''}><spring:message code="select.disconnectionByOperator" /></option>
					<option value="11" ${'11' eq crewAuthVo.searchDisconFlag ? 'selected' : ''}><spring:message code="select.deauthorization" /></option>
					<option value="12" ${'12' eq crewAuthVo.searchDisconFlag ? 'selected' : ''}><spring:message code="select.concurrentUsersExceeded" /></option>
					<option value="13" ${'13' eq crewAuthVo.searchDisconFlag ? 'selected' : ''}><spring:message code="select.blockWIFIuse" /></option>
				</select>
			</div>
			
			<div class="search-area">
				<span class="check-box1">
					<input type="checkbox" name="conn" id="conn" >
					<label for="conn">
						<strong><spring:message code="time.accessTime" /></strong>
					</label>
				</span>
				<input type="text" class="inp-date inp-date-picker w120" id="conStrDate" name="conStrDate" value="${defaultConStr}" autocomplete="off"/>
				<select class="form-select" id="conStrHour" name="conStrHour" >					
					<c:forEach begin="0" end="23" var="hour">
                    	<c:set var="setHour" value="${hour}" />
                        	<c:if test="${hour < 10}">
                            	<c:set var="setHour" value='0${hour}' />
                            </c:if>
                            <option value="${setHour}" ${setHour eq crewAuthVo.conStrHour ? 'selected' : ''}>
                            	<c:out value="${setHour}" />
                            </option>
                   	</c:forEach>
				</select>
				<label><spring:message code="time.hour" /></label>
				<select class="form-select" id="conStrMin" name="conStrMin">					
					<c:forEach begin="0" end="59" var="min">
                    	<c:set var="setMin" value="${min}" />
                        	<c:if test="${min < 10}">
                            	<c:set var="setMin" value='0${min}' />
                            </c:if>
                           	<option value="${setMin}" ${setMin eq crewAuthVo.conStrMin ? 'selected' : ''}>
                               	<c:out value="${setMin}" />
                            </option>
					</c:forEach>
				</select>
				<label><spring:message code="time.minute" /> ~</label>
				<input type="text" class="inp-date inp-date-picker w120" id="conEndDate" name="conEndDate" value="${defaultConEnd}" autocomplete="off"/>
				<select class="form-select" id="conEndHour" name="conEndHour">					
					<c:forEach begin="0" end="23" var="hour">
                    	<c:set var="setHour" value="${hour}" />
                        	<c:if test="${hour < 10}">
                            	<c:set var="setHour" value='0${hour}' />
                            </c:if>
                            <option value="${setHour}" ${setHour eq crewAuthVo.conEndHour ? 'selected' : ''}>
                            	<c:out value="${setHour}" />
                            </option>
                    </c:forEach>
				</select>
				<label><spring:message code="time.hour" /></label>
				<select class="form-select" id="conEndMin" name="conEndMin" >
					<c:forEach begin="0" end="59" var="min">
                    	<c:set var="setMin" value="${min}" />
                        	<c:if test="${min < 10}">
                            	<c:set var="setMin" value='0${min}' />
                            </c:if>
                            <option value="${setMin}" ${setMin eq crewAuthVo.conEndMin ? 'selected' : ''}>
                            	<c:out value="${setMin}" />
                            </option>
                    </c:forEach>
				</select>
				<label><spring:message code="time.minute" /></label>
			</div>
			
			<div class="search-area">
				<span class="check-box1">
					<input type="checkbox" name="disconn" id="disconn">
					<label for="disconn">
						<strong><spring:message code="time.disconnectTime" /></strong>
					</label>
				</span>
				<input type="text" class="inp-date inp-date-picker w120" id="disconStrDate" name="disconStrDate" value="${defaultDisconStr}" autocomplete="off"/>
				<select class="form-select" id="disconStrHour" name="disconStrHour" >					
					<c:forEach begin="0" end="23" var="hour">
                    	<c:set var="setHour" value="${hour}" />
                        	<c:if test="${hour < 10}">
                            	<c:set var="setHour" value='0${hour}' />
                            </c:if>
                            <option value="${setHour}" ${setHour eq crewAuthVo.disconStrHour ? 'selected' : ''}>
                            	<c:out value="${setHour}" />
                            </option>
                   	</c:forEach>
				</select>
				<label><spring:message code="time.hour" /></label>
				<select class="form-select" id="disconStrMin" name="disconStrMin">					
					<c:forEach begin="0" end="59" var="min">
                    	<c:set var="setMin" value="${min}" />
                        	<c:if test="${min < 10}">
                            	<c:set var="setMin" value='0${min}' />
                            </c:if>
                           	<option value="${setMin}" ${setMin eq crewAuthVo.disconStrMin ? 'selected' : ''}>
                               	<c:out value="${setMin}" />
                            </option>
					</c:forEach>
				</select>
				<label><spring:message code="time.minute" /> ~</label>
				<input type="text" class="inp-date inp-date-picker w120" id="disconEndDate" name="disconEndDate" value="${defaultDisconEnd}" autocomplete="off"/>
				<select class="form-select" id="disconEndHour" name="disconEndHour">					
					<c:forEach begin="0" end="23" var="hour">
                    	<c:set var="setHour" value="${hour}" />
                        	<c:if test="${hour < 10}">
                            	<c:set var="setHour" value='0${hour}' />
                            </c:if>
                            <option value="${setHour}" ${setHour eq crewAuthVo.disconEndHour ? 'selected' : ''}>
                            	<c:out value="${setHour}" />
                            </option>
                    </c:forEach>
				</select>
				<label><spring:message code="time.hour" /></label>
				<select class="form-select" id="disconEndMin" name="disconEndMin" >
					<c:forEach begin="0" end="59" var="min">
                    	<c:set var="setMin" value="${min}" />
                        	<c:if test="${min < 10}">
                            	<c:set var="setMin" value='0${min}' />
                            </c:if>
                            <option value="${setMin}" ${setMin eq crewAuthVo.disconEndMin ? 'selected' : ''}>
                            	<c:out value="${setMin}" />
                            </option>
                    </c:forEach>
				</select>
				<label><spring:message code="time.minute" /></label>
				<button class="btn-md white" type="button" id="btnSearch">
					<span><spring:message code="button.search" /></span>
				</button>
			</div>
			<div class="board-area">
				<table class="tbl-default bg-on ">
					<colgroup>
							<col style="">
							<col style="width:10%;">
							<col style="width:10%;">
							<col style="width:10%;">
							<col style="width:10%;">
							<col style="width:7%;">
							<col style="width:10%;">
							<col style="width:11%;">
							<col style="width:11%;">
							<col style="width:11%;">
					</colgroup>
					<thead>
						<tr>
							<th scope="col"><spring:message code="select.shipOwner" /></th>
							<th scope="col"><spring:message code="select.shipName" /></th>
							<th scope="col"><spring:message code="list.crewNickName" /></th>
							<th scope="col"><spring:message code="list.crewID" /></th>
							<th scope="col">MAC</th>
							<th scope="col"><spring:message code="select.deviceStatus" /></th>
							<th scope="col"><spring:message code="list.wapName" /></th>
							<th scope="col"><spring:message code="list.disconnectCause" /></th>
							<th scope="col"><spring:message code="list.accessTime" /></th>
							<th scope="col"><spring:message code="list.lastDisconnectTime" /></th>
						</tr>
					</thead>
					<tbody>
						<c:choose>
							<c:when test="${fn:length(crewAuthList) > 0 }">
								<c:forEach var="item" items="${crewAuthList }" >
									<tr>
										<td>${item.comp_name }</td>
										<td>${item.s_name }</td>
										<td>${item.crew_name }</td>
										<td>${item.crew_id }</td>
										<td>${item.mac }</td>
										<td>${item.status }</td>
										<td>${item.ap_name }</td>
										<td>${item.auth_flag }</td>
										<td>${item.conn_time }</td>
										<td>${item.disconn_time }</td>
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
				<!-- <div class="btn-area">
					<div class="fright">
						<button type="button" id="btnExcel" name="btnExcel" class="btn-lg green"><span>엑셀 다운로드</span></button>
					</div>	
				</div>  -->		
			</div>
		</form>
	</div>

	<jsp:include page="../footer.jsp" flush="false"/>

</body>
</html>