<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="x"      uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ taglib prefix="fmt"    uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>RADIUS</title>
<link rel="stylesheet" href="/web/css/bootstrap.css">
<link rel="stylesheet" href="/web/css/button.css">
<link rel="stylesheet" href="/web/css/signin.css">
<link rel="stylesheet" href="/web/css/si:gnin.css">
<!-- <link rel="stylesheet" href="/web/css/bootstrap-multiselect.css"> -->
<!-- all base : JS -->
<script type="text/javascript" src="/web/js/jquery-3.3.1.min.js"></script>
<script type="text/javascript" src="/web/js/jquery-ui-1.12.1/jquery-ui.js"></script>
<script charset="utf-8" src="/web/js/bootstrap.bundle.min.js"></script>
<!-- <script type="text/javascript" src="/web/js/bootstrap-multiselect.js"></script> -->

    <style>
      .bd-placeholder-img {
        font-size: 1.125rem;
        text-anchor: middle;
        -webkit-user-select: none;
        -moz-user-select: none;
        user-select: none;
      }

      @media (min-width: 768px) {
        .bd-placeholder-img-lg {
          font-size: 3.5rem;
        }
      }
    </style>
    
<script type="text/javascript">




// 로그아웃 후 뒤로가기 방지
history.pushState(null, null, location.href);
window.onpopstate = function(event){
	history.go(1);
};

jQuery(document).ready(function(){
		
	//로그인 버튼 클릭시
	$(document).on("click", "#btnLogin", function(e){
		e.preventDefault();
			
		fn_login();
	});
	
	  
	
	/* // 저장된 쿠키값을 가져와서 ID 칸에 넣어준다. 없으면 공백으로 들어감.
	var key = getCookie("key");
	$("#userId").val(key);
	
	// 그 전에 ID를 저장해서 처음 페이지 로딩 시, 입력 칸에 저장된 ID가 표시된 상태라면,
	if($("#userId").val() != "") {
		$("#rm").prop("checked", true);
	}
	
	$("#rm").change(function(){ 					// 체크박스 변화 함수	
		if($("#rm").is(":checked")) {				// rm 체크되어있다면
			setCookie("key", $("#userId").val(), 7);	// 7일동안 쿠키 보관
		} else {									
			deleteCookie("key");
		}
	});
	
	// ID 저장하기를 체크한 상태에서 ID를 입력하는 경우, 이럴 때도 쿠키 저장.
    $("#userId").keyup(function(){ // ID 입력 칸에 ID를 입력할 때,
        if($("#rm").is(":checked")){ // ID 저장하기를 체크한 상태라면,
            setCookie("key", $("#userId").val(), 7); // 7일 동안 쿠키 보관
        }
    });
	
	// 쿠키 저장하기 
	// setCookie => saveid함수에서 넘겨준 시간이 현재시간과 비교해서 쿠키를 생성하고 지워주는 역할
	function setCookie(cookieName, value, exdays) {
		var exdate = new Date();
		exdate.setDate(exdate.getDate() + exdays);
		var cookieValue = escape(value)
				+ ((exdays == null) ? "" : "; expires=" + exdate.toGMTString());
		document.cookie = cookieName + "=" + cookieValue;
	}
	
	// 쿠키 삭제
	function deleteCookie(cookieName) {
		var expireDate = new Date();
		expireDate.setDate(expireDate.getDate() - 1);
		document.cookie = cookieName + "= " + "; expires="
				+ expireDate.toGMTString();
	}
	
	// 쿠키 가져오기
	function getCookie(cookieName) {
		cookieName = cookieName + '=';
		var cookieData = document.cookie;
		var start = cookieData.indexOf(cookieName);
		var cookieValue = '';
		if (start != -1) { // 쿠키가 존재하면
			start += cookieName.length;
			var end = cookieData.indexOf(';', start);
			if (end == -1) // 쿠키 값의 마지막 위치 인덱스 번호 설정 
				end = cookieData.length;
			cookieValue = cookieData.substring(start, end);
		}
		return unescape(cookieValue);
	} */
	
});

function fn_login(){
	if( jQuery.trim($("#userId").val())=="" ){
		alert("아이디를 입력해주세요.");
		$("#userId").focus();
		return;
	}
	if( jQuery.trim($("#userPasswd").val())=="" ){
		alert("비밀번호를 입력해주세요.");
		$("#userPasswd").focus();
		return;
	}
	
	var param = {userId : $("#userId").val() , passWd : $("#userPasswd").val()};
	$.ajax({
        type    : 'post',
        async   : false,
        url     : '/loginProc.do',
        data    : param,
        success : function(data){
        	var code = data.code;
        	
        	if("SUCCESS" == code){
        		location.href='<c:url value="/chart/chart.do"/>'
        	}else{
        		alert("아이디 또는 비밀번호가 일치하지 않습니다.");
        	}
        }
	});
}

</script>
</head>

<body class="text-center">
<main class="form-signin">
	<div class="login-title">
    	<img src="/web/images/icon/ktsat-logo.png" height="35px" style="float:left;">
    	<br>FB BGAN RADIUS
  	</div>
	<div class="form-area">
  		<form:form name="loginForm" method="POST">	
  			<img src="/web/images/icon/key.svg" height="50px;" class="mb-1">
    		<div class="form-floating">
      			<input type="text"  class="form-control" id="userId" placeholder="name@example.com" >
      			<label for="userId">User ID</label>
    		</div>
    		<div class="form-floating mt-1">
      			<input type="password" class="form-control" id="userPasswd" placeholder="Password">
      			<label for="userPasswd">Password</label>
    		</div>
    		<!-- <div class="mt-2 mb-3">
    			<label for="rm">
    			<input type="checkbox" id="rm" name="rm" value="rm"> Remember me
    			</label>
    		</div> -->
    		<button class="w-100 btn btn-lg btn-primary mt-1" id="btnLogin">Login</button>
    		<!-- <p class="mt-5 mb-3 text-muted w-100"> &copy; 2017-2021 </p> -->
  		</form:form>
  	</div>
</main>
</body>
</html>