<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 목록</title>
</head>
<body>
	<table border="1">
		<tr>
			<td colspan="4"><a href="write.do">[게시글 쓰기]</a></td>
		</tr>
		<tr>
			<td>번호</td>
			<td>제목</td>
			<td>작성자</td>
			<td>조회수</td>
		</tr>
		
	<!-- 게시글의 목록을 출력하는 부분 -->	
		
	<!-- 게시글이 하나도 없을 경우 메세지 출력 -->
	<c:if test="${articlePage.hasNoArticles() }">
		<tr>
			<td colspan="4">게시글이 존재하지 않습니다!</td>
		</tr>
	</c:if>
	<!-- ArrayList에 있는 Article 객체를 하나씩 꺼내서 출력! -->
	<c:forEach var="article" items="${articlePage.content }">
		<tr>
			<!-- 게시글 번호 출력 -->
			<td>${article.number }</td>
			<td>
				<!-- 게시글의 제목에 게시글 내용을 볼 수 있는 창의 링크 설정 -->
				<a href="read.do?no=${article.number }&pageNo=${articlePage.currentPage }">
					<c:out value="${article.title }" />
				</a>
			</td>
			<!-- 작성자 이름과 조회수 출력 -->
			<td>${article.writer.name }</td>
			<td>${article.readCount }</td>
		</tr>
	</c:forEach>
	
	<!-- 페이지 목록을 출력하는 부분 -->
	
	<c:if test="${articlePage.hasArticles() }">
		<tr>
			<td colspan="4">
				<!-- 시작 페이지가 5보다 큰 경우 -->
				<c:if test="${articlePage.startPage > 5 }">
					<!-- [이전] 텍스트에 5페이지 전 목록으로 이동하는 링크 연결 -->
					<a href="list.do?pageNo=${articlePage.startPage - 5 }">[이전]</a>
				</c:if>
				<!-- 시작페이지부터 마지막페이지까지 각각의 페이지로 이동하는 링크를 페이지 숫자에 연결 -->
				<c:forEach var="pNo" begin="${articlePage.startPage }" end="${articlePage.endPage }">
					<a href="list.do?pageNo=${pNo }">[${pNo }]</a>
				</c:forEach>
				<!-- 마지막 페이지가 전체 페이지 수 보다 작을 경우 -->
				<c:if test="${articlePage.endPage < articlePage.totalPages }">
					<!-- [다음] 텍스트에 5페이지 이후 목록으로 이동하는 링크 연결 -->
					<a href="list.do?pageNo=${articlePage.startPage + 5 }">[다음]</a>
				</c:if>
			</td>
		</tr>
	</c:if>
	</table>
</body>
</html>