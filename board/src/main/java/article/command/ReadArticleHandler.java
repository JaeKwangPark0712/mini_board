package article.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import article.service.ArticleContentNotFoundException;
import article.service.ArticleData;
import article.service.ArticleNotFoundException;
import article.service.ReadArticleService;
import mvc.command.CommandHandler;

public class ReadArticleHandler implements CommandHandler{
	
	// ReadArticleHandler 클래스에서 사용할 ReadArtirlcService 객체 생성 - 멤버 필드로 설정!
	private ReadArticleService readService = new ReadArticleService();
	
	// 상속받은 인터페이스의 추상 메서드 구현!
	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 파라메터를 얻어와 int 값으로 파싱!
		String noVal = request.getParameter("no");
		int articleNum = Integer.parseInt(noVal);
		try {
			// ReadArtirlcService 객체의 getArticle() 메서드를 사용해서 ArticleData 객체 생성(+ 조회수 증가 옵션 추가)
			ArticleData articleData = readService.getArticle(articleNum, true);
			// request 객체에 'articleData' 속성 추가!
			request.setAttribute("articleData", articleData);
			// 글을 불러오는 데에 성공했을 경우, 게시글을 보여주는 jsp 페이지를 반환!
			return "/WEB-INF/view/readArticle.jsp";
		} catch(ArticleNotFoundException | ArticleContentNotFoundException e) {
			// 게시글 조회에 실패할 경우 예외를 발생시킴
			// request 객체에 로그 메세지 기록
			request.getServletContext().log("no article", e);
			// response 객체에 에러코드 전송(SC_NOT_FOUND, 404에러 코드)
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		
	}
	
	
}
