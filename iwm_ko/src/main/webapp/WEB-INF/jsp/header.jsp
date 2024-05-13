<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="x"      uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ taglib prefix="fmt"    uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:set var="suser_name" value="${sessionScope.user_name}" />

<link rel="stylesheet" href="../web/css/default.css" type="text/css" media="all">
<link rel="stylesheet" href="../web/js/jquery-ui-1.12.1/jquery-ui.css" type="text/css" media="all"> 


<script type="text/javascript">

	//window.onLoad = getAlarm(); 

	$(document).ready(function(){
		
		const isChecked = localStorage.getItem('sound');
			
		$('#sound').prop('checked', false);
		if (isChecked == 'false') {
	    	$('#sound').prop('checked', false);
	    } else {
	    	$('#sound').prop('checked', true);
	    }

		$('#sound').on('click', function() {
		    let isChecked = $(this).is(':checked');
		    localStorage.setItem('sound', isChecked);
		    
		    if (isChecked) {	  
		        getSevSound(isChecked); 
		    } /* else {
		        stopAllSounds();
		    } */
		});

		/* 언어 변경 설정 */
		// localStorage의 언어명과 클래스 변수에 저장
		let textStorage = localStorage.getItem('selectedLanguage');
		let flagStorage = localStorage.getItem('langFlag');				
		// localStorage가 비어있는 경우 디폴트값 설정
	    if (!textStorage || !flagStorage) {
	        textStorage = "한국어"; // 기본 언어 설정
	        flagStorage = "korean"; // 기본 언어 플래그 설정
	        /* self.location.replace("/monitor/monitor.do?lang=ko"); */
	        localStorage.setItem('selectedLanguage', textStorage);
	        localStorage.setItem('langFlag', flagStorage);
	    } 
	    // localStorage의 언어명과 클래스 적용
		document.querySelector("#selectedLanguage").innerHTML  = textStorage;	
		document.querySelector("#selectedLanguage").classList.add(flagStorage);

		// 언어 변경 이벤트
		$("#language a").click(function() {
        	
	        //event.preventDefault(); 
	        // 링크 클릭 시 페이지 이동을 막음

	        let selectedLanguage = document.querySelector("#selectedLanguage");
	        let langCode = this.getAttribute("data-lang");
	        $('#langTemp').val(langCode);
	        let langText = '';
	        let langFlag = '';

	        if (langCode === "ko") {
	        	langText = "한국어";
	        	langFlag = "korean";
	            selectedLanguage.textContent = langText;
	            selectedLanguage.classList.add(langFlag);
	        } else {
	        	langText = "English";
	        	langFlag = "eng";
	            selectedLanguage.textContent = langText;
	            selectedLanguage.classList.add(langFlag);
	        } 
	        localStorage.setItem('selectedLanguage', selectedLanguage.innerHTML);
	        localStorage.setItem('langFlag', langFlag); 
	    });
		
		// 로그아웃 시 localStorage 비움
		$('#logout').click(function(){
			localStorage.removeItem('selectedLanguage');
			localStorage.removeItem('langFlag');
		}); 
		/* 언어 변경 설정 끝*/	
	});	
	
	var getAlarmTimer = null;
	var chk_comp = null;
	
	function getAlarm(){ 		
		$.ajax({
		    type : "POST", 
		    url : "/alarmmon/getAlarm.ajax",
		    dataType : "json",
		    cache : false,
		    success : function(result){
		    	
		    	for(let i = 0; i < result.length; i++){    		
		    		if(result[i].severity == "CRITICAL"){
			    		$('#alarm_critical').html(result[i].s_cnt+'건');
			    	} else if(result[i].severity == "MAJOR"){
			    		$('#alarm_major').html(result[i].s_cnt+'건');	
			    	} else{
			    		$('#alarm_minor').html(result[i].s_cnt+'건');	
			    	}
		    	}		    	
		    	getSevSound();
		    }
		});		
		if(getAlarmTimer = null) {
			getAlarmTimer = setTimeout("getAlarm()",5000);	  
		} else {
			clearTimeout(getAlarmTimer);
			getAlarmTimer = setTimeout("getAlarm()",5000);	 
		}
	}
	
	function getSevSound(){ 	
		$.ajax({
		    type : "POST", 
		    url : "/alarmmon/getSevSound.ajax",
		    dataType : "json",
		    cache : false,
		    success : function(result){
		    
		    	let highestSeverity = '';

	            for(let i = 0; i < result.length; i++){
	                const severity = result[i].severity;
	                if(severity == 'CRITICAL'){
	                    highestSeverity = 'CRITICAL';
	                } else if(severity == 'MAJOR' && highestSeverity != 'CRITICAL'){
	                    highestSeverity = 'MAJOR';
	                } else if(severity == 'MINOR' && highestSeverity != 'CRITICAL' && highestSeverity != 'MAJOR'){
	                    highestSeverity = 'MINOR';
	                }
	            }
	            
	            if('${sessionScope.comp_id == "0"}'){
	           		playSound(highestSeverity);
	            }
	            //console.log('highestSeverity : ' + highestSeverity);
		    }
		});
	}
	
	function setSound(check){	
		if(true == check){
			getSevSound();
		}
	}
	
	function playSound(soundType) {
		//console.log('soundType : ' + soundType);
	    // 오디오 요소 선택
	    var sound;
	    switch(soundType) {
	        case 'CRITICAL':
	            sound = document.getElementById('criticalSound');
	            break;
	        case 'MAJOR':
	            sound = document.getElementById('majorSound');
	            break;
	        case 'MINOR':
	            sound = document.getElementById('minorSound');
	            break;
	        default:
	        	sound.pause();
            	sound.currentTime = 0;
	            return;
	    }

	    // 사용자 상호작용 이후에 사운드 재생 시도
	    if (sound && localStorage.getItem('sound') !== 'false') {
	        sound.play().catch(e => console.error('Error playing sound:', e));
	    }
	} 

