<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="u" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 읽기</title>
</head>
<body>
	<table border="1">
		<tr>
			<!-- Handler 클래스에서 등록한 request의 속성(+EL)을 이용해서 게시글의 정보 및 내용 출력 -->
			<td>번호</td>
			<td>${articleData.article.number }</td>
		</tr>
		<tr>
			<td>작성자</td>
			<td>${articleData.article.writer.name }</td>
		</tr>		
		<tr>
			<td>제목</td>
			<td> <c:out value="${articleData.article.title }"></c:out> </td>
		</tr>
		<tr>
			<td>내용</td>
			<td> <u:pre value="${articleData.content }" /> </td>
		</tr>
		<tr>
			<td colspan="2">
				<!-- pageNo 파라메터 값이 비어있을 경우, pageNo의 값으로 1을 설정하고, 비어있지 않을 경우 파라메터의 값을 설정(삼항연산!) -->
				<c:set var="pageNo" value="${empty param.pageNo ? '1' : param.pageNo }" />
				<!-- pageNo 값을 list.do의 파라메터값으로 주소창에 전달하여 해당 페이지로 이동하는 링크 생성 -->
				<a href="list.do?pageNo=${pageNo }">[목록]</a>
				<c:if test="${authUser.id == articleData.article.writer.id }">
					<a href="modify.do?no=${articleData.article.number }">[게시글 수정]</a>
					<a href="delete.do?no=${articleData.article.number }">[게시글 삭제]</a>
				</c:if>
			</td>
		</tr>		
	</table>
</body> 
</html>