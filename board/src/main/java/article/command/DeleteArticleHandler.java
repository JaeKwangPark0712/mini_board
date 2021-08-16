package article.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import article.service.ArticleData;
import article.service.ArticleNotFoundException;
import article.service.DeleteArticleService;
import article.service.DeleteRequest;
import article.service.PermissionDeniedException;
import article.service.ReadArticleService;
import auth.service.User;
import mvc.command.CommandHandler;

public class DeleteArticleHandler implements CommandHandler{
	
	// Handler 클래스에서 사용 할 Service 클래스 객체 생성
	private DeleteArticleService deleteService = new DeleteArticleService();
	// 게시글의 정보를 가져오기 위한 ReadeArticleService 객체 생성
	private ReadArticleService readService = new ReadArticleService();
	
	// 공통 인터페이스의 추상 메서드 구현!
	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// Session 객체에서 현재 로그인 한 사람의 정보를 담은 User 객체를 가져옴
		User authUser = (User) request.getSession().getAttribute("authUser");
		// request 객체에서 페이지 번호(no)를 받아와서 int 유형으로 파싱!
		String noVal = request.getParameter("no");
		int no = Integer.parseInt(noVal);
		// 게시글의 조회수를 중가시키지 않으면서 게시글의 정보를 가져옴
		ArticleData articleData = readService.getAtricle(no, false);
		
		// 게시글 삭제에 관한 정보를 담고있는 DeleteRequest 객체 생성
		DeleteRequest delReq = new DeleteRequest(authUser.getId(), no);
		// DeleteRequest 객체를 request 객체의 속성으로 저장
		request.setAttribute("delReq", delReq);
		
		if(!canDalete(authUser, articleData)) {
			// 게시글을 삭제할 권한이 없는 경우, response 객체에 오류코드 전송
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return null;
		}
		try {
			// 게시글 삭제 권한에 문제가 없다면 DeleteArticleService 객체의 delete() 메서드 실행
			deleteService.delete(delReq);
			// 게시글 삭제 성공 페이지 반환
			return "/WEB-INF/view/deleteSuccess.jsp";
		} catch(ArticleNotFoundException e) {
			// 삭제할 게시글을 찾지 못했을 경우 response 객체에 에러코드 전송
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null;
		} catch(PermissionDeniedException e) {
			// 게시글을 삭제할 권한이 없는 경우 response 객체에 에러코드 전송!
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return null;
		}
	}

	private boolean canDalete(User authUser, ArticleData articleData) {
		return articleData.getArticle().getWriter().getId().equals(authUser.getId());
	}
	
}