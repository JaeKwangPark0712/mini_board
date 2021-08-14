<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ tag trimDirectiveWhitespaces="true" %>
<%
	/* request 객체에서 Session 을 얻어왔을 때, Session이 존재하지 않거나, Session의 authUser 속성이 존재 하지 않을 경우 태그 몸체 내용 실행 */
	HttpSession httpSession = request.getSession(false);
	if(httpSession == null || httpSession.getAttribute("authUser") == null) {
%>		
	<jsp:doBody />	
<%		
	}
%>
