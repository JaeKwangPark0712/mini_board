package article.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import article.service.ArticleContentNotFoundException;
import article.service.ArticleData;
import article.service.ArticleNotFoundException;
import article.service.ReadArticleService;
import mvc.command.CommandHandler;

public class ReadArticleHandler implements CommandHandler{// 공통 인터페이스를 상속받은 Handler 클래스
	
	// Handler 클래스에서 사용할 Service 객체 생성
	private ReadArticleService readService = new ReadArticleService();

	// 공통 인터페이스의 추상 메서드 구현
	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// request 객체에서 얻어온 파라메터 값 저장(게시글 번호)
		String noVal = request.getParameter("no");
		int articleNum = Integer.parseInt(noVal);
		try {
			// ReadArticleService 객체의 getArticle() 메서드를 사용해서 ArticleData 객체 생성
			ArticleData articleData = readService.getAtricle(articleNum, true);
			// request 객체의 속성으로 articleData 저장!
			request.setAttribute("articleData", articleData);
			// 게시글 조회 페이지 주소 반환
			return "/WEB-INF/view/readArticle.jsp";
		} catch(ArticleNotFoundException | ArticleContentNotFoundException e) {// 예외가 발생했을 때
			// Context 객체에 로그 메세지 작성
			request.getServletContext().log("no article", e);
			// response 객체에 오류 코드 전송
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		
	}
	
	
}
