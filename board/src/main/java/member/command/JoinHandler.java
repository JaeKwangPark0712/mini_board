package member.command;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import member.service.DuplicateIdException;
import member.service.JoinRequest;
import member.service.JoinService;
import mvc.command.CommandHandler;

public class JoinHandler implements CommandHandler{
	
	// 가입에 실패했을 때 다시 보여줄 form화면을 상수로 지정
	private static final String FORM_VIEW = "/WEB-INF/view/joinForm.jsp";
	// Handler 클래스에서 사용 할 Service 객체 생성
	private JoinService joinService = new JoinService();
	
	// 공통 인터페이스의 추상 클래스 구현
	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 회원가입 페이지에서 GET 방식으로 요청이 올 경우 processForm 실행
		if(request.getMethod().equalsIgnoreCase("GET")) {
			return processForm(request, response);
		}
		// 회원가입 페이지에서 POST 방식으로 요청이 올 경우 prpcessSubmit 실행
		else if(request.getMethod().equalsIgnoreCase("POST")) {
			return processSubmit(request, response);
		}
		// 그 외의 방법으로 요청이 올 경우 response 객체에 405 상태코드 저장!
		else {
			response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return null;
		}
	}

	private String processForm(HttpServletRequest request, HttpServletResponse response) {
		// 폼 화면을 다시 반환
		return FORM_VIEW;
	}

	private String processSubmit(HttpServletRequest request, HttpServletResponse response) {
		// 가입하는 회원의 정보를 담고 있는 JoinRequest 객체 생성
		JoinRequest joinReq = new JoinRequest();
		// JoinRequest 객체의 속성값으로, 파라메터에 입력한 값들을 저장
		joinReq.setId(request.getParameter("id"));
		joinReq.setName(request.getParameter("name"));
		joinReq.setPassword(request.getParameter("password"));
		joinReq.setConfirmPassword(request.getParameter("confirmPassword"));
		
		// 오류 정보를 담는 HashMap 객체를 만들어 request 객체의 속성으로 저장
		Map<String, Boolean> errors = new HashMap<>();
		request.setAttribute("errors", errors);
		
		// validate() 메서드로 form 창에 오류가 있는지 확인
		joinReq.validate(errors);
		
		// 오류가 있을 경우, 폼 화면을 다시 보여줌
		if(!errors.isEmpty()) {
			return FORM_VIEW;
		}
		try {
			// JoinRequest 객체로 새로운 id를 생성!
			joinService.join(joinReq);
			// 가입 성공 페이지를 띄워줌
			return "/WEB-INF/view/joinSuccess.jsp";
		} catch(DuplicateIdException e) {
			// id가 중복될 경우, errors 맵에 오류코드를 추가하고 폼 화면을 다시 보여줌
			errors.put("duplicateId", Boolean.TRUE);
			return FORM_VIEW;
		}
		
		
	}

}
