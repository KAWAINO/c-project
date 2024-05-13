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

<script type="text/javascript" src="/web/js/jquery-ui-1.12.1/jquery-ui.js"></script>
<script type="text/javascript" src="/web/js/highcharts/highcharts.js"></script>
<script type="text/javascript" src="/web/js/highcharts/modules/exporting.js"></script>
<script type="text/javascript" src="/web/js/highcharts/modules/no-data-to-display.js"></script>

<script type="text/javascript" >

$(document).ready(function(){
	
	// 비밀번호 변경 확인
	let pwdLimitDays = '${sessionScope.pwdLimitDays}';
	let pwdChangeCheck = '${sessionScope.pwdChangeCheck}';
	//console.log(pwdLimitDays);
	//console.log(pwdChangeCheck);
	passwordChangeCheck(pwdLimitDays,pwdChangeCheck);
	
	// 선박 리스트
	getShipList();
	
	//getLastShipChart();
	//getTime();
	
	let language = localStorage.getItem('langFlag');
	//console.log('language : ' + language);
	$.ajax({
        type: "POST",
        url: "../monitor/monitor.do",
        data: { language : language }
    });
	
	// "선주사 All체크박스" 클릭 이벤트
	$('#compListAllChk').click(function() {
	    let isChecked = $(this).is(':checked');
	    $("input[name=chkCompId]").prop('checked', isChecked);
	    
	    let searchCompId = isChecked ? ["전체"] : [''];
	    updateShipList(searchCompId);     
	});

	// "선주사 체크박스" 클릭 이벤트
	$("input[name=chkCompId]").click(function() {
	    let all = $("input[name=chkCompId]").length;
	    let checked = $("input[name=chkCompId]:checked").length;
	    
	    $("#compListAllChk").prop("checked", all === checked);

	    let searchCompId = [];
	    $('input:checkbox[name="chkCompId"]:checked').each(function() {
	        searchCompId.push(this.value);
	    });
	    updateShipList(searchCompId);
	});

	// 동적으로 생성된 "선박 리스트 전체 선택 체크박스" 클릭 이벤트 위임
	$(document).on('change', '#shipListAllChk', function() {
	    let isChecked = $(this).is(':checked');
	    $("input[name=chkShipCode]").prop('checked', isChecked);
	});

	// 동적으로 생성된 "선박 리스트 개별 체크박스" 클릭 이벤트 위임
	$(document).on('change', 'input[name="chkShipCode"]', function() {

	    let all = $("input[name=chkShipCode]").length;
	    let checked = $("input[name=chkShipCode]:checked").length;
	    
	    $('#shipListAllChk').prop('checked', all === checked);
	});
	
	// 선박별 데이터 사용 현황 조회 클릭 이벤트
	$("#getShip").bind('click', function() {
		getLastShipChart();
    	getTime();
	}); 	

	// 선박별 데이터 사용 추이 조회 클릭 이벤트
	$("#getTimeChart").bind('click', function() {
    	getTime();
	});
	
	$('#searchUnit, #searchRange').on('change', updateSearchOptions);	
		
});

function getShipList(){
	let searchCompId = [];
    $('input:checkbox[name="chkCompId"]:checked').each(function() {
    	searchCompId.push(this.value);
    });
    //console.log(searchCompId);
    updateShipList(searchCompId);
} 

function updateShipList(searchCompId) {
	//console.log(searchCompId);
	$.ajax({
        type: "POST",
        url: "/monitor/shipList.ajax",
        data: { 'searchCompId': searchCompId },
        dataType: "json",
        async: false,
        error: function(e) { 
            alert('<spring:message code="confirm.error" />');
        },      
        success: function(data) {
        	let shipOptions = '';
        	shipOptions = '<li><span class="check-box1"><input type="checkbox" id="shipListAllChk" name="shipListAllChk" value="전체" checked/>' +
            					'<label for="shipListAllChk"><spring:message code="select.all" /></label></span></li>';
        	if (searchCompId != null || data.allShips != null) {    
	            for (let i = 0; i < data.length; i++) {
	                	
	                let s_code = data[i].s_code;
		            let s_name = data[i].s_name;
		            
		        	shipOptions += '<li><span class="check-box1">' +
				                    '<input type="checkbox" name="chkShipCode" id="chkShipCode_' + s_code + '" value="' + s_code + '" checked/>' +
				                    '<label for="chkShipCode_' + s_code + '">' + s_name + '</label></span></li>';
	            } 
        	} else {
        		shipOptions = '<li><span class="check-box1"><input type="checkbox" id="shipListAllChk" name="shipListAllChk" value="전체" checked/>' +
				'<label for="shipListAllChk"><spring:message code="select.all" /></label></span></li>';
        	}	
            $('#shipListoptions').html(shipOptions);    
        }
    });
}

