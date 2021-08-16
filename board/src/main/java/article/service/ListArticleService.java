package article.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import article.dao.ArticleDao;
import article.model.Article;
import jdbc.connection.ConnectionProvider;

public class ListArticleService { // 게시글 목록을 띄워줄 때 사용할 Service 클래스
	
	// service 클래스에서 사용할 ArticleDao 객체 작성
	private ArticleDao articleDao = new ArticleDao();
	// 한 페이지에 띄워줄 게시글 수 지정
	private int size = 10;
	
	// ListArticleService 클레스의 주요 메서드
	public ArticlePage getArticlePage(int pageNum) {
		try(Connection conn = ConnectionProvider.getConnection()) {
			// ArticleDao 클래스의 selectCount() 메서드를 사용해서 총 게시글 수를 얻어옴
			int total = articleDao.selectCount(conn);
			// ArticleDao 클래스의 select() 메서드를 사용해서 범위 내의 모든 게시글을 얻어옴(시작 페이지 :  (현재 페이지 수 - 1) * 페이지 당 게시글 수)
			List<Article> content = articleDao.select(conn, (pageNum - 1) * size, size);
			// 최종적으로  AtriclePage 객체를 반환
			return new ArticlePage(total, pageNum, size, content);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
