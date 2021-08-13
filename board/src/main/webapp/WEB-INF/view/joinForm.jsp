<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>가입창</title>
</head>
<body>
	<form action="join.do" method="post">
		<p>
			<!-- 오류가 생겨 다시 가입창으로 돌아왔을 때, id를 재입력하지 않도록 value 값에 추가해줌 -->
			아이디: <br/> <input type="text" name="id" value="${param.id }">
			<c:if test="${errors.id }">ID를 입력하세요!</c:if>
			<c:if test="${errors.duplicateId }">이미 사용중인 아이디입니다.</c:if>
		</p>
		<p>
			<!-- 오류가 생겨 다시 가입창으로 돌아왔을 때, 이름을 재입력하지 않도록 value 값에 추가해줌 -->
			이름: <br/> <input type="text" name="name" value="${param.name }">
			<c:if test="${errors.name }">이름을 입력하세요!</c:if>
		</p>
		<p>
			암호: <br/> <input type="password" name="password">
			<c:if test="${errors.password }">암호를 입력하세요!</c:if>
		</p>
		<p>
			암호 확인: <br/> <input type="password" name="confirmPassword">
			<c:if test="${errors.confirmPassword }">암호 확인을 입력하세요!</c:if>
			<c:if test="${errors.notMatch }">암호와 암호 확인이 일치하지 않습니다!</c:if>
		</p>
		<input type="submit" value="가입하기">
	</form>
</body>
</html>