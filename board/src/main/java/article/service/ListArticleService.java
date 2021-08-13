package article.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import article.dao.ArticleDao;
import article.model.Article;
import jdbc.connection.ConnectionProvider;

public class ListArticleService {
	
	private ArticleDao articleDao = new ArticleDao();
	// 한 페이지에 보여줄 게시글 수!
	private int size = 10;
	
	// ListArticleService의 주요 메서드!
	public ArticlePage getArticlePage(int pageNum) {
		try(Connection conn = ConnectionProvider.getConnection()) {
			// Dao 클래스의 selectCount() 메서드로 전체 게시글 개수를 구함.
			int total = articleDao.selectCount(conn);
			// ArticleDao 클래스의 select() 메서드로 특정 범위 내의 게시글을 전부 가져옴.
			// 각 페이지의 시작행으로부터 10개의 게시글을 불러온다!
			List<Article> content = articleDao.select(conn, (pageNum - 1) * size, size);
			// 위에서 얻어온 자료들로 ArticlePage 객체 생성 후 반환
			return new ArticlePage(total, pageNum, size, content);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
