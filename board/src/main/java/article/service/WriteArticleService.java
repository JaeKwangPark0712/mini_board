package article.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import article.dao.ArticleContentDao;
import article.dao.ArticleDao;
import article.model.Article;
import article.model.ArticleContent;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

public class WriteArticleService {// 게시글 작성 기능을 제공하는 service 클래스

	// service 클래스에서 사용할 DAO 클래스 객체 생성
	private ArticleDao articleDao = new ArticleDao();
	private ArticleContentDao contentDao = new ArticleContentDao();
	
	// WriteArticleService 클래스에서 사용할 메인 메서드
	public Integer write(WriteRequest writeReq) {
		Connection conn = null;
		try {
			// 커넥션 풀에서 커넥션 객체를 얻어옴
			conn = ConnectionProvider.getConnection();
			// 트랜젝션 처리를 위해 AutoCommit 비활성화
			conn.setAutoCommit(false);
			
			// WriteRequest 객체에서 Article 객체를 얻어옴
			Article article = toArticle(writeReq);
			// ArticleDao 객체의 insert 메서드를 사용해서 DB에 article 정보를 저장하고 게시글 번호 정보를 포함한 새로운 Article 객체 생성
			Article savedArticle = articleDao.insert(conn, article);
			if(savedArticle == null) {
				// 쿼리문 실행에 실패했을 경우 예외를 발생시킴
				throw new RuntimeException("fail to insert article");
			}
			// 쿼리문 실행을 통해 얻어온 게시글 번호와 writeRequest 객체의 content 정보를 가지고 새로운 ArticleContent 객체 생성
			ArticleContent content = new ArticleContent(savedArticle.getNumber(), writeReq.getContent());
			// 새롭게 생성된 ArticleContent를 ArticleContentDao 객체의 insert 메서드를 이용해서 DB에 추가하고 그 결과로 반환되는 ArticleContent 객체를 받아옴
			ArticleContent savedContent = contentDao.insert(conn, content);
			 if(savedContent == null) {
				 // 쿼리문 실행에 실패했을 경우 예외를 발생시킴
				 throw new RuntimeException("fail to insert article_content"); 
			 }
			 
			 // 모든 쿼리문 실행에 성공했을 경우 DB에 변경사항을 적용시키기 위해 commit 실행
			 conn.commit();
			 
			 // 새로 추가된 게시글으 번호를 반환!
			 return savedArticle.getNumber();
		} catch (SQLException e) {// 쿼리문 실행에 실패해서 예외가 발생했을 경우 변경사항을 저장하지 않고 rollback 시킴
			JdbcUtil.rollback(conn);
			throw new RuntimeException(e);
		} catch(RuntimeException e) {
			JdbcUtil.rollback(conn);
			throw e;
		} finally {
			// 메모리 확보를 위해 사용을 끝낸 Connection 닫아주기
			JdbcUtil.close(conn);
		}
	}
	
	// WriteRequest 객체에서 Article 객체를 생성해주는 메서드
	private Article toArticle(WriteRequest writeReq) {
		Date now = new Date();
		return new Article(null, writeReq.getWriter(), writeReq.getTitle(), now, now, 0);
	}
}
