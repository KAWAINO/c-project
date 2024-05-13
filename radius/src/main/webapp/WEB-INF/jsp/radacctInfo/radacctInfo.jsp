<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<!-- meta tag -->
<%@ include file="/decorators/meta.jsp" %>
<!-- //meta tag -->
<meta charset="UTF-8">
<title>RADIUS</title>

<script type="text/javascript">

	$(document).ready(function() { 	
	
		// causeAllChk  
		$("#statusAllChk").click(function() {
			
			if($(this).is(":checked")) {
				$("input[name=statusMultiCheck]").prop("checked", true);
			} else {
				$("input[name=statusMultiCheck]").prop("checked", false);
			}
		});

		$("input[name=statusMultiCheck]").click(function() {
			
			var all = $("input[name=statusMultiCheck]").length;
			var chked = $("input[name=statusMultiCheck]:checked").length;
			var chkedCount = 0;

			if(all == chked) {
				$("#statusAllChk").prop("checked", true);
			} else {
				$("#statusAllChk").prop("checked", false); 
			}
		});
		// acctStatus 체크상태
		var dataArray = document.getElementById("searchAcctStatus").value
	    /* console.log(dataArray); */

	    if(dataArray !== 'null'){
	       var checkboxes = document.getElementsByName('statusMultiCheck');
	       var checkedCount = 0;

	       for(var i=0; i < checkboxes.length; i++) {
	       	   if(dataArray.includes(checkboxes[i].value) || dataArray.includes() || dataArray.includes('전체 항목')) {
	               checkboxes[i].checked = true;
	               checkedCount++;
	           }
	       	   /* console.log(dataArray); */
	       	   if(dataArray.includes('전체 항목')) {
		   	   	   $("#statusAllChk").prop("checked", true);
		   	   }
	       }
	       if(checkedCount === checkboxes.length) {
	           document.getElementById("searchAcctStatus").value = '전체 항목';
	       } 
	    };
	    
	    // 검색
	    $("#btnSearch").bind("click", function() {		
			$("#searchRadacctInfoForm").attr("action", "/radacctInfo/radacctInfo.do");
			$("#searchRadacctInfoForm").submit();	
		});
	    
	    // 초기화
	    $("#btnReset").bind("click", function() {
	    	$("#searchUserName").val("");	
	    	$("#searchImsi").val("");	
	    	$("#searchMsisdn").val("");	
	    	$("#searchAcctStatus").val("전체 항목");	
	        $('input:checkbox[name=statusAllChk]').prop("checked", true);
	        $('input:checkbox[name=statusMultiCheck]').prop("checked", true);
	    });
	    
	 	// excel
		$("#btnExcel").bind("click", function() {
			goExcelDown();
		});
	    
	});
	
	// acctStatus 드롭다운
	var acctStatus = false;

	function selectStatusList() {
		
	    var div = document.getElementById("acctStatusDiv");
	    var search = document.getElementById("searchAcctStatus");
	    var checkboxes = document.getElementsByName('statusMultiCheck');

	    var chkArray = [];
	    $('input:checkbox[name="statusMultiCheck"]:checked').each(function() {
	        chkArray.push("'" + this.getAttribute('nm') + "'" );
	    });
	    var List = chkArray.join(",");
	    /* console.log("selectStatList ::"  + List); */
		
	    if (chkArray.length > 0) {
	        if(chkArray.length === checkboxes.length) {
	        	search.value = '전체 항목';
	        } else {
	        	search.value = chkArray.join(",");
	        }      
	    } else {
	    	search.value = '전체 항목';
	    }
	    if (!acctStatus) {
	        div.style.display = "block";
	        acctStatus = true;
	    } else {
	        div.style.display = "none";
	        acctStatus = false;
	    }
	};
	
	//paging
	function goPage(pageno) {
		$("#pageno").val(pageno);
		$("#searchChecked").val("true");
        $("#chkedAcctStatusList").val("true");
		$("#searchChecked").val("true");
		$("#searchRadacctInfoForm").attr("action", "/radacctInfo/radacctInfo.do");
		$("#searchRadacctInfoForm").submit();
	}
	
	//엑셀 다운로드
	function goExcelDown() {
		$("#searchRadacctInfoForm").attr("action", "/radacctInfo/radacctInfoExcelDownload.do");
	    $("#searchRadacctInfoForm").submit();
	}
	
</script>
</head>

<body>

