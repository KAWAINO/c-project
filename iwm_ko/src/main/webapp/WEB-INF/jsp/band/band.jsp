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
	let bandValue = '<spring:message code="confirm.exists.bandValue" />';
	let bandValueNumber = '<spring:message code="confirm.wrong.bandValueNumber" />';
	let deleteBandWidth = '<spring:message code="confirm.delete.bandWidth" />';
	let del = '<spring:message code="confirm.delete" />';
	let noBandValue = '<spring:message code="confirm.enter.noBandValue" />';

	function deleteBand(band_width) {
	
	    if(!confirm(band_width + " " + deleteBandWidth)){
	        return;
	    }
	
	    $.ajax({
	        type: "POST",
	        url: "/band/bandDelete.ajax",
	        data: "ajax=true&band_width=" + band_width,
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
<script type="text/javascript" src="/web/js/band/band.js"></script>

</head>
<body>

	<jsp:include page="../header.jsp" flush="false"/>
	
	<section class="contents-area">
		<header>
			<i class="icon-list"><span>icon</span></i>
			<h2><spring:message code="band.banWidthManagement" /></h2>
		</header>
		<div class="board-area" id="div_contents">
			 <table class="tbl-default bg-on" id="div_contents">
		     	<colgroup>
	                <col style="">
	                <col style="width:37%;">
	            </colgroup>
	            <thead>
	            	<tr>
	                	<th scope="col"><spring:message code="list.bandWidth" /></th>
	                  	<th scope="col"><spring:message code="list.action" /></th>
	               	</tr>
	            </thead>
	            <tbody id="bandTableBody">
		              <c:choose>
			              <c:when test="${fn:length(bandList) > 0}">
			                  <c:forEach var="item" items="${bandList}" varStatus="idx">
			                      <tr>
			                          <td id="band_width" name="band_width">${item.band_width}</td>
			                          <td>
			                              <c:if test="${sessionCompId == '0'}">
			                                  <button type="button" name="delete" id="btnDel_${idx.index}" 
			                                  	class="btn-sm white" onclick="deleteBand('${item.band_width}')">
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
			                              <p><spring:message code="nodata" /></p>
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
				<form name="addBandForm" id="addBandForm" method="post">
	                 <section class="contents-area">
	                     <header>
	                     	<i class="icon-write"><span>icon</span></i>
	                        <h2><spring:message code="band.banWidthManagement" /></h2>
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
		                                	<label for="add_band">
		                                		<spring:message code="band.bandWidthValue" />
		                                		<span class="key">*</span>
		                                	</label>
		                                </th>
		                                <td>
		                                    <input type="text" name="add_band" id="add_band" value="" class="width100">
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