function getSelectedValues() {
    return {
        searchUnit: $('#searchUnit option:selected').val(),
        searchRange: $('#searchRange option:selected').val(),
        searchTime: $('#searchTime option:selected').val()
    };
}

function updateSearchOptions(){
	
	const { searchUnit, searchRange, searchTime } = getSelectedValues();
    let startDefaultDate = '${startDefaultDate}';
    let endDefaultDate = '${endDefaultDate}';
    
    let result = '';
    
    if (searchRange === 'first') {
        if (searchUnit === 'hour' || searchUnit === 'day') {
            result = '<select name="searchTime" id="searchTime">';
            for (let i = 1; i <= 31; i++) {
                result += '<option value="' + (i < 10 ? "0" + i : i) + '" ' + (i === searchTime ? 'selected' : '') + '>' + i + '</option>';
            }
            result += '</select>';
            result += '&nbsp;<spring:message code="graph.day" />&nbsp;';
        } else if (searchUnit === 'month') {
            result = '<select name="searchTime" id="searchTime">';
            for (let i = 1; i <= 12; i++) {
                result += '<option value="' + (i < 10 ? "0" + i : i) + '" ' + (i === searchTime ? 'selected' : '') + '>' + i + '</option>';
            }
            result += '</select>';
            result += '&nbsp;<spring:message code="graph.month" />&nbsp;';
        }
    } else {
    	if(searchUnit === 'hour'){
    		result = '<input type="text" readonly name="startDate" id="startDate" value="' + startDefaultDate + '" class="inp-date inp-date-picker w150">';
    		result += '<select name="startHour" id="startHour">';
    		
			for (let i = 0; i <= 23; i++) {
				const fmtSearchTime = i < 10 ? "0" + i : i; 
                result += '<option value="' + (i < 10 ? "0" + i : i) + '" ' + (i === searchTime ? 'selected' : '') + '>' + fmtSearchTime + '</option>';
            }
            result += '</select>';
            result += '&nbsp;<spring:message code="graph.hour" />&nbsp;~&nbsp;';
            result += '<input type="text" readonly name="endDate" id="endDate" value="' + endDefaultDate + '" class="inp-date inp-date-picker w150">';
    		result += '<select name="endHour" id="endHour">';
    		
			for (let i = 0; i <= 23; i++) {
				const fmtSearchTime = i < 10 ? "0" + i : i; 
                result += '<option value="' + (i < 10 ? "0" + i : i) + '" ' + (i === searchTime ? 'selected' : '') + '>' + fmtSearchTime + '</option>';
            }
            result += '</select>';
            result += '&nbsp;<spring:message code="graph.hour" />';
    	} else{
    		result = '<input type="text" readonly name="startDate" id="startDate" value="' + startDefaultDate + '" class="inp-date inp-date-picker w150">';
    		result += '&nbsp;~&nbsp;';
    		result += '<input type="text" readonly name="endDate" id="endDate" value="' + endDefaultDate + '" class="inp-date inp-date-picker w150">';
    	}
    }
    $('#time_list').html(result); 
    $('#endHour').val('23');
}

function passwordChangeCheck(pwdLimitDays,pwdChangeCheck){
	if(pwdChangeCheck === 'false'){
		alert('<spring:message code="confirm.alarm.changePassword1" />' 
				+ pwdLimitDays + '<spring:message code="confirm.alarm.changePassword2" />');
	}
}