<!-- header -->
<%@ include file="/decorators/header.jsp"%>
<!-- //header -->

	<div class="container min-vh-100">
		<ol class="breadcrumb mt-4 py-3 fs-5">
			<li class="breadcrumb-item active">상태</li>
			<li class="breadcrumb-item active">인증 현황</li>
		</ol>
		<!-- 검색 -->
		<div class="card border-primary mb-4">
			<div class="card-header mb-4">상세검색</div>
			<div class="card-body">
				<form name="searchRadacctInfoForm" id=searchRadacctInfoForm method="post">
					<input type="hidden" id="pageno" name="pageno" value="${radacctInfoVo.pageno }"/>
					<input type="hidden" id="searchChecked" name="searchChecked" value="false">	
					<input type="hidden" id="chkedAcctStatusList" name="chkedAcctStatusList" value="true">		
					<div class="row">	
						<label class="col-form-label mb-1 col-1 offset-12-radacct-1"  for="searchUserName" >Username</label>
						<div class="col-15-radacct-0" >
							<input type="text" class="form-control" id="searchUserName" name="searchUserName" value="${radacctInfoVo.searchUserName }">
						</div>
						<label class="col-form-label mb-1 col-1 offset-12-radacct-1" for="searchImsi" >IMSI</label>	
						<div class="col-15-radacct-2" >
							<input type="text" class="form-control" id="searchImsi" name="searchImsi" value="${radacctInfoVo.searchImsi }">
						</div>
						<label class="col-form-label col-1 mb-1 offset-12-radacct-1" for="searchMsisdn" >MSISDN</label>		
						<div class="col-15-radacct-1">
							<input type="text" class="form-control" id="searchMsisdn" name="searchMsisdn" value="${radacctInfoVo.searchMsisdn }">
						</div>
						<label class="col-form-label col-1-radacct mb-1 offset-12-radacct-1"  >Account Status</label>		
						<div class="col-15-radacct-5">
							<input class="form-select-check" type="text" id="searchAcctStatus" name="searchAcctStatus" readonly="readonly"
							onclick="selectStatusList()" value="${radacctInfoVo.searchAcctStatus != null ? radacctInfoVo.searchAcctStatus : '전체 항목'}">
							<div id="acctStatusDiv" class="multi form-control" style="display: none; position: absolute; width: 13.5%;"> 
								<div class="mt-1 col-12">
									<div class="mb-2">
										<label>
											<input type="checkbox" class="form-check-input" id="statusAllChk" name="statusAllChk" value="전체">
											<small>전체</small>
										</label>
									</div>
									<div>					
										<label for="start">
											<input type="checkbox" class="form-check-input" id="start" name="statusMultiCheck" nm="Start" value="Start">
											<small>Start</small>
										</label>	
									</div>
									<div>					
										<label for="Interim-Update">
											<input type="checkbox" class="form-check-input" id="Interim-Update" name="statusMultiCheck" 
												nm="Interim-Update" value="Interim-Update">
											<small>Interim-Update</small>
										</label>	
									</div>
									<div>					
										<label for="stop">
											<input type="checkbox" class="form-check-input" id="stop" name="statusMultiCheck" 
												nm="Stop" value="Stop">
											<small>Stop</small>
										</label>	
									</div>
								</div>
									<%-- <c:forEach var="statusList" items="${acctStatusList }">
										<label for="checkbox_${statusList.acctStatus}">
											<input type="checkbox" class="form-check-input" nm="${statusList.acctStatus}"
												id="checkbox_${statusList.acctStatus}" name="statusMultiCheck" value="${statusList.acctStatus}" />
											<small>${statusList.acctStatus}</small>
										</label>
									</c:forEach>
								</div> --%>
								<div style="margin-top : 5%; font-size:12px;"> 
	        						<input type="button" class="form-control btn btn-primary" 
	        							id="statusChk" style=";"value="확인" onclick="selectStatusList()"/> 
	    						</div> 
							</div>	
							<%-- <input type="text" class="form-control" id="searchAcctStatus" name="searchAcctStatus" value="${radacctInfoVo.searchAcctStatus }"> --%>
						</div>
					</div>
					<div class="row">
						<div class="row mt-5 mb-6 card-footer" style=margin-left:0.05rem></div>
						<div class="search-footer" align="center">
							<button type="button" class="btn btn-primary btn-search mb-2 " id="btnSearch" >조회</button>
							<button type="button" class="btn btn-outline-primary btn-search mb-2 " id="btnReset">초기화</button>
						</div>	
					</div>
				</form>
			</div>
		</div>
		<div class="container px-0">
			<div class="row justify-content-between">
				<div class="col-3 mb-6 scroll-x">
					<h5>조회 결과</h5>
				</div>
				<div class="col-3" align="right">
					<button type="button" class="btn btn-outline-primary btn-addel mb-6" id="btnExcel">엑셀 다운로드</button>		
				</div>
			</div>
		</div>
		
		<div class="row">
			<div class="col-12">
				<div class="card-header left-table mb-3">
					<div class=" scroll-x" >
						<table class="table table-hover table-bordered mt-3">
							<colgroup>
								<col style="width:120px;"> <!-- acctSessionId -->
								<col style="width:150px;"> <!-- userName -->
								<col style="width:180px;"> <!-- gppImsi -->
								<col style="width:180px;"> <!-- msisdn -->
								<col style="width:150px;"> <!-- groupName -->
								<col style="width:150px;"> <!-- realm -->
								<col style="width:180px;"> <!-- nasIpAddress -->
								<col style="width:150px;"> <!-- nasPortId -->
								<col style="width:150px;"> <!-- nasPortType -->
								<col style="width:200px;"> <!-- acctUpdateTime -->
								<col style="width:150px;"> <!-- acctInterval -->
								<col style="width:150px;"> <!-- acctSessionTime -->
								<col style="width:150px;"> <!-- acctAuthentic -->
								<col style="width:180px;"> <!-- connectInfoStart -->
								<col style="width:180px;"> <!-- connectInfoStop -->
								<col style="width:150px;"> <!-- acctInputOctets -->
								<col style="width:150px;"> <!-- acctOutputOctets -->
								<col style="width:150px;"> <!-- acctInputPackets -->
								<col style="width:150px;"> <!-- acctOutputPackets -->
								<col style="width:200px;"> <!-- calledStationId -->
								<col style="width:200px;"> <!-- callingStationId -->
								<col style="width:160px;"> <!-- acctStatus -->
								<col style="width:160px;"> <!-- acctTerminateCause -->
								<col style="width:160px;"> <!-- serviceType -->
								<col style="width:180px;"> <!-- framedProtocol -->
								<col style="width:180px;"> <!-- framedIpAddress -->
								<col style="width:160px;"> <!-- gppSgsnAddress -->
								<col style="width:160px;"> <!-- gppGgsnAdress -->
								<col style="width:160px;"> <!-- gppChargingId -->
							</colgroup>
							<thead>
								<tr class="table-primary scroll-x">
									<th scope="col">SessionID</th>
									<th scope="col">Username</th>
									<th scope="col">IMSI</th>
									<th scope="col">MSISDN</th>
									<th scope="col">Groupname</th>
									<th scope="col">Realm</th>
									<th scope="col">NAS IP</th>
									<th scope="col">NAS Port IP</th>
									<th scope="col">NAS Port type</th>
									<th scope="col">Update time</th>
									<th scope="col">Interval</th>
									<th scope="col">Session time</th>
									<th scope="col">Authentic</th>
									<th scope="col">Connect Info Start</th>
									<th scope="col">Connect Info Stop</th>
									<th scope="col">Input Octets</th>
									<th scope="col">Output Octets</th>
									<th scope="col">Input Packets</th>
									<th scope="col">Output Packets</th>
									<th scope="col">Called Station ID</th>
									<th scope="col">Calling Station ID</th>
									<th scope="col">Status</th>
									<th scope="col">Terminate Cause</th>
									<th scope="col">Service Type</th>
									<th scope="col">Framed Protocol</th>
									<th scope="col">Framed IP</th>
									<th scope="col">GPP SGSN</th>
									<th scope="col">GPP GGSN</th>
									<th scope="col">GPP Charging ID</th>
								</tr>
							</thead>
							<tbody >
								<c:choose>
									<c:when test="${fn:length(radacctInfoList) > 0 }">
										<c:forEach var="item" items="${radacctInfoList }" varStatus="idx">		
											<tr >	
												<td>${item.acctSessionId }</td>
												<td>${item.userName }</td>
												<td>${item.gppImsi }</td>
												<td>${item.msisdn }</td>
												<td>${item.groupName }</td>
												<td>${item.realm }</td>
												<td>${item.nasIpAddress }</td>
												<td>${item.nasPortId }</td>
												<td>${item.nasPortType }</td>
												<td>${item.acctUpdateTime }</td>
												<td><fmt:formatNumber type="number" maxFractionDigits="3" value="${item.acctInterval }"/></td>
												<td><fmt:formatNumber type="number" maxFractionDigits="3" value="${item.acctSessionTime }"/></td>
												<td>${item.acctAuthentic }</td>
												<td>${item.connectInfoStart }</td>
												<td>${item.connectInfoStop }</td>
												<td><fmt:formatNumber type="number" maxFractionDigits="3" value="${item.acctInputOctets }"/></td>
												<td><fmt:formatNumber type="number" maxFractionDigits="3" value="${item.acctOutputOctets }"/></td>
												<td>${item.acctInputPackets }</td>
												<td>${item.acctOutputPackets }</td>
												<td>${item.calledStationId }</td>
												<td>${item.callingStationId }</td>
												<td>${item.acctStatus }</td>
												<td>${item.acctTerminateCause }</td>
												<td>${item.serviceType }</td>
												<td>${item.framedProtocol }</td>
												<td>${item.framedIpAddress }</td>
												<td>${item.gppSgsnAddress }</td>
												<td>${item.gppGgsnAdress }</td>
												<td>${item.gppChargingId }</td>
											</tr>
										</c:forEach>
									</c:when> 
									<c:otherwise>
	                                	<tr >
		                                	<td colspan="29" >       
		                                        <div class="my-4">
		                                           	데이터가 없습니다.
		                                        </div>                               
		                     				</td>
	                                    </tr>
	                            	</c:otherwise>
								</c:choose>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
<!-- paging -->
${pagingHTML}
<!-- paging -->

	</div>

<!-- footer -->
<%@ include file="/decorators/footer.jsp"%>
<!-- //footer -->	
</body>
</html>