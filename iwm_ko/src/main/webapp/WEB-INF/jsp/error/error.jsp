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

	alert('${errorMessage}');
	self.location.replace("/login/login.do");
	session.invalidate();
	
</script>

</head>

<body>



</body>

</html>