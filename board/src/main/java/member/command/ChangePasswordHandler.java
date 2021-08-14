package member.command;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import auth.service.User;
import member.service.ChangePasswordService;
import member.service.InvalidPasswordException;
import member.service.MemberNotFoundException;
import mvc.command.CommandHandler;

public class ChangePasswordHandler implements CommandHandler{
	
	// 비밀번호 변경 폼 화면 경로를 상수로 지정!
	private static final String FORM_VIEW = "/WEB-INF/view/changePwdForm.jsp";
	// Handler 클래스에서 사용할 Service 객체 생성!
	private ChangePasswordService changePasswordService = new ChangePasswordService();
	
	// 공통 인터페이스의 추상 클래스 구현
	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(request.getMethod().equalsIgnoreCase("GET")) {
			// 비밀번호 변경 페이지에서 GET 방식으로 요청이 올 경우 processForm 실행
			return processForm(request, response);
		} else if(request.getMethod().equalsIgnoreCase("POST")) {
			// 비밀번호 변경 페이지에서 POST 방식으로 요청이 올 경우 processSubmit 실행
			return processSubmit(request, response);
		} else {
			// 그 외의 방법으로 요청이 올 경우 response 객체에 405 상태코드 저장!
			response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return null;
		}

	}

	private String processForm(HttpServletRequest request, HttpServletResponse response) {
		// 폼 화면을 다시 반환
		return FORM_VIEW;
	}

	private String processSubmit(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// request 객체에서 얻어온 Session에서 authUser 속성을 얻어옴
		User user = (User) request.getSession().getAttribute("authUser");
		
		// 오류 키를 담는 errors 맵 객체 생성
		Map<String, Boolean> errors = new HashMap<>();
		// request 객체에 errors 속성 설정
		request.setAttribute("errors", errors);
		
		// request 객체에서 curPwd, newPwd 파라메터값을 얻어옴
		String curPwd = request.getParameter("curPwd");
		String newPwd = request.getParameter("newPwd");
		
		// 현재 비밀번호 입력창, 새로운 비밀번호 입력창이 비어있을 경우 errors 객체에 에러코드 추가
		if(curPwd == null || curPwd.isEmpty()) {
			errors.put("curPwd", Boolean.TRUE);
		}
		if(newPwd == null || newPwd.isEmpty()) {
			errors.put("newPwd", Boolean.TRUE);
		}
		// 에러가 있을 경우 다시 폼 화면을 보여줌
		if(!errors.isEmpty()) {
			return FORM_VIEW;
		}
		
		try {
			changePasswordService.changePassword(user.getId(), curPwd, newPwd);
			return "/WEB-INF/view/changePwdSuccess.jsp";
		} catch(InvalidPasswordException e) {
			// 현재 비밀번호가 일치하지 않아서 예외가 발생했을 경우 errors 객체에 오류코드 추가
			errors.put("badCurPwd", Boolean.TRUE);
			// 폼 화면을 다시 보여줌
			return FORM_VIEW;
		} catch(MemberNotFoundException e) {
			// 비밀번호를 변경할 유저의 정보를 찾지 못했을 경우 response 객체에 오류코드 전송(400번)
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}
	}
	
	
	
}
