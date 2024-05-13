<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE HTML>

<html>

<head>

<!-- meta tag -->
<%@ include file="/decorators/meta.jsp" %>

<title>MVSAT APMS</title>


<script src="/web/report/highcharts/highcharts.js"></script>
<script src="/web/report/highcharts/modules/exporting.js"></script>
<script type="text/javascript" src="/web/report/highcharts/modules/no-data-to-display.js"></script>
<script type="text/javascript" src="/web/report/canvg/canvg.js"></script>


<script type="text/javascript" src="/web/js/report/report.js"></script>


<script type="text/javascript">

	let downloadExcelMessage = '<spring:message code="confirm.downloadExcel" />';
	let downloadCanceledMessage = '<spring:message code="confirm.downloadCanceled" />';
	let success = '<spring:message code="confirm.success" />';
	let del = '<spring:message code="confirm.delete" />';
	let error = '<spring:message code="confirm.error" />';
	let selectShipName = '<spring:message code="select.shipName" />';
	let selectVesselName = '<spring:message code="confirm.select.vesselName" />';
	let selectDateToLookUp = '<spring:message code="confirm.select.dateToLookUp" />';

</script>

</head>

<body>

<jsp:include page="../header.jsp" flush="false"/>
<section class="contents-area">
<header>
	<i class="icon-stat2"><span>icon</span></i>
	<h2><spring:message code="report.crewUsageHistoryReport" /></h2>
</header>
<form name="searchReportForm" id ="searchReportForm" method="post">

	<!-- 검색 영역 -->
	<div class="search-area">

            <!-- 선주사 선택 -->
            <select name="searchCompId" id="searchCompId" onChange="updateShipNames(this.value);" >
                <option value="" ${param.searchCompId == '' ? 'selected' : ''}>
                	<spring:message code="select.shipOwner" />
                </option>
                <c:forEach items="${compList}" var="compItem">
                    <option value="${compItem.comp_id}">${compItem.comp_name}</option>
                </c:forEach>
            </select>

		<div id="ship" class="width20">
             <div id="name_list">
                <select name="searchSCode" id="searchSCode" class="width100" >
                    <option value="select"><spring:message code="select.shipName" /></option>
                    <c:forEach items="${shipList}" var="shipList">
                        <option value="${shipList.s_code}">${shipList.comp_name} - ${shipList.s_name}</option>
                    </c:forEach>

                </select>
            </div>
		</div>
        <select name="cs_year" id="cs_year">
            <option value="all"><spring:message code="select.year" /></option>
            <c:forEach var="year" items="${years}">
                <option value="${year}" ${year == preYear ? 'selected' : ''}>${year}</option>
            </c:forEach>
        </select><spring:message code="time.year" />

            <select name="cs_month" id="cs_month">
                <option value="all"><spring:message code="select.month" /></option>
                <c:forEach var="i" begin="1" end="12">
                    <c:set var="formattedMonth" value="${i < 10 ? '0' : ''}${i}" />
                    <option value="${formattedMonth}" ${i == preMonth ? 'selected' : ''}>${formattedMonth}</option>
                </c:forEach>
            </select><spring:message code="time.month" />

		&nbsp;&nbsp;
		<button type="button" name="search" id="btnSearch" class="btn-md white">
			<span><spring:message code="button.printReport" /></span>
		</button>
		<!-- <button type="button" name="cs_search" class="btn-md white" onClick="save('pdf')"><span>PDF 출력</span></button> -->
	</div>
	<!-- //검색 영역 -->
</form>
<div class="board-area" id="div_contents"></div>

<form name="imgForm" id="imgForm" action="/report/save_image.do" method="post">
        <input type="hidden" id="imgData" name="imgData">
        <input type="hidden" id="s_year" name="s_year" value="">
        <input type="hidden" id="s_month" name="s_month" value="">
        <input type="hidden" id="cs_comp" name="cs_comp" value="">
        <input type="hidden" id="cs_ship" name="cs_ship" value="">
        <input type="hidden" id="viewer" name="viewer" value="">
        <input type="hidden" id="path" name="path" value="">
</form>

 <div id="container1">

 </div>
</section>
<jsp:include page="../footer.jsp" flush="false"/>
</body>
</html>