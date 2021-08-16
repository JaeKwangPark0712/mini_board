package article.service;

import java.sql.Connection;
import java.sql.SQLException;

import article.dao.ArticleContentDao;
import article.dao.ArticleDao;
import article.model.Article;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

public class ModifyArticleService {// Request 객체를 사용하는 service 클래스 작성
	
	// service 클래스 내에서 사용 할 Dao 객체 생성
	private ArticleDao articleDao = new ArticleDao();
	private ArticleContentDao contentDao = new ArticleContentDao();
	
	// ModifyArticleService 클래스의 주요 메서드
	public void modify(ModifyRequest modReq) {
		Connection conn = null;
		try {
			// 커넥션 풀에서 커넥션을 얻어옴
			conn = ConnectionProvider.getConnection();
			// 트랜젝션 처리를 위해 AutoCommit 비활성화
			conn.setAutoCommit(false);
			
			// ArticleDao 객체의 selectById() 메서드를 사용해서 수정 할 게시글을 불러옴
			Article article = articleDao.selectById(conn, modReq.getArticleNumber());
			if(article == null) {
				// 수정 할 Article 객체를 얻어오지 못했을 때 예외 발생
				throw new ArticleNotFoundException();
			}
			if(!canModify(modReq.getUserId(), article)) {
				// 게시글 수정 권한이 없을 경우 예외 발생
				throw new PermissionDeniedException();
			}
			// 게시글을 불러오는 데 성공했을 경우, ArticleDao 객체와 ArticleContentDao 객체의 update() 메서드를 실행
			articleDao.update(conn, modReq.getArticleNumber(), modReq.getTitle());
			contentDao.update(conn, modReq.getArticleNumber(), modReq.getContent());
			// 쿼리문 실행에 성공했을 경우 commit 을 통해 변경 사항 DB에 적용
			conn.commit();
		} catch (SQLException e) {// 예외가 발생했을 경우, rollback() 메서드를 실행하여 변경사항을 되돌림
			JdbcUtil.rollback(conn);
			throw new RuntimeException(e);
		} catch(PermissionDeniedException e) {
			JdbcUtil.rollback(conn);
			throw e;
		} finally {
			// 메모리 확보를 위해 사용한 자원 닫아주기
			JdbcUtil.close(conn);
		}
	}

	// 로그인 한 유저의 아이디와 글 작성자의 아이디를 비교해서 글 수정 권한이 있는지 확인하는 메서드
	private boolean canModify(String modifyingUserId, Article article) {
		return article.getWriter().getId().equals(modifyingUserId);
	}

}
