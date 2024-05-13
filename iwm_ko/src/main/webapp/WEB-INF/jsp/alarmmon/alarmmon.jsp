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

<script type="text/javascript" >

	$(document).ready(function(){	

		//getAlarmSys();
		//getAlarmHis();
		//getAlarmActive();  	

	});
	
	var timer = null;
	var hisTimer = null;
	var activeTimer = null;
	
	// 알람현황 페이지로딩 ajax
	function getAlarmSys(){
		$.ajax({
			type : "POST",
			url : "/alarmmon/alarmmon_sys.do",						
			dataType : "html",
			error	: function (e) { alert('<spring:message code="confirm.error" />');},		
			success : function(result) {	
				$("#alarmSys").html(result);
			}
		});
		
		if(timer = null){
	     	timer = setTimeout("getAlarmSys()",5000);	  
		}else{
			clearTimeout(timer);
			timer = setTimeout("getAlarmSys()",5000);	 
		} 
	};
	
	// 알람이력 페이지로딩 ajax
	function getAlarmHis(){
		$.ajax({
			type : "POST",
			url : "/alarmmon/alarmmon_his.do",						
			dataType : "html",
			error	: function (e) {'<spring:message code="confirm.error" />'},		
			success : function(result) {	
				$("#alarmHis").html(result);
			}
		});
		
		if(hisTimer = null){
	     	hisTimer = setTimeout("getAlarmHis()",5000);	  
		}else{
			clearTimeout(hisTimer);
			hisTimer = setTimeout("getAlarmHis()",5000);	 
		} 
	};
	
	// active 로딩 ajax
	function getAlarmActive(){
		$.ajax({
			type : "POST",
			url : "/alarmmon/alarmmon_active.do",						
			dataType : "html",
			error	: function (e) {'<spring:message code="confirm.error" />'},		
			success : function(result) {	
				$("#active").html(result);
			}
		});
		
		if(activeTimer = null){
	     	activeTimer = setTimeout("getAlarmActive()",10000);	  
		}else{
			clearTimeout(activeTimer);
			activeTimer = setTimeout("getAlarmActive()",10000);	 
		} 
	};
	
	function setSysTimer(type){
		if("start" == type){
			$("#startAlarmSys").addClass("on");
			$("#stopAlarmSys").removeClass("on");
			if(timer != null){
				clearTimeout(timer);
				timer = setTimeout("getAlarmSys()", 5000);
			} else {
				timer = setTimeout("getAlarmSys()", 5000); // 함수 참조를 직접 사용
			}
		}else if("stop" == type){
			$("#stopAlarmSys").addClass("on");
			$("#startAlarmSys").removeClass("on");
			clearTimeout(timer);
			timer = null; // 타이머 정지 후 null 할당으로 초기화
		}
	}

	function setHisTimer(type){
		if("start" == type){
			$("#startAlarmHis").addClass("on");
			$("#stopAlarmHis").removeClass("on");
			if(hisTimer != null){
				clearTimeout(hisTimer);
				hisTimer = setTimeout("getAlarmHis()", 5000);
			} else {
				hisTimer = setTimeout("getAlarmHis()", 5000); // 함수 참조를 직접 사용
			}
		}else if("stop" == type){
			$("#stopAlarmHis").addClass("on");
			$("#startAlarmHis").removeClass("on");
			clearTimeout(hisTimer);
			hisTimer = null; // 타이머 정지 후 null 할당으로 초기화
		}
	}
	
	// 가청 삭제
	function delSound(seq){
		if(!confirm('<spring:message code="confirm.delete.sound" />')){
			return false;
		}
		$.ajax({
			type : "POST",
			url : "/alarmmon/delSound.ajax",
			data : { "seq" : seq },
			dataType : "json",
			error	: function (e) { alert('<spring:message code="confirm.error" />');},		
			success : function(data) {	
				if(data.result == "1") {
					alert('<spring:message code="confirm.successfully" />');
					document.location.reload();
				}else {
					alert('<spring:message code="confirm.error" />');
				}
			}
		});
	};
	
	// 알람 해제
	function delAlarm(seq){
		if(!confirm('<spring:message code="confirm.delete.alarm" />')){
			return false;
		}
		$.ajax({
			type : "POST",
			url : "/alarmmon/delAlarm.ajax",
			data : { "seq" : seq },
			dataType : "json",
			error	: function (e) { alert('<spring:message code="confirm.error" />');},		
			success : function(data) {	
				if(data.result == "1") {
					alert('<spring:message code="confirm.successfully" />');
					document.location.reload();
				}else {
					alert('<spring:message code="confirm.error" />');
				}
			}
		});
	};
	
</script>
</head>
<body>

	<jsp:include page="../header.jsp"/>
	
	<div id="active"></div>
	<div class="tab-wrap">	
		<div class="rt-area">	
				<ul class="tab-btn1">
					<li><a href="#alarmSys" onclick="getAlarmSys(); return false;"><spring:message code="alarmmon.status" /></a></li>
					<li><a href="#alarmHis" onclick="getAlarmHis(); return false;"><spring:message code="alarmmon.history" /></a></li>
				</ul>
			</div>
		
		<div id="alarmSys"></div>
		<div id="alarmHis"></div>
	</div>

	<jsp:include page="../footer.jsp" flush="false"/>
		
</body>
</html>