</script>
</head>
<body>

	<jsp:include page="../header.jsp" flush="false"/>
	
	<div class="contents-area">
		<header>
			<i class="icon-stat2"><span>icon</span></i>
			<h2><spring:message code="graph.dataUsagebyShip" /></h2>
		</header>
		<form name="searchMonitorForm" id="searchMonitorForm" method="post">
			<input type="hidden" id="excelMsg" name="excelMsg" value="">
			<input type="hidden" id="searchChked" name="searchChked" value="false"/>
			<input type="hidden" id="sessionLanguage" name="sessionLanguage" value="${sessionScope.language }"/>
			<div class="search-area">
				<div class="multiple-checkbox">
					<div class="selected"> <!-- onclick="getShipList()"> -->
						<span><spring:message code="graph.selectShipOwner" /></span>
						<input type="hidden" id="searchCompId" name="searchCompId" value="<%-- ${monitorVo.searchCompId != null ? monitorVo.searchCompId : '전체'} --%>"/>
					</div>
					<div class="selectbox" id="compListBox">
						<ul>
							<li>
								<span class="check-box1">
									<input type="checkbox" id="compListAllChk" name="compListAllChk" value="전체" checked/>
									<label for="compListAllChk"><spring:message code="select.all" /></label>
								</span>
							</li>
							<c:forEach var="comp" items="${compList }">
								<li>
									<span class="check-box1" >
										<input type="checkbox" id="chkCompId_${comp.comp_id }" name="chkCompId" value="${comp.comp_id}"checked>
										<label for="chkCompId_${comp.comp_id}">${comp.comp_name }</label>
									</span>
								</li>
							</c:forEach>						
						</ul>					
					</div>
				</div>	
				<div class="multiple-checkbox">
					<div class="selected"> <!-- onclick="getShipList()"> -->
						<span><spring:message code="graph.selectShip" /></span>
						<input type="hidden" id="searchShipCode" name="searchShipCode" value="${monitorVo.searchShipCode != null ? monitorVo.searchShipCode : '전체'}"/>
					</div>
					<div class="selectbox" id="shipListBox">
						<ul id="shipListoptions">	
							<li>
							</li>
						</ul>					
					</div>
				</div>
				<select name="searchOrder" id="searchOrder">
					<option value="DESC"><spring:message code="graph.descending" /></option>
					<option value="ASC"><spring:message code="graph.ascending" /></option>
				</select>
				<label class="ml15" for="searchCompCnt"><spring:message code="graph.monitorUpTo" /></label>&nbsp;
				<input type="text" id="searchCompCnt" class="w10 a-center" name="searchCompCnt" value=20 
					onkeydown="this.value=this.value.replace(/[^-0-9]/g,'');"> <spring:message code="graph.ship" />&nbsp;
				<button type="button" id="getShip" class="btn-md white"><span><spring:message code="graph.lookup" /></span></button>
			</div>
		</form>
		<div class="chart-area" id="chart1"></div>
		<div id=container1></div>
	</div>
	
	<div class="contents-area">
		<header>
			<i class="icon-stat"><span>icon</span></i>
			<h2><spring:message code="graph.dataUsageTrendByShip" /></h2>
		</header>
		<form name="searchMonitorForm2" id="searchMonitorForm2" method="post">		
		<input type="hidden" id="excelMsg" name="excelMsg" value="">
			<div class="search-area">
				<select id="searchUnit" name="searchUnit">
					<option value="hour" ${'hour' eq monitorVo.searchUnit ? 'selected' : ''}>
						<spring:message code="graph.hour" />
					</option>
					<option value="day" ${'day' eq monitorVo.searchUnit ? 'selected' : ''}>
						<spring:message code="graph.day" />
					</option>
					<option value="month" ${'month' eq monitorVo.searchUnit ? 'selected' : ''}>
						<spring:message code="graph.month" />
					</option>
				</select>
				<select id="searchRange" name="searchRange">
					<option value="first" ${'first' eq monitorVo.searchRange ? 'selected' : ''}>
						<spring:message code="graph.lately" />
					</option>
					<option value="term" ${'term' eq monitorVo.searchRange ? 'selected' : ''}>
						<spring:message code="graph.term" />
					</option>
				</select>
				<div id="time_list">
					<select id="searchTime" name="searchTime">					
						<c:forEach begin="1" end="31" var="setTime">
                    	<c:set var="setTime" value="${setTime}" />
                            <c:set var="setTime" value='${setTime}' />
                            <option value="${setTime}" ${setTime eq monitorVo.searchTime ? 'selected' : ''}>
                            	<c:out value="${setTime}" />
                            </option>
                    	</c:forEach>
					</select>&nbsp;<spring:message code="graph.day" />&nbsp;
				</div>
				<button type="button" id="getTimeChart" class="btn-md white" ><span><spring:message code="graph.lookup" /></span></button>
			</div>
		</form>
		<div class="chart-area" id="chart2">
			<div id="container2" style="height: 30%;"></div>
		</div>
	</div>
	
	<jsp:include page="../footer.jsp" flush="false"/>
