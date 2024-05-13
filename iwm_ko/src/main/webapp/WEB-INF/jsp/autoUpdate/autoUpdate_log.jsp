<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>


<!DOCTYPE HTML>

<head>
<!-- meta tag -->
<%@ include file="/decorators/meta.jsp" %>

<title>MVSAT APMS</title>
<script type="text/javascript" src="/web/js/autoUpdate/autoUpdateLog.js"></script>

<script type="text/javascript">
      window.onload = function() {

                  var search = sessionStorage.getItem("search");

                  //검색일경우 selectbox 유지 아닐경우 초기화

                  if (!search) {
                     $('#cs_upd').prop('checked', false);
                      sessionStorage.removeItem("searchCompId");
                      sessionStorage.removeItem("cs_upd");
                      sessionStorage.removeItem("searchSCode");

                  } else {
                      var cs_upd = sessionStorage.getItem("cs_upd") || '${autoUpdateLogVo.cs_upd}';

                      $('#cs_upd').prop('checked', cs_upd === "true");
                      sessionStorage.removeItem("search");
                  }

                 var reCompId = sessionStorage.getItem("searchCompId");
                 var reSCode = sessionStorage.getItem("searchSCode");

                  if (reCompId) {
                      $('#searchCompId').val(reCompId);
                      updateShipNames(reCompId);
                  }
                 if (reSCode) {
                        $('#searchSCode').val(reSCode);
                 }

        };
</script>

</head>


<body>
<jsp:include page="../header.jsp" flush="false"/>
<section class="contents-area">
<header>
	<i class="icon-list"><span>icon</span></i>
	<h2>업데이트 현황</h2>
</header>
<form name="searchAutoUpdateLogForm" id="searchAutoUpdateLogForm" method="post">
	       <input type="hidden" id="pageno" name="pageno" value="${autoUpdateLogVo.pageno }">
	<div class="search-area">

    <label for="searchCompId" class="blind">선주사 선택</label>
            <!-- 선주사 선택 -->
            <select name="searchCompId" id="searchCompId" onChange="updateShipNames(this.value);" class="width5">
                <option value="" ${param.searchCompId == '' ? 'selected' : ''}>선주사</option>
                <c:forEach items="${compList}" var="compItem">
                    <option value="${compItem.comp_id}">${compItem.comp_name}</option>
                </c:forEach>
            </select>
        <div id="ship">
            <label for="searchSCode" class="blind">선박명 입력</label>
             <div id="name_list">
                <select name="searchSCode" id="searchSCode" value="select" onChange="" >
                    <option value="select">선박명</option>
                </select>
            </div>
		</div>
		<label for="cs_upd" class="blind">업데이트 여부 선택</label>
		<span class="check-box1 ml05">
            <input type="checkbox" id="cs_upd" name="cs_upd" value="1"><label for="cs_upd">버전이 다른 S/W 조회</label>
		</span>
            <button type="button" name="search" id="btnSearch" class="btn-md white"><span>검 색</span></button>
	</div>
	<div class="board-area" id="div_list">

         <table class="tbl-default bg-on">
            <caption>자동 업데이트 관리</caption>
            <colgroup>
                <col style="width:10%;">
                <col style="width:10%;">
                <col style="width:10%;">
                <col style="width:10%;">
                <col style="width:10%;">
                <col style="width:10%;">
                <col style="width:10%;">
                <col style="">
            </colgroup>

                    <thead>
                        <tr>
                            <th scope="col">선주사</th>
                            <th scope="col">선박명</th>
                            <th scope="col">선박코드</th>
                            <th scope="col">Package name</th>
                            <th scope="col">최신 버전</th>
                            <th scope="col">현재 버전</th>
                            <th scope="col">마지막 결과</th>
                            <th scope="col">업데이트 시간</th>
                        </tr>
                    </thead>

                      <tbody id="autoUpdateLogTableBody">
                        <c:choose>
                            <c:when test="${fn:length(autoUpdateLogList) > 0 }">
                                <c:forEach var="item" items="${autoUpdateLogList }" varStatus="idx">
                                    <tr>
                                        <td>${item.comp_name}</td>
                                        <td>${item.s_name }</td>
                                        <td>${item.s_code }</td>
                                        <td>${item.p_name }</td>
                                        <td>${item.v1 }</td>
                                        <td>${item.v2 }</td>
                                        <td>${item.last_result }</td>
                                        <td>${item.update_date }</td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="8">
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

	     <div class="board-btm-area">

                    <!-- paging -->

                        ${pagingHTML}

                    <!-- //paging -->

         </div>



</form>
</section>
<jsp:include page="../footer.jsp" flush="false"/>
</body>
</html>