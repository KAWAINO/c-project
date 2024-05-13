<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>


<!DOCTYPE HTML>
<html>
<head>

<!-- meta tag -->
<%@ include file="/decorators/meta.jsp" %>

<title>MVSAT APMS</title>


<script>

	let success = '<spring:message code="confirm.success" />';
	let error = '<spring:message code="confirm.error" />';
	let del = '<spring:message code="confirm.delete" />';
	let portValue = '<spring:message code="confirm.exists.portValue" />';
	let startPort = '<spring:message code="confirm.enter.startPort" />';
	let endPort = '<spring:message code="confirm.enter.endPort" />';
	let allowedPort = '<spring:message code="confirm.wrong.allowedPort" />';
	let againPort = '<spring:message code="confirm.wrong.port" />';
	let portRange = '<spring:message code="confirm.wrong.portRange" />';
	let deletePort = '<spring:message code="confirm.delete.port" />';
	
	function deleteDport(port_from , port_to) {
	
		if(!confirm(port_from + " ~ " + port_to + " " + deletePort)){
	         return;
	    }
	
	    $.ajax({
	        type: "POST",
	        url: "/dport/dportDelete.ajax",
	        data: "ajax=true&port_from=" + port_from + "&port_to=" + port_to,
	        dataType: "json",
	        error: function (e) {
	            alert(error);
	        },
	        success: function(data) {
	            if(data.result == "1") {
	                alert(del);
	                document.location.reload();
	            } else {
	                alert(error);
	            }
	        }
	    });
	}
</script>

<script type="text/javascript" src="/web/js/dport/dport.js"></script>

</head>

<body>

	<jsp:include page="../header.jsp" flush="false"/>
	
	<section class="contents-area">
		<header>
			<i class="icon-list"><span>icon</span></i>
			<h2><spring:message code="dport.dportManagement" /></h2>
		</header>
		<div class="board-area" id="div_contents">
			<table class="tbl-default bg-on" id="div_contents">
            	<colgroup>
                    <col style="">
                    <col style="width:37%;">
                </colgroup>
                <thead>
                 	<tr>
                    	<th scope="col"><spring:message code="list.allowedPortRange" /></th>
	                    <c:if test="${sessionCompId == '0'}">
                        	<th scope="col"><spring:message code="list.action" /></th>
	                    </c:if>
	                    <c:if test="${sessionCompId != '0'}">
	                        <th scope="col"></th>
	                    </c:if>
                    </tr>
               	</thead>
                <tbody id="dportTableBody">
                	<c:choose>
	                    <c:when test="${fn:length(dportList) > 0}">
	                        <c:forEach var="item" items="${dportList}" varStatus="idx">
	                            <tr>
	                                <td id="port" name="port">${item.port_from} ~ ${item.port_to}</td>
	                                <td>
	                                    <c:if test="${sessionCompId == '0'}">
	                                        <button type="button" name="delete" id="btnDel_${idx.index}" class="btn-sm white" 
	                                        	onclick="deleteDport('${item.port_from}' , '${item.port_to}')">
	                                        	<span><spring:message code="button.delete" /></span>
	                                        </button>
	                                    </c:if>
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
    	</div>
        <div class="board-btm-area">
            <div class="btn-area">
                <div class="fleft">
                     <c:if test="${sessionCompId == '0'}">
			              <button type="button" class="btn-lg blue" data-bs-toggle="modal" data-bs-target="#addModal">
			              	   <span><spring:message code="button.add" /></span>
			              </button>
                    </c:if>
                </div>
                <div class="fright"></div>
            </div>
        </div>

	</section>

	<jsp:include page="../footer.jsp" flush="false"/>
</body>

<!-- 추가 Modal -->
<div class="modal fade" id="addModal" data-bs-backdrop="static">
	<div class="modal-dialog modal-lg modal-dialog-centered" role="document">
		<div class="modal-content">
			<form name="addDportForm" id="addDportForm" method="post">
            	<section class="contents-area">
                	<header>
	                    <i class="icon-write"><span>icon</span></i>
	                    <h2><spring:message code="dport.dportManagement" /></h2>
                        <div class="cancelArea" data-bs-dismiss="modal">
							<img class="cancelX" src="../web/images/common/btn-layer-close.png">
						</div>
                   	</header>
					<div class="board-area">
                    	<table class="tbl-write">
                        	<colgroup>
                            	<col style="width:15%;">
                               	<col style="">
                           	</colgroup>
                            <tbody>
                               	<tr>
                                   	<th>
                                   		<label for="add_port">
                                   			<spring:message code="dport.portValue" /> 
                                   			<span class="key">*</span>
                                   		</label>
                                   	</th>
									<td>
                                    	<input type="text" id="add_from" name="add_from"class="width45"> ~ <input type="text" id="add_to" name="add_to" class="width45">
                                    </td>
                                  </tr>
                            </tbody>
                       	</table>
                   	</div>
                    <div class="board-btm-area">
                    	<div class="btn-area">
                        	<div class="fleft"></div>
                            <div class="fright">
                                 <button type="button"  id="btnAdd" class="btn-lg burgundy">
                                 	<span><spring:message code="button.save" /></span>
                                 </button>
                                 <button type="button" name="cancel" class="btn-lg gray btn-modal-close" data-bs-dismiss="modal">
                                 	<span><spring:message code="button.cancel" /></span>
                                 </button>
                            </div>
                        </div>
                    </div>
                 </section>
             </form>
         </div>
    </div>
</div>


</html>