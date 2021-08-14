package auth.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import mvc.command.CommandHandler;

//공통 인터페이스를 상속 받아 구현
public class LogoutHandler implements CommandHandler{

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// request 객체에서 Session 객체를 가져옴(단, 열려있는 세션이 없을경우 새로 만들지 않음)
		HttpSession session = request.getSession(false);
		if(session != null) {
			// 현재 열려있는 세션을 닫아 로그아웃기능 구현!
			session.invalidate();
		}
		// 로그아웃에 셩공했을 시, index.jsp 페이지로 돌아감
		response.sendRedirect(request.getContextPath() + "/index.jsp");
		return null;
	}

}
