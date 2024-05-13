<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>


<!DOCTYPE HTML>
<html>
<head>

<!-- meta tag -->
<%@ include file="/decorators/meta.jsp" %>
<title>MVSAT APMS</title>


<!-- Google reCAPTCHA js -->
<!-- <script src="https://www.google.com/recaptcha/api.js?onload=onloadCallback&render=explicit" async defer></script> -->
<script src="https://www.google.com/recaptcha/api.js" async defer></script>
<!-- <script type="text/javascript">
  var recaptchaSiteKey = '${recaptchaSiteKey}';
</script> -->

<script type="text/javascript" src="/web/js/login/login.js"></script>

</head>
<body class="login" onLoad="document.getElementById('user_id').focus();">
<div id="wrap">
	<!-- header -->
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
	<!-- // header -->

	<!-- container -->
	<div class="container">

		<!-- contents -->
		<div class="contents-area">
			<h1><span>Log</span> In</h1>

			<div class="rt-area">
				<div class="msg-box">
					<span class="desc">* <spring:message code="login.login" /></span>
				</div>
			</div>
                <div class="login-area">
                    <form action="loginProc.do" method="post" name="frm_login" class="login-form">
                        <div style="margin-top:-20px;">

                            <div class="txt-field mb10">
                                <label class="id">아이디</label>
                                <input type="text" id="user_id" name="user_id" value="">
                            </div>
                            <div class="txt-field">
                                <label class="pw">비밀번호</label>
                                <input type="password" id="passwd" name="passwd" value="" maxlength="">
                            </div>


                            <!-- Google reCAPTCHA 위젯 -->
                            <div id="recaptcha-widget" class="g-recaptcha" data-sitekey="6LeRlh0pAAAAAAP7cUX8occWnRcoReTPs3JOnv33" style="margin-top:10px;"></div>

                            <button type="submit" name="btn_login" class="btn-login" id="btnLogin"><span>Login</span></button>

                            <span class="check-box1">
                                <input type="checkbox" id="chkId" name="chkId" value="agent">
                                <label for="chkId">Save ID</label>
                            </span>
                        </div>
                    </form>
                </div>

		</div>
		<!-- //contents -->
	</div>
	<!-- //container -->

	<!-- footer -->
	<!-- footer -->
	<footer class="footer">
		<p class="copy">Copyright &copy 2017 KT SAT All rights reserved.</p>
	</footer>
	<!-- // footer -->
	<!-- //footer -->
</div>
<script>
$(document).ready(function() {

	bodyH();
	function bodyH() {
		var windowH = $(window).height();
		var headerH = $('.header').outerHeight();
		var footerH = $('.footer').outerHeight();
		var contentH = $('.contents-area').outerHeight();

		$('.container').height(windowH-headerH-footerH).css({minHeight:contentH});
		$('.contents-area').css({marginTop:-contentH/2});
	}

	// for resize
	$( window ).resize(function() {
		bodyH();
	});

});





$(document).ready(function() {

});

</script>
</body>
</html>