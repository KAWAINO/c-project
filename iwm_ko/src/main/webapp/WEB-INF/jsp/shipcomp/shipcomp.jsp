<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE HTML>
<html>
<head>


<!-- meta tag -->
<%@ include file="/decorators/meta.jsp" %>
<title>MVSAT APMS</title>
<script type="text/javascript">

	let success = '<spring:message code="confirm.success" />';
	let error = '<spring:message code="confirm.error" />';
	let del = '<spring:message code="confirm.delete" />';
	let shipOwnerNameAgain = '<spring:message code="confirm.exists.shipOwnerName" />';
	let shipOwnerName = '<spring:message code="confirm.enter.shipOwnerName" />';
	let numberOfShips = '<spring:message code="confirm.enter.numberOfShips" />';
	let deleteShipOwner = '<spring:message code="confirm.delete.shipOwner" />';
	let downloadExcelMessage = '<spring:message code="confirm.downloadExcel" />';
	let downloadCanceledMessage = '<spring:message code="confirm.downloadCanceled" />';
	
</script>


<script type="text/javascript" src="/web/js/shipComp/shipComp.js"></script>

</head>
<body>
<jsp:include page="../header.jsp" flush="false"/>
<section class="contents-area">
<header>
	<i class="icon-list"><span>icon</span></i>
	<h2><spring:message code="shipComp.shipOwnerStatus" /></h2>
</header>
<form name="searchShipCompForm" id="searchShipCompForm" method="post">
 <input type="hidden" id="pageno" name="pageno" value="${shipCompVo.pageno }">
 <input type="hidden" id="sessionCompId" name="sessionCompId" value="${shipCompVo.sessionCompId }">
	<!-- 검색 영역 -->
	<div class="search-area">
            <input type="text" id="searchShipComp" class="w250" name="searchShipComp" 
            	placeholder="<spring:message code="search.enterShipName" />" value="${shipCompVo.searchShipComp}">
		<button type="button" name="search" id="btnSearch" class="btn-md white">
			<span><spring:message code="button.search" /></span>
		</button>
	</div>

         <div id ="div_contents" class="board-area" style="display: block;">
	      <table class="tbl-default bg-on">

                                  <colgroup>
                                        <col style="width:25%;">
                                        <col style="width:10%;">
                                        <col style="width:10%;">
                                        <col style="width:10%;">
                                        <col style="">

                                  </colgroup>

                                 <thead>
                                    <tr>
                                      <th scope="col"><spring:message code="list.shipOwnerName" /></th>
                                      <th scope="col"><spring:message code="list.numberOfShips" /></th>
                                      <th scope="col"><spring:message code="list.owmStatus" /></th>
                                      <th scope="col"><spring:message code="list.wapStatus" /></th>
                                      <th scope="col"><spring:message code="list.description" /></th>

                                    </tr>
                                  </thead>
                                  <tbody id="shipCompTableBody">
                                    <c:choose>
                                        <c:when test="${fn:length(shipCompList) > 0 }">
                                            <c:forEach var="item" items="${shipCompList }" varStatus="idx">
                                                <tr id="list_${item.comp_id }" onClick="goUpdate('${item.comp_id}')">
                                                    <td>${item.comp_name}</td>
                                                    <td>${item.ship_hold }</td>
                                                    <td>${item.s_cnt }</td>
                                                    <td>${item.a_cnt }</td>
                                                    <td>${item.descr}</td>
                                                </tr>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            <tr>
                                                <td colspan="5">
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
		<div class="fleft">
           <c:if test="${shipCompVo.sessionCompId == '0'}">
			<button type="button" class="btn-lg blue" data-bs-toggle="modal" data-bs-target="#addModal">
				<span><spring:message code="button.add" /></span>
			</button>
           </c:if>
		</div>
		<div class="fright">
			<button type="button" name="excel" id="btnExcel" onClick="" class="btn-lg green">
				<span><spring:message code="button.exportToExcel" /></span>
			</button>
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
         <section class="contents-area">
            <header>
                <i class="icon-write"><span>icon</span></i>
                <h2><spring:message code="shipComp.shipOwnerStatus" /></h2>
                 <div class="cancelArea" data-bs-dismiss="modal">
							<img class="cancelX" src="../web/images/common/btn-layer-close.png">
						</div>
            </header>


        <form name="addShipCompForm" id="addShipCompForm" method="post">
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
                            	<label for="addCompName">
                            		<spring:message code="list.shipOwnerName" /> 
                            		<span class="key">*</span>
                            	</label>
                            </th>
                            <td>
                            	<input type="text" id="addCompName" name="addCompName" value="" class="width100">
                            </td>
                            <th>
                            	<label for="addCompCnt">
                            		<spring:message code="list.numberOfShips" /> 
                            		<span class="key">*</span>
                            	</label>
                            </th>
                            <td>
                            	<input type="text" id="addCompCnt" name="addCompCnt" value="" class="width100">
                            </td>
                        </tr>
                        <tr>
                            <th><label for="addDesc"><spring:message code="list.description" /> </label></th>
                            <td colspan=3>
                                <div class="textarea">
                                    <textarea id="addDescr" name="addDescr"></textarea>
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
                        <c:if test="${comp_id == '0'}">
                          <button type="button" name="btn_shipcomp_add" id="btnAdd" class="btn-lg burgundy">
                          	<span><spring:message code="button.save" /></span>
                          </button>
                        </c:if>
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

<!-- 수정 Modal -->
<div class="modal fade" id="updateModal" data-bs-backdrop="static">
    <div class="modal-dialog modal-lg modal-dialog-centered" role="document">
     <div class="modal-content">
         <section class="contents-area">
            <header>
                <i class="icon-write"><span>icon</span></i>
                <h2><spring:message code="shipComp.shipOwnerStatus" /></h2>
                                    <div class="cancelArea" data-bs-dismiss="modal">
							<img class="cancelX" src="../web/images/common/btn-layer-close.png">
						</div>
            </header>
        <form name="updShipCompForm" id="updShipCompForm" method="post">
         <input type="hidden" id="u_updCompName" name="u_updCompName">
         <input type="hidden" id="updCompId" name="updCompId">
         <input type="hidden" id="chkDel" name="chkDel">
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
                            	<label for="updCompName">
                            		<spring:message code="list.shipOwnerName" />  
                            		<span class="key">*</span>
                            	</label>
                            </th>
                            <td><input type="text" id="updCompName" name="updCompName" value="" class="width100"></td>
                            <th>
                            	<label for="updCompHold">
                            		<spring:message code="list.numberOfShips" /> 
                            		<span class="key">*</span>
                            	</label>
                            </th>
                            <td><input type="text" id="updCompHold" name="updCompHold" value="" class="width100"></td>
                        </tr>
                        <tr>
                            <th><label for="updDescr"><spring:message code="list.description" /></label></th>
                            <td colspan=3>
                                <div class="textarea">
                                    <textarea id="updDescr" name="updDescr"></textarea>
                                </div>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
            <div class="board-btm-area">
                <div class="btn-area">
                    <div class="fleft">
                       <c:if test="${shipCompVo.sessionCompId == '0'}">
                        <button type="button" id="btnDel" class="btn-lg yellow">
                        	<span><spring:message code="button.delete" /></span>
                        </button>

                      </c:if>
                    </div>
                    <div class="fright">
                          <c:if test="${shipCompVo.sessionCompId == '0'}">
                            <button type="button" name="btn_shipcomp_add" id="btnUpd" class="btn-lg burgundy">
                            	<span><spring:message code="button.save" /></span>
                            </button>
                          </c:if>
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