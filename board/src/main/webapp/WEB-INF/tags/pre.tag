<%@ tag language="java" pageEncoding="UTF-8" body-content="empty"%>
<!-- 태그 앞 뒤의 공백 제거 -->
<%@ tag trimDirectiveWhitespaces="true" %>
<!-- 커스텀 태그의 필수 속성 지정 -->
<%@ attribute name="value" type="java.lang.String" required="true" %>
<%
	/* html 코드 상의 특수 문자를 실제로 보여지는 글자들로 대체 */
	value = value.replace("<", "&lt;");
	value = value.replace("\n", "\n<br/>");
	value = value.replace("&", "&amp;");
	value = value.replace(" ", "&dbsp;");
%>
<%= value %>
