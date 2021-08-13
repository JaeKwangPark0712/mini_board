package article.service;

import java.sql.Connection;
import java.sql.SQLException;

import article.dao.ArticleContentDao;
import article.dao.ArticleDao;
import article.model.Article;
import article.model.ArticleContent;
import jdbc.connection.ConnectionProvider;

public class ReadArticleService {
	
	// 멤버 필드 : 게시글 조회에 사용할 DAO 클래스 객체 생성
	private ArticleDao articleDao = new ArticleDao();
	private ArticleContentDao contentDao = new ArticleContentDao();
	
	// ReadArticleService 클래스의 메인 메서드!
	public ArticleData getArticle(int articleNum, boolean increaseReadCount) {
		try(Connection conn = ConnectionProvider.getConnection()) {
			// ArticleDao의 selectById 메서드를 통해 새로운 Article 객체 생성
			Article article = articleDao.selectById(conn, articleNum);
			if(article == null) {
				// 쿼리문 실행에 실패했을 경우, 'ArticleNotFoundException()' 예외를 발생시킴
				throw new ArticleNotFoundException();
			}
			// ArticleContentDao의 selectById 메서드를 통해 새로운 ArticleContent 객체 생성
			ArticleContent content = contentDao.selectById(conn, articleNum);
			if(content == null) {
				// 쿼리문 실행에 실패했을 경우, 'ArticleContentNotFoundException()' 예외를 발생시킴
				throw new ArticleContentNotFoundException();
			}
			if(increaseReadCount) {
				// increaseReadCount의 값이 True일 경우, ArticleDao의 'increaseReadCount()' 메서드를 실행시킴! -> 게시글의 조회수가 1 증가함
				articleDao.increseReadCount(conn, articleNum);
			}
			// 모든 쿼리문 실행에 성공했을 경우, 새로운 Article 객체를 생성해서 반환!
			return new ArticleData(article, content);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
