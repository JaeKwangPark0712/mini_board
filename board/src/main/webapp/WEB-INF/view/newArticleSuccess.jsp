<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 등록 결과</title>
</head>
<body>
	게시글을 등록했습니다. <br/>
	<!-- ContextPath 경로를 변수로 얻어오되 화면에 출력되지 않도록 세미콜론 연산자 처리! -->
	${ctxPath = pageContext.request.contextPath ; "" }
	<a href="${ctxPath }/article/list.do">[게시글 목록 보기]</a>
	<a href="${ctxPath }/article/read.do?no=${newArticleNo }">[게시글 내용 보기]</a>
</body>
</html>