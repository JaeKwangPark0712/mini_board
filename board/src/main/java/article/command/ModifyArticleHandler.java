package article.command;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import article.service.ArticleData;
import article.service.ArticleNotFoundException;
import article.service.ModifyArticleService;
import article.service.ModifyRequest;
import article.service.PermissionDeniedException;
import article.service.ReadArticleService;
import auth.service.User;
import mvc.command.CommandHandler;

public class ModifyArticleHandler implements CommandHandler{// 공통 인터페이스를 상속받은 Handler 클래스
	
	// 게시글 수정 폼 화면 주소를 상수로 지정
	private static final String FORM_VIEW = "/WEB-INF/view/modifyForm.jsp";
	
	// Handler 클래스에서 사용할 ReadArticleService 객체와 ModifyArticleService 객체 생성
	private ReadArticleService readService = new ReadArticleService();
	private ModifyArticleService modifyService = new ModifyArticleService();
	
	// 공통 인터페이스의 추상 메서드 구현
	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 글 수정 페이지에서 GET 방식으로 요청이 올 경우 processForm 실행
		if(request.getMethod().equalsIgnoreCase("GET")) {
			return processForm(request, response);
		}
		// 글 수정 페이지에서 GET 방식으로 요청이 올 경우 processForm 실행
		else if(request.getMethod().equalsIgnoreCase("POST")) {
			return processSubmit(request, response);
		}
		// 그 외의 방법으로 요청이 올 경우 response 객체에 405 상태코드 저장!
		else {
			response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return null;
		}
	}

	private String processForm(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		try {
			// request 객체에서 파라메터값을 가져와 변수에 담음
			String noVal = request.getParameter("no");
			int no = Integer.parseInt(noVal);
			// 게시글의 조회수를 늘리지 않으면서 파라메터에서 얻어온 게시글 번호를 통해 게시글의 정보를 불러옴
			ArticleData articleData = readService.getAtricle(no, false);
			// session 객체에서 로그인 한 유저의 정보를 불러옴
			User authUser = (User) request.getSession().getAttribute("authUser");
			if(!canModify(authUser, articleData)) {
				// 게시글 수정 권한이 없는 경우, response 객체에 오류코드 전송
				response.sendError(HttpServletResponse.SC_FORBIDDEN);
				return null;
			}
			// ReadArticleService 객체의 메서드로 얻어온 ArticleData 객체와 session 의 속성으로 얻어온 User 객체를 통해 ModifyRequest 객체 생성!
			ModifyRequest modReq = new ModifyRequest(authUser.getId(), no, articleData.getArticle().getTitle(), articleData.getContent());
			// request 객체의 속성으로 modReq 저장!
			request.setAttribute("modReq", modReq);
			// 폼 화면을 다시 보여줌
			return FORM_VIEW;
		} catch(ArticleNotFoundException e) {
			// 예외가 발생했을 경우 response 객체에 에러코드 전송!
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		
	}
	
	// 로그인 한 유저와 게시글을 작성한 유저의 아이디를 비교해서 게시글 수정 권한이 있는지 확인하는 메서드
	private boolean canModify(User authUser, ArticleData articleData) {
		String writerId = articleData.getArticle().getWriter().getId();
		return authUser.getId().equals(writerId);
	}

	private String processSubmit(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// session 객체에서 로그인 한 유저의 정보를 불러옴
		User authUser = (User) request.getSession().getAttribute("authUser");
		// request 객체에서 파라메터값을 가져와 변수에 담음
		String noVal = request.getParameter("no");
		int no = Integer.parseInt(noVal);
		
		// session 의 속성으로 얻어온 User 객체와 request 객체의 파라메터를 통해 ModifyRequest 객체 생성!
		ModifyRequest modReq = new ModifyRequest(authUser.getId(), no, request.getParameter("title"), request.getParameter("content"));
		// request 객체의 속성으로 modReq 지정!
		request.setAttribute("modReq", modReq);
		
		// 오류 정보를 담을 HashMap 객체 생성
		Map<String, Boolean> errors = new HashMap<>();
		// request 객체의 속성으로 errors 지정!
		request.setAttribute("errors", errors);
		// 에러가 존재할 경우 다시 폼 화면을 보여줌
		if(!errors.isEmpty()) {
			return FORM_VIEW;
		}
		try {
			// 생성한 ModifyRequset 객체를 통해 ModifyArticleService 객체의 modify() 메서드 실행
			modifyService.modify(modReq);
			// 게시글 수정 성공 화면 주소 반환
			return "/WEB-INF/view/modifySuccess.jsp";
		} catch(ArticleNotFoundException e) {// 예외가 발생했을 경우 response 객체에 오류 코드 전송
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null;
		} catch(PermissionDeniedException e) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return null;
		}
	}

}
