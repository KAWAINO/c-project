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
<title>APMS</title>
<link rel="stylesheet" href="/web/css/bootstrap.css">
<link rel="stylesheet" href="/web/css/button.css">
<link rel="stylesheet" href="/web/css/signin.css">
<link rel="stylesheet" href="/web/css/signin.css">
<!-- all base : JS -->
<script type="text/javascript" src="/web/js/jquery-3.3.1.min.js"></script>
<script type="text/javascript" src="/web/js/jquery-ui-1.12.1/jquery-ui.js"></script>
<script charset="utf-8" src="/web/js/bootstrap.bundle.min.js"></script>

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
jQuery(document).ready(function(){
	// 로그인 버튼 클릭시
	$(document).on("click", "#btnLogin", function(e){
		e.preventDefault();
		
		fn_login();
	});
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
	
	var param = {user_id : $("#userId").val() , passwd : $("#userPasswd").val()};
	$.ajax({
        type    : 'post',
        async   : false,
        url     : '/login/loginProc.do',
        data    : param,
        success : function(data){
        	var code = data.code;
        	
        	if("SUCCESS" == code){
        		location.href='<c:url value="/suser/suser.do"/>'
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
	      <input type="text" class="form-control" id="userId" placeholder="name@example.com">
	      <label for="userId">User ID</label>
	    </div>
	    <div class="form-floating mt-1">
	      <input type="password" class="form-control" id="userPasswd" placeholder="Password">
	      <label for="userPasswd">Password</label>
	    </div>
	    <button class="w-100 btn btn-lg btn-primary mt-1" id="btnLogin">Login</button>
	  </form:form>
  </div>
</main>
</body>
</html>