</script>

<!-- 오디오 요소 -->							       
<audio id="criticalSound" src="../resources/sound/critical.wav" preload="auto"></audio>
<audio id="majorSound" src="../resources/sound/major.wav" preload="auto"></audio>
<audio id="minorSound" src="../resources/sound/minor.wav" preload="auto"></audio>

<div id="wrap" >

	<!-- header -->
	<c:choose>
    	<c:when test="${sessionScope.comp_id eq null}">
			<header class="header">
				<h1>
					<img src="../web/images/common/logo-black-ktsat.png" alt="KTsat">
					<span><img src="../web/images/common/site-title.png" alt=""></span>
				</h1>
				<div class="right-area">
					<div class="language">
						<div>
					        <i id="selectedLanguage"></i>
					        <ul id="language" >
					            <li>
					                <a href="../login/login.do?lang=ko" data-lang="ko">
					                    <i class="korean">한국어</i>
					                </a>
					            </li>
					            <li>
					                <a href="../login/login.do?lang=en" data-lang="en">
					                    <i class="eng">English</i>
					                </a>
					            </li>
					        </ul>
					    </div>
					</div>
				</div>
			</header>
		</c:when>
		<c:otherwise>
			<header class="header" >
				<input type="hidden" id="langTemp" value="">
				<div class="top-area">
					<h1><a href="../monitor/monitor.do"><img src="../web/images/common/logo.png" alt="KTsat MVSAT APMS"></a></h1>
					<div class="right-area">
						<c:choose>
		        			<c:when test="${sessionScope.comp_id eq 0}">	
								<div class="alarm-area" id="top_alarm">
									<ul class="monitoring-alarm">
										<li>
											<div class="status critical">Critical</div>
											<a href="../alarmmon/alarmmon.do" id="alarm_critical">&nbsp;0<spring:message code="header.severity" /></a>
										</li>
										<li>
											<div class="status major">Major</div>
											<a href="../alarmmon/alarmmon.do" id="alarm_major">&nbsp;0<spring:message code="header.severity" /></a>
										</li>
										<li>
											<div class="status minor">Minor</div>
											<a href="../alarmmon/alarmmon.do" id="alarm_minor">&nbsp;0<spring:message code="header.severity" /></a>
										</li>
									</ul>
									<input type="hidden" value="" id="SEVERITY">
								</div>
								
								<div class="wifi-area">
							    <div>
							   
							        <!-- 가청 경보음 제어 체크박스 -->
							        <span class="checkbox-onoff">
							            <input type="checkbox" id="sound" name="sound" onclick="setSound(this.checked);" >
							            <label for="sound"><spring:message code="header.alarmSound" /></label>
							        </span>
	
							    </div>
							</div>
						</c:when>
					</c:choose>
						
						<div class="language">
						    <div>
						        <i id="selectedLanguage"></i>
						        <ul id="language" >
						            <li>
						                <a href="../monitor/monitor.do?lang=ko" data-lang="ko">
						                    <i class="korean">한국어</i>
						                </a>
						            </li>
						            <li>
						                <a href="../monitor/monitor.do?lang=en" data-lang="en">
						                    <i class="eng">English</i>
						                </a>
						            </li>
						        </ul>
						    </div>
						</div> 
						
						<div class="user-area">
							<c:set var="userName" value="${suser_name}" />
		                    <div class="user-name"><strong>${userName}</strong><spring:message code="header.loginUser" /></div>
			                <a href="/login/logout.do" >
							    <button name="logout" id="logout" class="btn-logout">로그아웃</button>
							</a>
						</div>
					</div>
				</div>
				
				<%@ include file = "/WEB-INF/jsp/menu/menu.jsp" %>
				<!-- <nav class="gnb" id="nav">
				
					<ul id="monitoring"></ul>
					<ul id="menu"></ul>			
				</nav>	 -->		
			</header>
		</c:otherwise>
	</c:choose> 
	<!-- // header -->
	<!-- container -->
	<section class="container">
	<!-- contents -->
