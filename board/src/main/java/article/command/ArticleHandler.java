package article.command;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import article.model.Writer;
import article.service.WriteArticleService;
import article.service.WriteRequest;
import auth.service.User;
import mvc.command.CommandHandler;

public class ArticleHandler implements CommandHandler{// 공통 인터페이스를 상속받는 Handler 클래스
	
	// 게시글 작성 폼 화면 경로를 상수로 지정
	private static final String FORM_VIEW = "/WEB-INF/view/newArticleForm.jsp";
	// Handler 클래스에서 사용할 WriteArticleService 객체 생성
	private WriteArticleService writeService = new WriteArticleService();
	
	// 공통 인터페이스의 추상 메서드 구현
	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 글 작성 페이지에서 GET 방식으로 요청이 올 경우 processForm 실행
		if(request.getMethod().equalsIgnoreCase("GET")) {
			return processForm(request, response);
		}
		// 글 작성 페이지에서 POST 방식으로 요청이 올 경우 processSubmit 실행
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
		// 오류 정보를 담을 HashMap 객체 생성
		Map<String, Boolean> errors = new HashMap<>();
		// request 객체에 errors 속성 저장! 
		request.setAttribute("errors", errors);
		
		// session에서 현재 로그인한 사람의 정보를 얻어옴
		User user = (User) request.getSession(false).getAttribute("authUser");
		// 로그인 한 유저 정보와 request 객체의 파라메터값으로 새로운 WriteRequest 객체를 생성해주는 메서드
		WriteRequest writeReq = createWriteRequest(user, request);
		// 오류가 있는지 확인한 후 오류가 있을 시 errors 맵 객체에 오류코드 추가
		writeReq.validate(errors);
		
		// errors 객체에 오류코드가 있을 경우 폼 화면을 다시 보여줌
		if(!errors.isEmpty()) {
			return FORM_VIEW;
		}
		
		// WriteArticleService 객체의 write() 메서드를 이용해서 DB에 게시글 정보를 저장하고 저장한 게시글의 번호를 얻어옴
		int newArticleNo = writeService.write(writeReq);
		// 새로운 게시글 번호를 request 객체의 속성으로 저장
		request.setAttribute("newArticleNo", newArticleNo);
		
		// 회원 가입 성공 결과창을 띄워줌
		return "/WEB-INF/view/newArticleSuccess.jsp";
	}
	
	// 로그인 한 유저 정보와 request 객체의 파라메터값으로 새로운 WriteRequest 객체를 생성해주는 메서드
	private WriteRequest createWriteRequest(User user, HttpServletRequest request) {
		return new WriteRequest(new Writer(user.getId(), user.getName()), request.getParameter("title"), request.getParameter("content"));
	}
}
