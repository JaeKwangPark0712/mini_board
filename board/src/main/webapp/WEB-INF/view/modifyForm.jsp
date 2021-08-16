<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 수정</title>
</head>
<body>
	<form action="modify.do" method="post">
		<input type="hidden" name="no" value="${modReq.articleNumber }">
		<p>
			 번호: <br/> ${modReq.articleNumber }
		</p>
		<p>
			<!-- 폼화면으로 진입했을 때, 그 전에 자신이 작성했던 제목을 다시 보여주기 위해 value 속성에 parameter 값 대입 -->
			제목: <br/> <input type="text" name="title" value="${modReq.title }">
			<c:if test="${errors.title }">제목을 입력하세요!</c:if>
		</p>
		<p>
			내용: <br/> 
			<!-- 폼화면으로 진입했을 때, 그 전에 자신이 작성했던 내용을 다시 보여주기 위해 value 속성에 parameter 값 대입 -->
			<textarea rows="5" cols="30" name="content">${modReq.content }</textarea>
		</p>
		<input type="submit" value="글 수정">
	</form>
</body>
</html>