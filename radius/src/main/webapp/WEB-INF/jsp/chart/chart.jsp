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

<style type="text/css">

@keyframes chartjs-render-animation {
	from { opacity: 0.99; }
	to { opacity: 1; }
}

.chartjs-render-monitor {
	animation: chartjs-render-animation 0.001s;
}

.chartjs-size-monitor,
.chartjs-size-monitor-expand,
.chartjs-size-monitor-shrink {
	position: absolute;
	direction: ltr;
	left: 0;
	top: 0;
	right: 0;
	bottom: 0;
	overflow: hidden;
	pointer-events: none;
	visibility: hidden;
	z-index: -1;
}

.chartjs-size-monitor-expand > div {
	position: absolute;
	width: 1000000px;
	height: 1000000px;
	left: 0;
	top: 0;
}

.chartjs-size-monitor-shrink > div {
	position: absolute;
	width: 200%;
	height: 200%;
	left: 0;
	top: 0;
}
</style>

</head>
<body>

<!-- header -->
<%@ include file="/decorators/header.jsp"%>
<!-- //header -->

<div class="container min-vh-100">
	<ol class="breadcrumb mt-4 py-3 fs-5">
	  <li class="breadcrumb-item active">Home</li>
	  <li class="breadcrumb-item active">인증 통계</li>
	</ol>

	<div class="row">
		<div class="col-6">
			<div class="card border-primary mb-4">
				<div class="card-header text-white bg-primary">인증시도 추이</div>
			  	<div class="card-body">
			  		<div class="chartjs-size-monitor">
			  			<div class="chartjs-size-monitor-expand">
			  				<div class=""></div>
			  			</div>
			  			<div class="chartjs-size-monitor-shrink">
			  				<div class=""></div>
			  			</div>
			  		</div>
					<canvas class="my-4 w-100 chartjs-render-monitor" id="chart1" width="602" height="301" style="display: block; width: 602px; height: 301px;">	
					</canvas>	
				</div>
			</div>
		</div>
		<div class="col-6">
			<div class="card border-primary mb-4">
				<div class="card-header text-white bg-primary">금일 누적량</div>
			  	<div class="card-body">
			  		<div class="chartjs-size-monitor">
			  			<div class="chartjs-size-monitor-expand">
			  				<div class=""></div>
			  			</div>
			  			<div class="chartjs-size-monitor-shrink">
			  				<div class=""></div>
			  			</div>
			  		</div>
					<canvas class="my-4 w-100 chartjs-render-monitor" id="chart2" width="602" height="301" style="display: block; width: 602px; height: 301px;">
					</canvas>
				</div>
			</div>
		</div>
	</div>
	<div class="row">
		<div class="col-12">
			<div class="card border-primary mb-4">
				<div class="card-header text-white bg-primary">금일 시간대별 통계</div>
				<div class="card-body">
					<table class="table table-hover table-bordered text-center mt-2">
					  <thead>
					    <tr  class="table-primary scroll-x">
					      <th scope="col">시간</th>
					      <th scope="col">인증시도수</th>
					      <th scope="col">성공수</th>
					      <th scope="col">실패수</th>
					    </tr>
					  </thead>
					  <tbody id="newTbody">

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


	
<script type="text/javascript">

