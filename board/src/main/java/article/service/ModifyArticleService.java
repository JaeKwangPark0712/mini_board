package article.service;

import java.sql.Connection;
import java.sql.SQLException;

import article.dao.ArticleContentDao;
import article.dao.ArticleDao;
import article.model.Article;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

public class ModifyArticleService {
	
	// service 클래스에서 사용 할 DAO 객체 생성
	private ArticleDao articleDao = new ArticleDao();
	private ArticleContentDao contentDao = new ArticleContentDao();
	
	// service 클래스의 주요 메서드
	public void modify(ModifyRequest modReq) {
		Connection conn = null;
		try {
			// 커넥션 풀에서 커넥션을 얻어옴
			conn = ConnectionProvider.getConnection();
			// 트랜젝션 처리를 위해 AutoCommit 비활성화
			conn.setAutoCommit(false);
			
			// ArticleDao 클래스의 selectById() 메서드를 이용해서 Article 객체를 얻어옴
			Article article = articleDao.selectById(conn, modReq.getArticleNumber());
			if(article == null) {
				// Article 객체를 얻어오지 못 할 경우 예외 처리
				throw new ArticleNotFoundException();
			}
			if(!canModify(modReq.getUserId(), article))  {
				// 게시글 수정 권한이 없을 경우 예외 처리!
				throw new PermissionDeniedException();
			}
			// Article 객체를 불러오는데 문제가 없없다면 ArticleDao 클래스와 ArticleContentDao 클래스의 update 메서드를 실행시켜 글 내용을 바꾼다!
			articleDao.update(conn, modReq.getArticleNumber(), modReq.getTitle());
			contentDao.update(conn, modReq.getArticleNumber(), modReq.getContent());
			// 커넥션을 commit 하여 DB에 변경된 부분을 적용시킨다!
			conn.commit();
		} catch(SQLException e) {
			// 예외가 발생한 경우 변경사항을 적용하지 않고 롤백시킨다!
			JdbcUtil.rollback(conn);
			throw new RuntimeException(e);
		} catch(PermissionDeniedException e) {
			JdbcUtil.rollback(conn);
			throw e;
		} finally {
			// 메모리 자원 확보를 위해 사용을 끝낸 Connection은 닫아준다!
			JdbcUtil.close(conn);
		}
	}
	
	// 게시글을 수정하려는 유저와 게시글을 작성한 유저가 일치하는지 확인하는 메서드
	private boolean canModify(String modifyingUserId, Article article) {
		return article.getWriter().getId().equals(modifyingUserId);
	}
}
