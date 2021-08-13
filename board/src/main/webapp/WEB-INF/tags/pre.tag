<%@ tag language="java" pageEncoding="UTF-8"%>
<!-- 태그 앞 뒤의 빈 공간 제거 -->
<%@ tag trimDirectiveWhitespaces="true" %>
<!-- 커스텀 태그에 String 타입의 필수 속성 'value' 지정! -->
<%@ attribute name="value" type="java.lang.String" required="true" %>
<%
	/* HTML 문서 상에서 특수문자를 제대로 출력하기 위해서 replace 함수를 사용하여 문자열 대치! */
	value = value.replace("<", "&lt;");
	value = value.replace("\n", "\n<br/>");
	value = value.replace("&", "&amp;");
	value = value.replace(" ", "&nbsp;");
%>
<%=value %>