getAccessCnt();
getAccCnt();
getAccTable();

	var timer1 = null;
	var timer2 = null;
	var timer3 = null;
	
	function getAccessCnt(){
		
		var hour = [];
		var authCnt = [];
		var successCnt = [];
		var failCnt = [];
		
		$.ajax({
			url : "/chart/accessCnt.ajax",
			type : "POST",
			dataType : "JSON",
			data : { "data" : "accessCnt" },
			success : function(data) {
				
				// 차트
				for(var i = 0; i < data.length; i++) {		

					hour.push(data[i].hour + '시');
					authCnt.push(data[i].auth_cnt);
					successCnt.push(data[i].success_cnt);
					failCnt.push(data[i].fail_cnt);	
				}			

				var ctx = document.getElementById('chart1');

				var myChart = new Chart(ctx, {
					type: 'line',
					data: {
					  		labels: hour,
					    	datasets: [{
									      data: authCnt,
									      label: '인증시도수   ',
									      lineTension: 0.1,
									      backgroundColor: 'transparent',
									      borderColor: '#C8D7FF',
									      borderWidth: 2,									      
									      pointRadius : 0
									   },
									   {
										  data: successCnt,
										  label: '성공수   ',	
										  lineTension: 0.1,
										  backgroundColor: 'transparent',
										  borderColor: '#0A6ECD',
										  borderWidth: 2,
										  pointRadius : 0
									   },
									   {
										  data: failCnt,
										  label: '실패수   ',
										  lineTension: 0.1,
										  backgroundColor: 'transparent',
										  borderColor: '#CD2E57',
										  borderWidth: 2,
										  pointRadius : 0
									   }
					    			  ]
					  	  },
					  	   options: {
					    			 scales: {
					      				      yAxes: [{
					        						   ticks: {
					        							   	   min: 0,
					          								   beginAtZero: false
					       									  }
					      						     }]
					    					 },
				       						  animation: { 
				            							  duration: 0,
				          								 },
					  				}
				})
			} 
		});
		if(timer1 = null){
	     	timer1 = setTimeout("getAccessCnt()",18*100000);	  
		}else{
			clearTimeout(timer1);
			timer1 = setTimeout("getAccessCnt()",18*100000);	 
		}
	} 
	
	

	function getAccCnt(){

		var time = [];
		var authCnt = [];
		var successCnt = [];
		var failCnt = [];
			
		$.ajax({
			url : "/chart/accCnt.ajax",
			type : "POST",
			dataType : "JSON",
			data : { "data" : "accCnt" },
			success : function(data) {
				
				for(var i = 0; i < data.length; i++){
					
					time.push(data[0].stat_time);
					authCnt.push(data[0].auth_cnt);
					successCnt.push(data[0].success_cnt);
					failCnt.push(data[0].fail_cnt);
				}

				var ctx = document.getElementById('chart2');
	
				var myChart = new Chart(ctx, {
					
					type: 'bar',
					data: {
						   labels: time,
						   datasets: [{
									   data: authCnt,
									   label: '인증시도수   ',
									   backgroundColor: '#DCEBFF',
									   borderColor: '#C8D7FF',
									   borderWidth: 1 //경계선 굵기
									 },
									 {
									   data: successCnt,
									   label: '성공수   ',
									   backgroundColor: '#288CD2',
									   borderColor: '#288CD2',
									   borderWidth: 1 //경계선 굵기
									 },
									 {
									   data: failCnt,
									   label: '실패수   ',
									   backgroundColor: '#D25A5A',
									   borderColor: '#CD0000',
									   borderWidth: 1 //경계선 굵기
									}]
						  },
						   options: {
					 				 scales: {
					   						  yAxes: [{
					     							   ticks: { 
														       min: 0,
														       beginAtZero: false
					     									  }
					   								 }]
					 						 },
										      animation: { 
										         		  duration: 0,
				     									 },
								    } 
				})
			}
		});
		if(timer2 = null){
	     	timer2 = setTimeout("getAccCnt()",18*100000);	  
		}else{
			clearTimeout(timer2);
			timer2 = setTimeout("getAccCnt()",18*100000);	 
		}
	} 
	
	function getAccTable(){
		
		$.ajax({
			url : "/chart/accTable.ajax",
			type : "POST",
			dataType : "JSON",
			data : { "data" : "accTable" },
			success : function(data) {

				var noData = "";
				
				if(data.length == 0){
					
					noData += "<tr>";
					noData += "<td colspan='4'> 데이터가 없습니다. </td>";
					
					$("#newTbody").html(noData);
				} else {
					
					var result = "";
					
					for(var i = 0; i < data.length; i++){
						result += "<tr>";
						result += "<td>" + data[i].stat_time + "</td>";
						result += "<td>" + data[i].auth_cnt + '건' + "</td>";
						result += "<td>" + data[i].success_cnt + '건' +  "</td>";
						result += "<td>" + data[i].fail_cnt + '건' +  "</td>";
						result += "</tr>";
					}
					$("#newTbody").html(result);
				}
			}		
		});
		if(timer3 = null){
	     	timer3 = setTimeout("getAccTable()",18*100000);	  
		}else{
			clearTimeout(timer3);
			timer3 = setTimeout("getAccTable()",18*100000);	 
		}
	}

	
</script>

<!-- footer -->
<%@ include file="/decorators/footer.jsp"%>
<!-- //footer -->	

</body>


</html>