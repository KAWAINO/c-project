<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE HTML>
<html>
<head>

<!-- meta tag -->
<%@ include file="/decorators/meta.jsp" %>

<title>MVSAT APMS</title>

<script type="text/javascript">
	let downloadExcelMessage = '<spring:message code="confirm.downloadExcel" />';
	let downloadCanceledMessage = '<spring:message code="confirm.downloadCanceled" />';
	let success = '<spring:message code="confirm.success" />';
	let del = '<spring:message code="confirm.delete" />';
	let error = '<spring:message code="confirm.error" />';
	let configuration = '<spring:message code="confirm.exists.configuration" />';
	let configurationName = '<spring:message code="confirm.enter.configurationName" />';
	let value = '<spring:message code="confirm.enter.value" />';
	let deleteConfiguration = '<spring:message code="confirm.delete.configuration" />';
</script>

<script type="text/javascript" src="/web/js/conf/conf.js"></script>

</head>

<body>

<jsp:include page="../header.jsp" flush="false"/>

<section class="contents-area">
<header>
	<i class="icon-list"><span>icon</span></i>
	<h2><spring:message code="conf.configurationManagement" /></h2>
</header>
    <form name="searchConfForm" id="searchConfForm" method="post">
    <input type="hidden" id="pageno" name="pageno" value="${confVo.pageno }">
	<div class="search-area">
        <c:if test="${sessionCompId == '0'}">
           <input type="text" id="searchConf" class="w250" name="searchConf" 
           		placeholder="<spring:message code="search.enterConfigurationName" />" value="${param.searchConf}">
           <button type="button" name="search" id="btnSearch" class="btn-md white">
           		<span><spring:message code="button.search" /></span>
           </button>
        </c:if>
	</div>
	<div class="board-area" id="div_contents">
	 <table class="tbl-default bg-on" id="div_contents">
           <colgroup>
	            <col style="width:25%;">
	            <col style="width:25%;">
	            <col style="">
           </colgroup>
             <thead>
                <tr>
                  <th scope="col"><spring:message code="list.configurationName" /></th>
                  <th scope="col"><spring:message code="list.value" /></th>
                  <th scope="col"><spring:message code="list.description" /></th>
                </tr>
              </thead>
              <tbody id="confTableBody">
                <c:choose>
                    <c:when test="${fn:length(confList) > 0 }">
                        <c:forEach var="item" items="${confList }" varStatus="idx">
                            <tr id="list_${item.conf_name }" onClick="goUpdate('${item.conf_name}')">
                                <td>${item.conf_name}</td>
                                <td>${item.val }</td>
                                 <td><c:out value="${item.descr}" default=""/></td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="3">
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
	    		<div class="fleft">
	    		    <c:if test="${sessionCompId == '0'}">
	    			    <button type="button" class="btn-lg blue" data-bs-toggle="modal" data-bs-target="#addModal">
	    			    	<span><spring:message code="button.add" /></span>
	    			    </button>
	                 </c:if>
	    		</div>
	    		<div class="fright">
	    		    <c:if test="${sessionCompId == '0'}">
	    			    <button type="button" name="excel" id="btnExcel" onClick="" class="btn-lg green">
	    			    	<span><spring:message code="button.exportToExcel" /></span>
	    			    </button>
	                </c:if>
	    		</div>
	    	</div>
	    </div>
	</div>
