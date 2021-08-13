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
import article.service.ReadArticleService;
import auth.service.User;
import mvc.command.CommandHandler;

public class ModifyArticleHandler implements CommandHandler{
	
	// 보여줄 폼 화면을 상수로 지정!
	private static final String FORM_VIEW = "/WEB-INF/view/modifyForm.jsp";
	
	// 게시글 내용을 보여줄 때 사용할 ReadArticleService 객체 생성
	private ReadArticleService readService = new ReadArticleService();
	// 게시글을 수정할 때 사용할 ModifyArticleService 객체 생성
	private ModifyArticleService modifyService = new ModifyArticleService();
	
	// 공통 인터페이스의 추상 메서드 구현!
	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(request.getMethod().equalsIgnoreCase("GET")) {// GET 방식으로 요청이 올 경우 processForm() 메서드 실행
			return processForm(request, response);
		} else if(request.getMethod().equalsIgnoreCase("POST")) {// POST 방식으로 요청이 올 경우 processSubmit() 메서드 실행
			return processSubmit(request, response);
		} else {// 이외의 방식으로 요청이 올 경우 response 객체에 오류 코드 지정
			response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return null;
		}	
	}

	private String processForm(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			// request의 파라메터에서 no 값을 얻어와 int 값으로 파싱!
			String noVal = request.getParameter("no");
			int no = Integer.parseInt(noVal);
			// 조회수를 더하지 않으면서 파라메터 얻어온 값의 번호를 갖는 게시글의 정보(ArticleData 객체)를 얻어옴
			ArticleData articleData = readService.getArticle(no, false);
			// Session 객체에서 현재 로그인 한 사람의 정보를 담은 User 객체를 가져옴
			User authUser = (User) request.getSession().getAttribute("authUser");
			if(!canModify(authUser, articleData)) {// 로그인 한 유저와 글 작성자가 다를 경우 response 객체에 오류코드를 전송!
				response.sendError(HttpServletResponse.SC_FORBIDDEN);
				return null;
			}
			// 게시글 수정 전의 정보를 보여주기 위해 ModifyRequest 객체 생성 후 request 객체의 속성으로 설정
			ModifyRequest modReq = new ModifyRequest(authUser.getId(), no, articleData.getArticle().getTitle(), articleData.getContent());
			request.setAttribute("modReq", modReq);
			// 폼 페이지 반환
			return FORM_VIEW;
		} catch(ArticleNotFoundException e) {// 수정하는 게시글이 존재하지 않는 경우 예외처리!
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
	}
	
	// 로그인 한 유저의 정보와 글 작성자의 정보를 비교하여, 글 수정 권한이 있는지 확인하는 메서드
	private boolean canModify(User authUser, ArticleData articleData) {
		String writerId = articleData.getArticle().getWriter().getId();
		return authUser.getId().equals(writerId);
	}

	private String processSubmit(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// Session 객체에서 현재 로그인한 사람의 정보를 담은 User 객체를 가져옴
		User authUser = (User) request.getSession().getAttribute("authUser");
		// request의 파라메터에서 no 값을 얻어와 int 값으로 파싱!
		String noVal = request.getParameter("no");
		int no = Integer.parseInt(noVal);
		// 게시글을 수정하는 기능을 수행하기 위해 ModifyRequest 객체 생성 후 request 객체의 속성으로 설정
		ModifyRequest modReq = new ModifyRequest(authUser.getId(), no, request.getParameter("title"), request.getParameter("content"));
		request.setAttribute("modReq", modReq);
		
		// 게시글 수정 폼의 오류 코드를 저장하는 errors 객체 생성
		Map<String, Boolean> errors = new HashMap<>();
		// errors 객체를 request 객체의 속성으로 전달
		request.setAttribute("errors", errors);
		// validate() 메서드로 오류 검사
		modReq.validate(errors);
		if(!errors.isEmpty()) {// errors 객체가 비어있지 않을 경우, 폼 화면을 다시 반환함
			return FORM_VIEW;
		}
		try {// 오류가 없을 경우, ModifyArticleService 객체의 modify() 메서드를 이용해서 게시글을 수정!
			modifyService.modify(modReq);
			// 게시글 변경에 성공했을 경우, 성공 페이지를 반환함
			return "/WEB-INF/view/modifySuccess.jsp";
		} catch(ArticleNotFoundException e) {// 수정할 게시글을 찾지 못할 경우 예외 처리!
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
	}
}
