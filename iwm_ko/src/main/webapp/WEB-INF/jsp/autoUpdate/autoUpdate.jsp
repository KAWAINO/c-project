<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE HTML>

<head>

<!-- meta tag -->
<%@ include file="/decorators/meta.jsp" %>

<title>MVSAT APMS</title>
<script type="text/javascript" src="/web/js/autoUpdate/autoUpdate.js"></script>
</head>

<body>
<jsp:include page="../header.jsp" flush="false"/>

    <section class="contents-area">

        <header>
            <i class="icon-list"><span>icon</span></i>
            <h2>자동 업데이트 관리</h2>
        </header>
    <form name="searchAutoUpdateForm" id="searchAutoUpdateForm" method="post">
    <input type="hidden" id="pageno" name="pageno" value="${autoUpdateVo.pageno }">
        <div class="board-area" id="div_contents">
        <div class="scroll-x">

             <table class="tbl-default bg-on">
                        <colgroup>
                            <col style="width:100px;">
                            <col style="width:100px;">
                            <col style="width:250px;">
                            <col style="width:200px;">
                            <col style="width:200px;">
                            <col style="width:70px;">
                            <col style="width:70px;">
                            <col style="width:250px;">
                            <col style="width:200px;">
                            <col style="width:130px;">
                            <col style="width:70px;">
                            <col style="width:150px;">
                            <col style="width:70px;">
                            <col style="width:280px;">
                        </colgroup>


                        <thead>
                            <tr>
                                <th scope="col">이름</th>
                                <th scope="col">버전</th>
                                <th scope="col">파일위치(서버)</th>
                                <th scope="col">파일위치(로컬)</th>
                                <th scope="col">다운로드 위치</th>
                                <th scope="col">프로시저 여부</th>
                                <th scope="col">파일 다운로드 여부</th>
                                <th scope="col">다운로드 파일 위치(서버)</th>
                                <th scope="col">다운로드 파일 위치(로컬)</th>
                                <th scope="col">다운로드 파일 이름</th>
                                <th scope="col">command 실행 여부</th>
                                <th scope="col">command</th>
                                <th scope="col">script 실행 여부</th>
                                <th scope="col">script 파일 위치</th>
                            </tr>
                        </thead>


                          <tbody id="autoUpdateTableBody">
                            <c:choose>
                                <c:when test="${fn:length(autoUpdateList) > 0 }">
                                    <c:forEach var="item" items="${autoUpdateList }" varStatus="idx">
                                        <tr id="list_${item.seq }" onClick="goUpdate('${item.seq}')">
                                            <td>${item.p_name}</td>
                                            <td>${item.version }</td>
                                            <td style="word-break:break-all">${item.file_loc }</td>
                                            <td style="word-break:break-all">${item.local_loc }</td>
                                            <td style="word-break:break-all">${item.down_loc }</td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${item.svr_proc_flag == '0'}">none</c:when>
                                                    <c:when test="${item.svr_proc_flag == '1'}">개별</c:when>
                                                    <c:otherwise>all</c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${item.download_flag == 'Y'}">사용</c:when>
                                                    <c:otherwise>미사용</c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td style="word-break:break-all">${item.down_file_svr }</td>
                                            <td style="word-break:break-all">${item.down_file_cli }</td>
                                            <td>${item.down_file_name }</td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${item.cmd_flag == 'N'}">미실행</c:when>
                                                    <c:otherwise>실행</c:otherwise>
                                                </c:choose>
                                            </td>
                                             <td style="word-break:break-all">${item.cmd_str }</td>
                                             <td>
                                                 <c:choose>
                                                     <c:when test="${item.script_flag == 'N'}">미실행</c:when>
                                                     <c:otherwise>실행</c:otherwise>
                                                 </c:choose>
                                             </td>
                                             <td style="word-break:break-all">${item.script_file }</td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="14">
                                            <div class="no-data">
                                                <p>데이터가 없습니다.</p>
                                            </div>
                                        </td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                          </tbody>
            </table>
        </div>
        </div>
        <div class="board-btm-area">

                <!-- paging -->

                    ${pagingHTML}

                <!-- //paging -->


            <div class="btn-area">
                <div class="fleft">
                    <button type="button" class="btn-lg blue" data-bs-toggle="modal" data-bs-target="#addModal"><span>등 록</span></button>

                </div>
                <div class="fright">
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
                    <form name="addAutoUpdateForm" id="addAutoUpdateForm" method="post">
                            <section class="contents-area">
                                <header>
                                    <i class="icon-write"><span>icon</span></i>
                                    <h2>자동 업데이트 관리</h2>
                                     <i class="btn-lg gray " style="width:75%; ">
                                        <img class=" btn-modal-close" style="margin-left:687px; margin-top:13px; cursor:pointer;" data-bs-dismiss="modal" src="../web/images/common/btn-layer-close.png" >
                                    </i>
                                </header>

                                <div class="board-area">
                                    <table class="tbl-write">
                                        <caption>자동 업데이트 정보 추가 및 수정</caption>
                                        <colgroup>
                                            <col style="width:15%;">
                                            <col style="">
                                            <col style="width:15%;">
                                            <col style="width:35%;">
                                        </colgroup>
                                        <tbody>
                                            <tr>
                                                <th><label for="add_name">이름 <span class="key">*</span></label></th>
                                                <td>
                                                    <input type="text" name="add_name" id="add_name" value="" class="width100">
                                                </td>
                                                 <th><label for="add_version">버전 <span class="key">*</span></label></th>
                                                <td>
                                                    <input type="text" name="add_version" id="add_version" value="" class="width100">
                                                </td>
                                            </tr>
                                            <tr>
                                                <th><label for="add_svr_loc">파일위치(서버) <span class="key">*</span></label></th>
                                                <td colspan="3">
                                                   <input type="text" id="add_svr_loc" name="add_svr_loc" class="width100">
                                                </td>
                                            </tr>
                                            <tr>
                                                <th><label for="add_local_loc">파일위치(로컬) <span class="key">*</span></label></th>
                                                <td colspan="3">
                                                    <input type="text" id="add_local_loc" name="add_local_loc" class="width100">
                                                </td>
                                            </tr>
                                            <tr>
                                                <th><label for="add_down_loc">다운로드 위치 <span class="key">*</span></label></th>
                                                <td colspan="3">
                                                    <input type="text" id="add_down_loc" name="add_down_loc" class="width100">
                                                </td>
                                            </tr>
                                            <tr>
                                                <th><label for="add_proc_flag">프로시저 여부 <span class="key">*</span></label></th>
                                                <td>
                                                    <select name="add_proc_flag" id="add_proc_flag">
                                                        <option value="0">none</option>
                                                        <option value="1">개별</option>
                                                        <option value="2">all</option>
                                                    </select>
                                                </td>
                                                <th><label for="add_down_flag">파일 다운로드 여부 <span class="key">*</span></label></th>
                                                <td>
                                                    <select name="add_down_flag" id="add_down_flag">
                                                        <option value="Y">사용</option>
                                                        <option value="N">미사용</option>
                                                    </select>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th><label for="add_down_svr">다운로드 파일 위치(서버) <span class="key">*</span></label></th>
                                                <td colspan="3">
                                                    <input type="text" id="add_down_svr" name="add_down_svr" class="width100">
                                                </td>
                                            </tr>
                                            <tr>
                                                <th><label for="add_down_local">다운로드 파일 위치(로컬) <span class="key">*</span></label></th>
                                                <td colspan="3">
                                                    <input type="text" id="add_down_local" name="add_down_local" class="width100">
                                                </td>
                                            </tr>
                                            <tr>
                                                <th><label for="add_down_name">다운로드 파일 이름 <span class="key">*</span></label></th>
                                                <td colspan="3">
                                                    <input type="text" id="add_down_name" name="add_down_name"class="width100">
                                                </td>
                                            </tr>
                                            <tr>
                                                <th><label for="add_cmd_flag">command 실행 여부 <span class="key">*</span></label></th>
                                                <td>
                                                    <select name="add_cmd_flag" id="add_cmd_flag">
                                                        <option value="Y">실행</option>
                                                        <option value="N">미실행</option>
                                                    </select>
                                                </td>
                                                <th><label for="add_cmd_str">command <span class="key">*</span></label></th>
                                                <td><input type="text" id="add_cmd_str" name="add_cmd_str"class="width100"></td>
                                            </tr>
                                            <tr>
                                                <th><label for="add_script_flag">script 실행 여부 <span class="key">*</span></label></th>
                                                <td>
                                                    <select name="add_script_flag" id="add_script_flag">
                                                        <option value="Y">실행</option>
                                                        <option value="N"> 미실행</option>
                                                    </select>
                                                </td>
                                                <th><label for="add_script_loc">script 파일 위치 <span class="key">*</span></label></th>
                                                <td>
                                                    <input type="text" id="add_script_loc" name="add_script_loc" class="width100">
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
                                                 <button type="button"  id="btnAdd" class="btn-lg burgundy"><span>저 장</span></button>
                                            <button type="button" name="cancel" class="btn-lg gray btn-modal-close" data-bs-dismiss="modal"><span>취 소</span></button>
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
                    <form name="updAutoUpdateForm" id="updAutoUpdateForm" method="post">
                            <section class="contents-area">
                                <header>
                                    <i class="icon-write"><span>icon</span></i>
                                    <h2>자동 업데이트 관리</h2>
                                     <i class="btn-lg gray " style="width:75%; ">
                                        <img class=" btn-modal-close" style="margin-left:687px; margin-top:13px; cursor:pointer;"  data-bs-dismiss="modal" src="../web/images/common/btn-layer-close.png" >
                                    </i>
                                </header>

                                <div class="board-area">
                                    <table class="tbl-write">
                                        <caption>자동 업데이트 정보 추가 및 수정</caption>
                                        <colgroup>
                                            <col style="width:15%;">
                                            <col style="">
                                            <col style="width:15%;">
                                            <col style="width:35%;">
                                        </colgroup>
                                        <tbody>
                                            <tr>
                                                <th><label for="upd_name">이름 <span class="key">*</span></label></th>
                                                <td>
                                                    <span id="u_upd_name"></span>
                                                    <input type="hidden" name="upd_name" id="upd_name" value="" class="width100" readonly>
                                                </td>
                                                 <th><label for="upd_version">버전 <span class="key">*</span></label></th>
                                                <td>
                                                    <input type="text" name="upd_version" id="upd_version" value="" class="width100">
                                                </td>
                                            </tr>
                                            <tr>
                                                <th><label for="upd_file_loc">파일위치(서버) <span class="key">*</span></label></th>
                                                <td colspan="3">
                                                   <input type="text" id="upd_file_loc" name="upd_file_loc" class="width100">
                                                </td>
                                            </tr>
                                            <tr>
                                                <th><label for="upd_local_loc">파일위치(로컬) <span class="key">*</span></label></th>
                                                <td colspan="3">
                                                    <input type="text" id="upd_local_loc" name="upd_local_loc" class="width100">
                                                </td>
                                            </tr>
                                            <tr>
                                                <th><label for="upd_down_loc">다운로드 위치 <span class="key">*</span></label></th>
                                                <td colspan="3">
                                                    <input type="text" id="upd_down_loc" name="upd_down_loc" class="width100">
                                                </td>
                                            </tr>
                                            <tr>
                                                <th><label for="upd_svr_proc_flag">프로시저 여부 <span class="key">*</span></label></th>
                                                <td>
                                                    <select name="upd_svr_proc_flag" id="upd_svr_proc_flag">
                                                        <option value="0">none</option>
                                                        <option value="1">개별</option>
                                                        <option value="2">all</option>
                                                    </select>
                                                </td>
                                                <th><label for="upd_download_flag">파일 다운로드 여부 <span class="key">*</span></label></th>
                                                <td>
                                                    <select name="upd_download_flag" id="upd_download_flag">
                                                        <option value="Y">사용</option>
                                                        <option value="N">미사용</option>
                                                    </select>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th><label for="upd_down_file_svr">다운로드 파일 위치(서버) <span class="key">*</span></label></th>
                                                <td colspan="3">
                                                    <input type="text" id="upd_down_file_svr" name="upd_down_file_svr" class="width100">
                                                </td>
                                            </tr>
                                            <tr>
                                                <th><label for="upd_down_file_cli">다운로드 파일 위치(로컬) <span class="key">*</span></label></th>
                                                <td colspan="3">
                                                    <input type="text" id="upd_down_file_cli" name="upd_down_file_cli" class="width100">
                                                </td>
                                            </tr>
                                            <tr>
                                                <th><label for="upd_down_file_name">다운로드 파일 이름 <span class="key">*</span></label></th>
                                                <td colspan="3">
                                                    <input type="text" id="upd_down_file_name" name="upd_down_file_name"class="width100">
                                                </td>
                                            </tr>
                                            <tr>
                                                <th><label for="upd_cmd_flag">command 실행 여부 <span class="key">*</span></label></th>
                                                <td>
                                                    <select name="upd_cmd_flag" id="upd_cmd_flag">
                                                        <option value="Y">실행</option>
                                                        <option value="N">미실행</option>
                                                    </select>
                                                </td>
                                                <th><label for="upd_cmd_str">command <span class="key">*</span></label></th>
                                                <td><input type="text" id="upd_cmd_str" name="upd_cmd_str"class="width100"></td>
                                            </tr>
                                            <tr>
                                                <th><label for="upd_script_flag">script 실행 여부 <span class="key">*</span></label></th>
                                                <td>
                                                    <select name="upd_script_flag" id="upd_script_flag">
                                                        <option value="Y">실행</option>
                                                        <option value="N"> 미실행</option>
                                                    </select>
                                                </td>
                                                <th><label for="upd_script_file">script 파일 위치 <span class="key">*</span></label></th>
                                                <td>
                                                    <input type="text" id="upd_script_file" name="upd_script_file" class="width100">
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>


                                <div class="board-btm-area">
                                    <div class="btn-area">
                                        <div class="fleft">
                                             <button type="button" id="btnDel" class="btn-lg yellow"><span>삭 제</span></button>
                                        </div>
                                        <div class="fright">
                                                 <button type="button"  id="btnUpd" class="btn-lg burgundy"><span>저 장</span></button>
                                            <button type="button" name="cancel" class="btn-lg gray btn-modal-close" data-bs-dismiss="modal"><span>취 소</span></button>
                                        </div>
                                    </div>
                                </div>
                             </section>
                         </form>
                     </div>
                </div>
            </div>


</html>