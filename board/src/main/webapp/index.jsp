<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="u" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원제 게시판 예제</title>
</head>
<body>

<u:isLogin>
	${authUser.name }님, 어서오세요!
	<a href="logout.do">[로그아웃]</a>
	<a href="changePwd.do">[암호변경하기]</a>
</u:isLogin>
<u:notLogin>
	사이트 방문을 환영합니다. 로그인 후 이용해주세요!
	<a href="join.do">[회원가입]</a>
	<a href="login.do">[로그인하기]</a>
</u:notLogin>

</body>
</html>