</form>
</section>
<jsp:include page="../footer.jsp" flush="false"/>
</body>

    <!-- 추가 Modal -->
            <div class="modal fade" id="addModal" data-bs-backdrop="static">
                <div class="modal-dialog modal-lg modal-dialog-centered" role="document">
                 <div class="modal-content">
                    <form name="addConfForm" id="addConfForm" method="post">
                            <section class="contents-area">
                                <header>
                                    <i class="icon-write"><span>icon</span></i>
                                    <h2><spring:message code="conf.configurationManagement" /></h2>
	                                <div class="cancelArea" data-bs-dismiss="modal">
										<img class="cancelX" src="../web/images/common/btn-layer-close.png">
									</div>
                                </header>

                                <div class="board-area">
                                    <table class="tbl-write">
                                        <colgroup>
                                            <col style="width:15%;">
                                            <col style="">
                                            <col style="width:15%;">
                                            <col style="width:35%;">
                                        </colgroup>
                                        <tbody>
                                            <tr>
                                                <th>
                                                	<label for="add_conf">
                                                		<spring:message code="list.configurationName" />
                                                		<span class="key">*</span>
                                                	</label>
                                                </th>
                                                <td>
                                                    <input type="text" name="add_conf" id="add_conf" value="" class="width100">
                                                </td>
                                                 <th>
                                                 	<label for="add_val">
														<spring:message code="list.value" /> 
                                                 		<span class="key">*</span>
                                                 	</label>
                                                 </th>
                                                <td>
                                                    <input type="text" name="add_val" id="add_val" value="" class="width100">
                                                </td>
                                            </tr>
                                            <tr>
                                                <th>
                                                	<label for="add_descr">
														<spring:message code="list.description" /> 
                                                	</label>
                                                </th>
                                                <td colspan="3">
                                                    <div class= "textarea">
                                                    <textarea id = "add_descr" name="add_descr"></textarea>
                                                    </div>
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                                <div class="board-btm-area">
                                    <div class="btn-area">
                                        <div class="fleft">

                                        </div>
                                        <div class="fright">
                                            <c:if test="${sessionCompId == '0'}">
                                                 <button type="button"  id="btnAdd" class="btn-lg burgundy">
                                                 	<span><spring:message code="button.save" /></span>
                                                 </button>
                                            </c:if>
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



    <!-- 수정 Modal -->
    <div class="modal fade" id="updateModal" data-bs-backdrop="static">
        <div class="modal-dialog modal-lg modal-dialog-centered" role="document">
         <div class="modal-content">
             <section class="contents-area">
                <header>
                    <i class="icon-write"><span>icon</span></i>
                    <h2><spring:message code="conf.configurationManagement" /></h2>
                    <div class="cancelArea" data-bs-dismiss="modal">
						<img class="cancelX" src="../web/images/common/btn-layer-close.png">
					</div>
                </header>
            <form name="updConfForm" id="updConfForm" method="post">
             <input type="hidden" id="u_upd_conf" name="u_upd_conf">
                <div class="board-area">
                    <table class="tbl-write">
                         <colgroup>
                         <col style="width:15%;">
                         <col style="">
                         <col style="width:15%;">
                         <col style="width:35%;">
                         </colgroup>

                        <tbody>
                             <tr>
                                 <th>
                                 	<label for="upd_conf">
                                 		<spring:message code="list.configurationName" />
                                 		<span class="key">*</span>
                                 	</label>
                                 </th>
                                 <td>
                                    <span id="upd_conf"></span>
                                 </td>
                                  <th>
	                                  <label for="upd_val">
	                                  	<spring:message code="list.value" />
	                                  	<span class="key">*</span>
	                                  </label>
                                  </th>
                                 <td>
                                     <input type="text" name="upd_val" id="upd_val" value="" class="width100">
                                 </td>
                             </tr>
                             <tr>
                                 <th>
                                 	<label for="upd_descr">
	                                 	<spring:message code="list.description" /> 
	                                 	<span class="key">*</span>
                                 	</label>
                                 </th>
                                 <td colspan="3">
                                     <div class= "textarea">
                                     <textarea id = "upd_descr" name="upd_descr"></textarea>
                                     </div>
                                 </td>
                             </tr>
                         </tbody>

                    </table>
                </div>
                <div class="board-btm-area">
                    <div class="btn-area">
                        <div class="fleft">
                            <button type="button" id="btnDel" class="btn-lg yellow">
                            	<span><spring:message code="button.delete" /></span>
                            </button>
                        </div>
                        <div class="fright">

                            <button type="button" name="btnUpd" id="btnUpd" class="btn-lg burgundy">
                            	<span><spring:message code="button.save" /></span>
                            </button>
                            <button type="button" name="cancel" class="btn-lg gray btn-modal-close" data-bs-dismiss="modal">
                            	<span><spring:message code="button.cancel" /></span>
                            </button>

                        </div>
                    </div>
                </div>
            </form>
             </section>
             </div>
        </div>
    </div>
    
    
</html>

