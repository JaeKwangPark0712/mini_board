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

public class WriteArticleService {
	
	//멤버 필드
	// 작성한 게시글을 DB에 넣어주기 위한 ArticleDao 객체와 ArticleContentDao 객체 생성!
	private ArticleDao articleDao = new ArticleDao();
	private ArticleContentDao articleContentDao = new ArticleContentDao();
	
	//WriteArticleService의 주요 메서드
	public Integer write(WriteRequest req) {
		Connection conn = null;
		try {
			//ConnectionProvider 클래스의 getConnection() 메서드로 커넥션 풀의 커넥션 얻어옴
			conn = ConnectionProvider.getConnection();
			// 트랜젝션 처리를 위해 AutoCommit 기능 비활성화!
			conn.setAutoCommit(false);
			
			// toArticle() : WriteRequest 객체의 정보를 통해 글 작성 시간과 글 수정 시간 정보를 포함한 새로운 Article 객체를 반환하는 메서드
			Article article = toArticle(req);
			// article table에 새로운 데이터를 저장함과 동시에 Article 객체 반환!( + 게시글의 번호 정보도 함께 저장됨!)
			Article savedArticle = articleDao.insert(conn, article);
			// 쿼리문 실행에 실패했을 경우 RuntimeException 발생과 동시에 에러 메세지 출력
			if(savedArticle == null) {
				throw new RuntimeException("fail to insert article");
			}
			// ArticleDao의 insert() 메서드를 통해 얻어온 게시글 번호와 WriteRequest 객체를 통헤 얻어온 content를 통해 새로운 ArticleContent 객체 생성
			ArticleContent content = new ArticleContent(savedArticle.getNumber(), req.getContent());
			// article_content table에 새로운 데이터를 저장함과 동시에 ArticleContent 객체 반환!
			ArticleContent savedArticleContent = articleContentDao.insert(conn, content);
			// 쿼리문 실행에 실패했을 경우 RuntimeException 발생과 동시에 에러 메세지 출력
			if(savedArticleContent == null) {
				throw new RuntimeException("fail to insert article_content");
			}
			// 두 쿼리문 모두 실행에 성공했을 경우 commit 실행!
			conn.commit();
			
			// 게시글의 글 번호 반환
			return savedArticle.getNumber();
			// 예외가 발생했을 경우 데이터 처리 결과를 그대로 DB에 저장하지 않고 rollback 시키기!
		} catch(SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException(e);
		} catch(RuntimeException e) {
			JdbcUtil.rollback(conn);
			throw e;
			// 메모리 확보를 위해 사용을 마친 리소스 닫아주기
		} finally {
			JdbcUtil.close(conn);
		}
	}
	
	// toArticle() : WriteRequest 객체의 정보를 통해 글 작성 시간과 글 수정 시간 정보를 포함한 새로운 Article 객체를 반환하는 메서드
	private Article toArticle(WriteRequest req) {
		Date now = new Date();
		return new Article(null, req.getWriter(), req.getTitle(), now, now, 0);
	}
}
