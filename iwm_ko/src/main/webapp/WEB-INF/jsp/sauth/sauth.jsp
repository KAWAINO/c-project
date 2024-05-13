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

<script type="text/javascript" >

$(document).ready(function(){
	
	// 페이지 로딩 시 선박명 리스트 생성을 위한 comp_id set
	let searchCompId = $('#searchCompId option:selected').val();
	getShipList(searchCompId);

	// 선주사박스 change event
	$('#searchCompId').change( function() {
		$('#s_code').val('');
		searchCompId = $('#searchCompId option:selected').val();
		getShipList(searchCompId);
		$('#boardArea').html('<table class="tbl-default "><colgroup><col style="width:47%;"><col style=""></colgroup>'
			+ '<thead><tr><th scope="col">메뉴</th><th scope="col">사용여부</th></tr></thead><tbody>'
			+ '<tr ><td colspan="2"><div class="no-data"><p>선택된 선박이 없습니다. 선박을 선택하여 주세요.</p></div></td></tr></tbody></table>');
	});
	
	// 선박명, 등급 change event
	$("#searchScode, #searchGrade").on('change', function(){		
		$("#searchSauthForm").attr("action", "/sauth/sauth.do");
        $("#searchSauthForm").submit();  
	});
	
});
	
	// 선박명 리스트 생성 ajax
	function getShipList(searchCompId){	
		// submit 후 selected 유지를 위해 이전에 선택된 s_code 값을 변수에 저장
		let selectedScode = $('#s_code').val(); 
		//console.log(searchCompId);
		//console.log(selectedScode);
		if(searchCompId !== "select"){	
			$.ajax({
				type : "POST",
				url : "/apinfo/shipList.ajax",
				data : { "searchCompId" : searchCompId},
				dataType : "json",
				error	: function (e) { alert("오류가 발생하였습니다. 관리자에게 문의 바랍니다.");},		
				success : function(data) {		
					
					let result = "<option value='select'><spring:message code="select.shipName" /></option>";
					
					for(i = 0; i < data.length; i++){
						// 이전에 선택된 s_code 값 selected 유지 및 선박명 리스트 생성
						let isSelected = data[i].s_code === selectedScode ? " selected" : "";		
						result += "<option value='"+data[i].s_code+"'"+isSelected+">"+data[i].s_name+"</option>";
						//console.log('selectedScode : ' + selectedScode);
					}
					$('#searchScode').html(result);
				} 
			});
		} else {
		    $('#searchScode').html("<option value='select'><spring:message code="select.shipName" /></option>");
		}
	}

	function gradeUpdate(grade, s_code){	
		
		// Controller에서 각각 substring을 하기 위해 변수 2개를 지정
		var useFlag = [];
		var guiCode = [];
	
		$("select[name=useFlag] option:selected").each(function(){
			useFlag.push(this.value);
		});
		//console.log("useFlag : " + useFlag);
		
		$("select[name=useFlag] option:selected").each(function(){
			guiCode.push(this.value);
		});
		//console.log("guiCode : " + guiCode);
		
		// grade
		var grade = $("select[name=searchGrade] option:selected").val();
		//console.log("grade : " + grade);
		
		// s_code
		var s_code = $("select[name=searchScode] option:selected").val();
		//console.log("s_code : " + s_code);
	
		var datas = $("#searchCauthForm").serialize();
		$.ajax({
			type : "POST",
			url : "/sauth/gradeUpdate.ajax",
			data : {
					 "searchGrade" : grade,
					 "searchScode" : s_code,
					 "updUseFlag" : useFlag,
					 "updGuiCode" : guiCode
				   },
			dataType : "json",
			error	: function (e) { alert("오류가 발생하였습니다. 관리자에게 문의 바랍니다."); },
			success : function(data) {
				if(data.result == "1") {
					alert("정상적으로 수정되었습니다.");
					document.location.reload();
				}else {
					alert("오류가 발생하였습니다. 관리자에게 문의 바랍니다.");
				}
			}
		});
		return;
	}

</script>

</head>

<body>

	<jsp:include page="../header.jsp" flush="false"/>
	
	<div class="contents-area">
		<header>
			<i class="icon-list"><span>icon</span></i>
			<h2><spring:message code="sauth.owmGradeManagement" /></h2>
		</header>
		<form id="searchSauthForm" name="searchSauthForm" method="post" >
			<input type="hidden" id="s_code" name="s_code" value="${s_code }"/>
			<div class="search-area">
				<select id="searchCompId" name="searchCompId">
					<option value="select"><spring:message code="select.shipOwner" /></option>
					<c:forEach var="ship" items="${compList }">
						<option value="${ship.comp_id}" <c:if test="${ship.comp_id eq sauthVo.searchCompId }">selected</c:if>>
							<c:out value="${ship.comp_name}" />
						</option>
					</c:forEach>
				</select>
				<select id="searchScode" name="searchScode" >
					<option value="select"><spring:message code="select.shipName" /></option>
				</select>
				<select  id="searchGrade" name="searchGrade">
					<c:forEach var="ship" items="${gradeList }">
						<option value="${ship.grade }" <c:if test="${ship.grade eq sauthVo.searchGrade }">selected</c:if>>
							<c:out value="${ship.gname }" />
						</option>
					</c:forEach>
				</select>
			</div>
		</form>
		<div class="board-area" id="boardArea">
			<table class="tbl-default ">
				<colgroup>
					<col style="width:47%;">
					<col style="">
				</colgroup>
				<thead>
					<tr>
						<th scope="col"><spring:message code="cauth.menu" /></th>
						<th scope="col"><spring:message code="cauth.use" /></th>
					</tr>
				</thead>
				<tbody>
					<c:choose>
						<c:when test="${fn:length(searchList) > 0 }">
							<c:forEach var="item" items="${searchList }" varStatus="idx" >
								<tr id="list_${item.gui_code }" >							
				  					<c:choose>
										<c:when test="${sessionScope.language == 'korean' }">
											<td>${item.gui_name }</td>
										</c:when>
										<c:otherwise>
											<td>${item.en_gui_name }</td>
										</c:otherwise>
									</c:choose>					
				  					<td>
				  						<select id="useFlag" name="useFlag">		  						
				  							<option value="Y-${item.gui_code }" <c:if test="${item.use_flag eq 'Y' }">selected</c:if>>
				  								<spring:message code="cauth.enable" />
				  							</option>
				  							<option value="N-${item.gui_code }" <c:if test="${item.use_flag eq 'N' }">selected</c:if>>
				  								<spring:message code="cauth.disable" />
				  							</option>
				  						</select>
				  					</td>
				  				</tr>
							</c:forEach>	
							<c:choose>
								<c:when test="${sessionScope.comp_id == '0' }">		
									<td colspan="2">
										<div class="board-auth-area">
											<div class="btn-area">							
												<div class="fright">
													<button type="button" name="gradeUpdate" onClick="gradeUpdate()" class="btn-lg burgundy">
														<span><spring:message code="button.save" /></span>
													</button>
												</div>
											</div>
										</div>
									</td>
								</c:when>
							</c:choose>
						</c:when>						
						<c:otherwise>
							<tr >
						    	<td colspan="2">
						    		<div class="no-data">
						    			<p><spring:message code="sauth.noShipSelected" /></p>
						    		</div>
						    	</td>
						    </tr>
						</c:otherwise>
					</c:choose>
				</tbody>
			</table>
		</div>
	</div>

	<jsp:include page="../footer.jsp" flush="false"/>

</body>

</html>