package article.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import article.service.ArticlePage;
import article.service.ListArticleService;
import mvc.command.CommandHandler;

public class ListArticleHandler implements CommandHandler{// 공통 인터페이스를 상속받은 Handler 클래스 구현
	
	// Handler 클래스에서 사용할 service 객체 생성
	private ListArticleService listService = new ListArticleService();

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 현재 페이지 번호를 request 객체의 파라메터에서 얻어옴
		String pageNoVal = request.getParameter("pageNo");
		// 페이지 번호를 1로 초기화 한 후, 파라메터에서 얻어온 값이 null이 아니면 파라메터에서 얻어온 값으로 대체!
		int pageNo = 1;
		if(pageNoVal != null) {
			pageNo = Integer.parseInt(pageNoVal);
		}
		// 파라메터에서 얻어온 페이지 번호를 가지고 ArticlePage 객체 생성
		ArticlePage articlePage = listService.getArticlePage(pageNo);
		// ArticlePage 객체를 request 객체의 속성값으로 지정
		request.setAttribute("articlePage", articlePage);
		// 게시글 리스트 페이지 반환!
		return "/WEB-INF/view/listArticle.jsp";
	}

}