</body>

<script>

	var timer = null;
	var timer1 = null;	
	
	function getLastShipChart() {
		// 선택된 체크박스 값 가져오기
	    let searchShipCode = $('input:checkbox[name="chkShipCode"]:checked').map(function() {
	        return this.value;
	    }).get();
		
	    let searchCompId = $('input:checkbox[name="chkCompId"]:checked').map(function() {
	        return this.value;
	    }).get();
	    	
	    // 데이터 객체 생성
	    let datas = {
	        'searchRange': $("#searchRange").val(),
	        'searchUnit': $("#searchUnit").val(),
	        'searchTime': $("#searchTime").val(),
	        'searchCompId': searchCompId.join(','),
	        'searchShipCode': searchShipCode.join(','),
	        'searchOrder': $("#searchOrder").val(),
	        'searchCompCnt': $("#searchCompCnt").val(),
	    };
	    $.ajax({
	        type: "POST",
	        url: "/monitor/getLastShipChart.ajax",
	        data: datas,
	        dataType: "json",
	        success: function(lastResult) {
	        	
	        	//console.log(lastResult);
	        	
	        	let categories = [];
	        	let lastChart = [];
	        	
	        	for(let i = 0; i < lastResult.length; i++){
	        		categories.push(lastResult[i].s_name);
	    			lastChart.push(parseInt(lastResult[i].val2));
	    		}
	        	
	        	getThisShipChart(datas, lastChart, categories);
	        }
	    });
	}
	
	function getThisShipChart(datas, lastChart, categories){
		$.ajax({
	        type: "POST",
	        url: "/monitor/getThisShipChart.ajax",
	        data: datas,
	        dataType: "json",
	        success: function(thisResult) {
	        	
	        	let thisChart = [];
	        	
	        	for(let i = 0; i < lastChart.length; i++){
	    			thisChart.push(parseInt(thisResult[i].val2));
	    		}
	        	
	        	getShipChart(lastChart, categories, thisChart);
	        }
	    });
	}
	
	function getShipChart(lastChart, categories, thisChart){
		
		let yAxisText = '<spring:message code="graph.usage" />';
		let lastMonth = '<spring:message code="graph.previousMonth" />';
		let thisMonth = '<spring:message code="graph.thisMonth" />';
	    		
		let chart1 = Highcharts.chart('container1', {
			chart: {
				type: 'column',
				backgroundColor: null
			},
		    title: {
		        text: null
		    },
		    subtitle: {
		        text: null
		    },
		    xAxis: {
		    	categories: categories,
		        title: {
		            text: null
		        }
		    },
		    yAxis: {
		        min: 0,
		        title: {
		            text: yAxisText
		        },
		        labels: {
		            overflow: 'justify'
		        }
		    },
		    tooltip: {
		        valueSuffix: null
		    },
		    plotOptions: {
		    	column: {
		            dataLabels: {
		                enabled: true,
		                color: (Highcharts.theme && Highcharts.theme.dataLabelsColor)
		            }
		        },
		        series: {
		            pointWidth: 30,
		            point: {
		            },
		            animation: false
		        }
		    },
		    credits: {
		        enabled: false
		    },
		    series: [{
	            name: lastMonth,
	            color: 'dimgray',
	            data: lastChart
	        }, {
	            name: thisMonth,
	            color: 'lightskyblue',
	            data: thisChart
	        }]
		});
		
		if(timer == null){
	     	timer = setTimeout("getLastShipChart()", 60 * 1000);	  
		}else{
			clearTimeout(timer);
			timer = setTimeout("getLastShipChart()", 60 * 1000);	 
		}
    }
	
	function getTime(){
		// 선택된 체크박스 값 가져오기
	    let searchShipCode = $('input:checkbox[name="chkShipCode"]:checked').map(function() {
	        return this.value;
	    }).get();
		
	    let searchCompId = $('input:checkbox[name="chkCompId"]:checked').map(function() {
	        return this.value;
	    }).get();
		
		let datas = {
	        'searchRange': $("#searchRange").val(),
	        'searchUnit': $("#searchUnit").val(),
	        'searchTime': $("#searchTime").val(),
	        'searchCompId': searchCompId.join(', '),
	        'searchShipCode': searchShipCode.join(', '),
	        'searchOrder': $("#searchOrder").val(),
	        'searchCompCnt': $("#searchCompCnt").val(),
	    };
		
		// searchRange가 term이면 datas에 startDate, endDate, startHour, endHour 추가
		if($('#searchRange').val() == "term"){
			
			if(parseInt($('#endDate').val()) < parseInt($('#startDate').val())){
				alert('<spring:message code="confirm.wrong.invalidDate" />');	
			 }
			
			let startHour = "";
			let endHour = "";
			
			if($('#searchUnit').val() == "hour"){
				startHour = $('#startHour').val(); 
				endHour = $('#endHour').val();
			} else {
				startHour = '00'; 
				endHour = '00';
			}
			 datas['startDate'] = $('#startDate').val();
			 datas['endDate'] = $('#endDate').val();
			 datas['startHour'] = startHour;
			 datas['endHour'] = endHour;		 
		}
		
		$.ajax({
	        type: "POST",
	        url: "/monitor/getTime.ajax",
	        data: datas,
	        dataType: "json",
	        success: function(response) {
	        	let monTime = [];
	        	
	        	for(let i = 0; i < response.length; i++){
	        		monTime.push(response[i].mon_time);
	        	}
	        	getTimeChartData(datas, monTime);
	        }
		});
	}
	
	function getTimeChartData(datas, monTime){
		$.ajax({
	        type: "POST",
	        url: "/monitor/getTimeChartData.ajax",
	        data: datas,
	        dataType: "json",
	        success: function(response) {
	        	
	        	let seriesData = [];

	            for (var key in response) {
                    seriesData.push({
                        name: key,
                        data: response[key].map(Number) // 데이터가 문자열이라면 숫자로 변환
	                });
	            }
	            //console.log(seriesData);
	            getTimeChart(monTime, seriesData); 
	        }
		});
	}
	
	function getTimeChart(monTime, seriesData){
		
		let yAxisText = '<spring:message code="graph.usage" />';
		
		// Highcharts 설정
	  	Highcharts.setOptions({
	        lang: {
	            thousandsSep: ','
	        }
	    });

	    let chart2 = Highcharts.chart('container2', {
	    	chart: {
		        backgroundColor:null
		        ,/* zoomType: monTime.length > 31 ? 'x' : undefined */
		    },
			title: {
		        text: null
		    },
		    subtitle: {
		        text: null
		    },
		    xAxis: {
		    	categories: monTime,
				/* crosshair: true */
			title: {
            		text: null
        		}		
		    },
		    yAxis: {
		        min: 0,
		        title: {
		            text: yAxisText
		        },
		        labels: {
		            overflow: 'justify'
		        }
		    },
		    tooltip: {
		        valueSuffix: null
		    },
		    plotOptions: {
		    	column: {
		            dataLabels: {
		                enabled: true,
		                color: (Highcharts.theme && Highcharts.theme.dataLabelsColor)
		            }
		        },
		        series: {
		            pointWidth: 30,
		            point: {
		                events: {
		                    click: function () {
		                        //alert(this.category + ', ' + this.y);
		                    }
		                }
		            },
		            animation: false
		        }
		    },
		    credits: {
		        enabled: false
		    },
	        
		    series: seriesData
	    });
	    
		if(timer1 == null){
	     	timer1 = setTimeout("getTime()",60*1000);	  
		}else{
			clearTimeout(timer1);
			timer1 = setTimeout("getTime()",60*1000);	 
		}
	}   

</script>

</html>

