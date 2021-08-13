package article.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import article.service.ArticlePage;
import article.service.ListArticleService;
import mvc.command.CommandHandler;

public class ListArticleHandler implements CommandHandler{
	
	// 페이지 계산에 사용할 ListArticleService 객체 생성
	private ListArticleService listService = new ListArticleService();

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// request 객체의 파라메터값을 얻어옴
		String pageNoVal = request.getParameter("pageNo");
		int pageNo = 1;
		// 얻어온 파라메터가 null이 아닐 경우, Integer로 파싱해서 pageNo에 대입, null일 경우 1로 초기화!
		if(pageNoVal != null) {
			pageNo = Integer.parseInt(pageNoVal);
		}
		// ListArticleService 객체의 getArticlePage 메서드를 통해 ArticlePage 객체를 얻어옴!
		ArticlePage articlePage = listService.getArticlePage(pageNo);
		// 얻어온 ArticlePage 객체를 request 객체의 속성으로 대입!
		request.setAttribute("articlePage", articlePage);
		// 게시글 목록 페이지 주소 반환
		return "/WEB-INF/view/listArticle.jsp";
	}
}
