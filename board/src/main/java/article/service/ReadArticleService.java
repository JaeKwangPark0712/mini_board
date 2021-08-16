package article.service;

import java.sql.Connection;
import java.sql.SQLException;

import article.dao.ArticleContentDao;
import article.dao.ArticleDao;
import article.model.Article;
import article.model.ArticleContent;
import jdbc.connection.ConnectionProvider;

public class ReadArticleService {// 게시글 조회에 사용할 service 클래스

	// service 클래스에서 사용할 DAP 객체 생성
	private ArticleDao articleDao = new ArticleDao();
	private ArticleContentDao contentDao = new ArticleContentDao();
	
	// ReadArticleService 클래스의 주요 메서드
	public ArticleData getAtricle(int articleNum, boolean increaseReadCount) {
		try(Connection conn = ConnectionProvider.getConnection()) {
			// AtricleDao 객체의 SelectById() 메서드로 새로운 Article 객체 생성
			Article article = articleDao.selectById(conn, articleNum);
			if(article == null) {
				// Article 객체를 얻어오지 못했을경우 예외 발생
				throw new ArticleNotFoundException();
			}
			// AtricleContentDao 객체의 SelectById() 메서드로 새로운 ArticleContent 객체 생성
			ArticleContent content = contentDao.selectById(conn, articleNum);
			if(content == null) {
				// ArticleContent 객체를 얻어오지 못했을경우 예외 발생
				throw new ArticleContentNotFoundException();
			}
			if(increaseReadCount) {
				// increaseReadCount 가 true 일 경우, ArticleDao 객체의 increaseReadCount() 메서드를 실행시킴
				articleDao.increaseReadCount(conn, articleNum);
			}
			// ArticleDao와  AtricleContentDao 객체에서 얻어온 article과 content로 새로운 ArticleData 객체 생성 후 반환
			return new ArticleData(article, content);
		} catch (SQLException e) {
			// 예외가 발생했을 경우 RuntimeException 발생
			throw new RuntimeException(e);
		}
	}
}
