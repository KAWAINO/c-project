<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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
	
	// 등급별 네비게이션바
	getMenuList();
});

	function getMenuList(){
		
		let grade = '${sessionScope.user_grade}';
		let lang = localStorage.getItem('langFlag');
		
		let originalMenuList  = [
	        <c:forEach var="menu" items="${menuList}" varStatus="status">
	            {
	                gui_name: "${menu.gui_name}",
	                en_gui_name: "${menu.en_gui_name}",
	                web_name: "${menu.web_name}"
	                
	            }${status.last ? '' : ','}
	        </c:forEach>
	    ]; 
		//console.log(menuList);
		
		// 필터링 함수로 "여객선" 또는 "Ferry"를 포함하는 항목을 제외
	    let menuList = originalMenuList.filter(function(menu) {
	        return !(menu.gui_name.includes("여객선") || menu.en_gui_name.includes("Ferry"));
	    });	
		//console.log(menuList);
	
		let currentURL = window.location.pathname;
		let urlArr = currentURL.split("/");
		let groupCode = urlArr[1];
		let urlTemp = urlArr[2];
		
		let menuArr = urlTemp.split(".");
		let menuCode = menuArr[0];
		let groupName = "";
		
		for(let i = 0; i < menuList.length; i++){
			let gui_name = menuList[i].gui_name;
			let web_name = menuList[i].web_name;
			
			let guiArr = gui_name.split("|");
			let webArr = web_name.split("|");
			
			let guiCode = guiArr[0];
			let guiGroup = guiArr[1];
			let guiName = guiArr[2];
			let webGroup = webArr[0];
			let webName = webArr[1];
			
			//console.log('webName : ' + webName);
			//console.log('menuCode : ' + menuCode);
			//console.log('guiCode : ' + guiCode);
	    	//console.log('groupName : ' + groupName);
			if(webName === menuCode){
				if(guiCode === '1'){
					groupName = "shipcomp";	        				
				} else if(guiCode ==='2') {
					groupName = "crewcdr";
				} else if(guiCode ==='3') {
					groupName = "stat";
				} else if(guiCode ==='4') {
					groupName = "setting";
				} else if(guiCode ==='5') {
					groupName = "traffic";
				} else if(guiCode ==='6') {
					groupName = "alarmhis";
				} else if(guiCode ==='7') {
					groupName = "cuser";
				}       			       			      			      			      			      			
			}       		
		}
	
		let prevGroup = "";
		let menu = "";
		for(let i = 0; i < menuList.length; i++) {
			let guiString = "";
			if(lang === 'korean' || lang === null){
				guiString = menuList[i].gui_name;
			} else {
				guiString = menuList[i].en_gui_name;
			}
			
			let webArr = menuList[i].web_name.split("|");
			let guiArr = guiString.split("|");
			
			let guiCode = guiArr[0];
			let guiGroup = guiArr[1];
			let guiName = guiArr[2];
			
			let webGroup = webArr[0];
			let webName = webArr[1];
			
			let classOn = "";
			if(webName === menuCode){
				classOn = guiCode;
			}
			
			let className = ""; 
			if(guiCode === '1') {
				className = "shipcomp";
			} else if(guiCode === '2') {
				className = "crewcdr";
			} else if(guiCode === '3') {
				className = "stat";
			} else if(guiCode === '4') {
				className = "setting";
			} else if(guiCode === '5') {
				className = "traffic";
			} else if(guiCode === '6') {
				className = "alarmhis";
			} else if(guiCode === '7') {
				className = "cuser";
			}	
			
			// monitoring 네브바 담을 변수 선언
			let monitoring = "";
			
	    	monitoring += '<li class="monitoring ' + (menuCode === "monitor" ? "on" : menuCode === "alarmmon" ? "on" : "") + '">';
	    	monitoring += '<h2><spring:message code="header.nav.monitoring" /></h2>';
	    	monitoring += '<ul>';
	    	
	    	if(lang === "korean" || lang === null){
	    		monitoring += '<li ' + (menuCode === "monitor" ? "class='on'" : "") + '>';
	    		monitoring += '<a href="../monitor/monitor.do">종합 감시</a></li>';
	    		if('${sessionScope.comp_id}' === "0"){
	    			monitoring += '<li ' + (menuCode === "alarmmon" ? "class='on'" : "") + '>';
	        		monitoring += '<a href="../alarmmon/alarmmon.do">알람 모니터링</a></li>';
	    		}
	    	} else {
	    		monitoring += '<li ' + (menuCode === "monitor" ? "class='on'" : "") + '>';
	    		monitoring += '<a href="../monitor/monitor.do">Comprehensive monitoring</a></li>';
	    		if('${sessionScope.comp_id}' === "0"){
	    			monitoring += '<li ' + (menuCode === "alarmmon" ? "class='on'" : "") + '>';
	        		monitoring += '<a href="../alarmmon/alarmmon.do">Alarm monitoring</a></li>';
	    		}
	    	}
	    	monitoring += '</ul></li>';
	    	$('#monitoring').html(monitoring);
	
			// 새 그룹이 시작될 때
		    if (guiGroup !== prevGroup) {
		        // 첫 번째 그룹이 아니라면 이전 그룹을 닫음.
		        if (prevGroup !== "") {
		            menu += '</ul></li>';
		        }
		        menu += '<li id="' + className + '" class="' + (className === groupName ? className + " on" : className) + '">';
		        menu += '<h2>' + guiGroup + '</h2><ul>';
		    }
			// 현재 항목 추가
		    menu += '<li ' + (webName === menuCode ? "class='on'" : "") + '>';
		    menu += '<a href="../' + webGroup + '/' + webName + '.do">' + guiName + '</a></li>';
	
		    prevGroup = guiGroup;
		}
		$('#monitoring').append(menu);
		
	}
	
</script>

</head>

<body>

	<nav class="gnb" id="nav">
				
		<ul id="monitoring"></ul>
		<ul id="menu"></ul>		
			
	</nav>	

</body>
</html>