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

public class WriteArticleHandler implements CommandHandler{
	
	// 글 작성 작업에 실패할 시 보여줄 폼 화면 경로를 상수값으로 지정!
	private static final String FORM_VIEW = "/WEB-INF/view/newArticleForm.jsp";
	// Handler 객체에서 사용 할 service 객체 생성
	private WriteArticleService writeService = new WriteArticleService();
	
	// CommandHandler의 추상 메서드 오버라이딩!
	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(request.getMethod().equalsIgnoreCase("GET")) { // GET 방식으로 요청이 들어올 경우(글 작성에 실패 할 경우) 실행되는 메서드
			return processForm(request, response);
		} else if(request.getMethod().equalsIgnoreCase("POST")) {// POST 방식으로 요청이 들어올 경우 실행되는 메서드
			return processSubmit(request, response);
		} else {
			// GET, POST 이외의 방식으로 요청이 들어올 경우 response 객체에 상테 지정!
			response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return null;
		}
	}

	private String processForm(HttpServletRequest request, HttpServletResponse response) {
		// GET 방식으로 요청이 들어올 경우 폼 화면을 다시 반환!
		return FORM_VIEW;
	}

	private String processSubmit(HttpServletRequest request, HttpServletResponse response) {
		// 폼 태그 관련 오류들을 저장 할 Map 객체 생성
		Map<String, Boolean> errors = new HashMap<>();
		// request 객체의 속성으로 'errors' 맵 객체 설정!
		request.setAttribute("errors", errors);
		
		// Session객체에서 'authUser'속성을 얻어와 User 타입으로 캐스팅
		User user = (User) request.getSession(false).getAttribute("authUser");
		//request 객체의 파라메터와 로그인 한 user의 속성을 통해 WriterRequest 객체 생성!
		WriteRequest writeRequest = createWtireRequest(user, request);
		// WriterRequest 객체의 validate() 메서드를 통해 폼 화면의 오류 점검!
		writeRequest.validate(errors);
		
		// errors 맵 객체에 오류 정보가 있을 경우 다시 폼 화면을 반환!
		if(!errors.isEmpty()) {
			return FORM_VIEW;
		}
		
		// writeService 객체의 write() 메서드를 이용해서 새로 작성하는 게시글의 번호를 얻어옴!
		int newArticleNo = writeService.write(writeRequest);
		// request 객체의 속성값으로 newArticleNo 저장!
		request.setAttribute("newArticleNo", newArticleNo);
		
		return "/WEB-INF/view/newArticleSuccess.jsp";
	}
	
	// request 객체의 파라메터와 로그인 한 user의 속성을 통해 WriterRequest 객체를 만들어주는 메서드
	private WriteRequest createWtireRequest(User user, HttpServletRequest req) {
		return new WriteRequest(new Writer(user.getId(), user.getName()), req.getParameter("title"), req.getParameter("content"));
	}

}
