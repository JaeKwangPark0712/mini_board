<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 쓰기</title>
</head>
<body>
	<form action="write.do" method="post">
		<p>
			<!-- 오류로 인해 다시 폼 화면으로 돌아왔을 때, 이미 작성했던 내용이 사라지지 않도록 value 속성 지정! -->
			제목: <br/> <input type="text" name="title" value="${param.title }">
			<c:if test="${errors.title }">제목을 입력하세요!</c:if>
		</p>
		<p>
			<!-- 오류로 인해 다시 폼 화면으로 돌아왔을 때, 이미 작성했던 내용이 사라지지 않도록 태그 몸체 내용 작성! -->
			내용: <br/>
			<textarea rows="5" cols="30" name="content">${param.contents }</textarea>
		</p>
		<input type="submit" value="새 글 등록">
	</form>
</body>
</html>