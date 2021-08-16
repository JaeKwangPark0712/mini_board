package article.service;

import java.sql.Connection;
import java.sql.SQLException;

import article.dao.ArticleContentDao;
import article.dao.ArticleDao;
import article.model.Article;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

public class DeleteArticleService {
	
	// service 클래스에서 시용할 DAO 객체 생성
	private ArticleDao articleDao = new ArticleDao();
	private ArticleContentDao contentDao = new ArticleContentDao();
	
	// DeleteArticleService 클래스의 주요 메서드!
	public void delete(DeleteRequest delReq) {
		Connection conn = null;
		try {
			// 커넥션 풀에서 커넥션을 얻어옴
			conn = ConnectionProvider.getConnection();
			// 트랜젝션 처리를 위해 Auto Commit 비활성화!
			conn.setAutoCommit(false);
			// ArticleDao 객체의 selectById() 메서드를 통해 삭제하려는 메서드를 불러옴
			Article article = articleDao.selectById(conn, delReq.getArticleNumber());
			if(article == null) {
				// Article 객체를 얻어오지 못 할 경우 예외 처리!
				throw new ArticleNotFoundException();
			}
			if(!canDelete(delReq.getUserId(), article)) {
				// 로그인 한 유저와 게시글 작성자가 다를 경우 예외처리!
				throw new PermissionDeniedException();
			}
			//Article 객체를 정상적으로 불러왔을 경우, ArticleDao 객체와 ArticleContentDao 객체의 delete() 메서드를 실행시킨다!
			articleDao.delete(conn, delReq.getArticleNumber());
			contentDao.delete(conn, delReq.getArticleNumber());
			// commit을 통해 DB에 변경된 사항을 적용시킨다!
			conn.commit();
		} catch(SQLException e) {
			// 게시글 삭제가 정상적으로 이루어지지 않은 경우, rollback() 메서드를 통해 변경사항을 되돌린다!
			JdbcUtil.rollback(conn);
			throw new RuntimeException(e);
		} catch(PermissionDeniedException e) {
			// 게시글 삭제 권한이 없을 경우, rollback() 메서드를 실행시킨다!
			JdbcUtil.rollback(conn);
			throw e;
		} finally {
			// 메모리 자원 확보를 위해 사용을 끝낸 Connection은 닫아준다!
			JdbcUtil.close(conn);
		}
	}
	
	// 게시글을 삭제하려는 유저와 게시글을 작성한 유저가 일치하는지 확인하는 메서드
	private boolean canDelete(String userId, Article article) {
		return article.getWriter().getId().equals(userId);
	}
}