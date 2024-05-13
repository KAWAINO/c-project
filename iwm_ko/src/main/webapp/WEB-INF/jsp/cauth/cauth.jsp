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
	
	//console.log('${sessionScope.language}');
	
	$('#searchGrade').on('change', function(){		
		$("#searchCauthForm").attr("action", "/cauth/cauth.do");
        $("#searchCauthForm").submit();
	});

});


function gradeUpdate(grade, use_flag){	
	
	// Controller에서 각각 substring을 하기 위해 변수 2개를 지정
	var useFlag = [];
	var guiCode = [];

	$("select[name=useFlag] option:selected").each(function(){
		useFlag.push(this.value);
	});
	console.log("useFlag : " + useFlag);
	
	$("select[name=useFlag] option:selected").each(function(){
		guiCode.push(this.value);
	});
	//console.log("guiCode : " + guiCode);
	
	// grade
	var grade = $("select[name=searchGrade] option:selected").val();
	console.log("grade : " + grade);

	var datas = $("#searchCauthForm").serialize();
	$.ajax({
		type : "POST",
		url : "/cauth/gradeUpdate.ajax",
		data : {
				 "searchGrade" : grade,
				 "updUseFlag" : useFlag,
				 "updGuiCode" : guiCode
			   },
		dataType : "json",
		error	: function (e) { alert('<spring:message code="confirm.error" />'); },
		success : function(data) {
			if(data.result == "1") {
				alert('<spring:message code="confirm.gradeModified" />');
				document.location.reload();
			}else {
				alert('<spring:message code="confirm.gradeFailed" />');
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
			<h2><spring:message code="cauth.iwmGradeManagement" /></h2>
		</header>
		<form id="searchCauthForm" name="searchCauthForm" method="post" >
			<div class="search-area">
				<select  id="searchGrade" name="searchGrade" >		
					<c:forEach var="cuser" items="${gradeList }" >
						<option value="${cuser.grade }" <c:if test="${cuser.grade eq cauthVo.searchGrade }">selected</c:if>>
							<c:out value="${cuser.gname }" />
						</option>
					</c:forEach>
				</select>
			</div>
		</form>
		<div class="board-area">
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
						</c:when>						
						<c:otherwise>
							<tr>
						    	<td colspan="2">
						    		<div class="no-data">
						    			<p><spring:message code="noData" /></p>
						    		</div>
						    	</td>
						    </tr>
						</c:otherwise>
					</c:choose>
				</tbody>
			</table>
			<c:choose>
				<c:when test="${sessionScope.comp_id == '0' }">	
					<div class="board-btm-area">
						<div class="btn-area">
							<div class="fright">
								<button type="button" name="gradeUpdate" onClick="gradeUpdate()" class="btn-lg burgundy">
									<span><spring:message code="button.save" /></span>
								</button>
							</div>
						</div>
					</div>
				</c:when>
				<c:otherwise>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
	
	<jsp:include page="../footer.jsp" flush="false"/>

</body>

</html>