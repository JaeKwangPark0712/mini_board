package auth.command;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import auth.service.LoginFailException;
import auth.service.LoginService;
import auth.service.User;
import mvc.command.CommandHandler;

// 공통 인터페이스를 상속 받아 구현
public class LoginHandler implements CommandHandler{
	
	// 로그인 폼 화면 경로를 상수로 지정!
	private static final String FORM_VIEW = "/WEB-INF/view/loginForm.jsp";
	// 로그인 기능 구현할 때 사용 할 service 객체 생성
	private LoginService loginService = new LoginService();
	
	// 공통 인터페이스의 추상 메서드 구현
	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(request.getMethod().equalsIgnoreCase("GET")) {
			// GET 방식으로 요청이 들어올 경우 processForm() 메서드 실행
			return processForm(request, response);
		} else if(request.getMethod().equalsIgnoreCase("POST")) {
			// POST 방식으로 요청이 들어올 경우, processSubmit() 메서드 실행
			return processSubmit(request, response);
		} else {
			// 이외의 방식으로 요청이 들어올 경우, response 객체에 상태 코드 지정!(405번)
			response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return null;
		}
	}

	private String processForm(HttpServletRequest request, HttpServletResponse response) {
		// processForm 메서드를 실행할경우 바로 폼 화면을 보여줌
		return FORM_VIEW;
	}

	private String processSubmit(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 파라메터에서 id와 암호 값을 얻어옴
		String id = trim(request.getParameter("id"));
		String password = trim(request.getParameter("password"));
		
		// 에러 정보를 담을 HashMap 객체를 생성해서 request 객체의 속성으로 지정함
		Map<String, Boolean> errors = new HashMap<>();
		request.setAttribute("errors", errors);
		
		// 아이디 창과 암호 창이 비어있을 경우 각각의 이름을 가진 에러 객체를 errors 맵에 넣어줌
		if(id == null || id.isEmpty()) {
			errors.put("id", Boolean.TRUE);
		}
		if(password == null || password.isEmpty()) {
			errors.put("password", Boolean.TRUE);
		}
		
		// 에러가 존재할 경우 다시 폼 화면을 보여줌
		if(!errors.isEmpty()) {
			return FORM_VIEW;
		}
		
		try {
			// 입력받은 아이디와 암호를 가지고 새로운 User 객체를 생성!
			User user = loginService.login(id, password);
			// request 객체에 User 객체를 속성으로 지정!
			request.getSession().setAttribute("authUser", user);
			// index.jsp 페이지로 리다이렉트!
			response.sendRedirect(request.getContextPath() + "/index.jsp");
			return null;
		} catch(LoginFailException e) {
			// 로그인에 실패했을 경우 errors 맵에 에러 객체를 추가한 뒤 다시 폼 화면을 보여줌
			errors.put("idOrPwdNotMatch", Boolean.TRUE);
			return FORM_VIEW;
		}

	}

	// String이 비어있으면 null을 반환하고, 그렇지 않으면 앞뒤 공백을 잘라낸 문자열을 반환하는 메서드!
	private String trim(String str) {
		return str == null ? null : str.trim();
	}
	
	
}
