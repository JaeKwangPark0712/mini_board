package article.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import article.model.ArticleContent;
import jdbc.JdbcUtil;

public class ArticleContentDao {// 게시글의 내용을 DB에 저장하는 클래스
	
	public ArticleContent insert(Connection conn, ArticleContent content) throws SQLException {// 리턴값으로 ArticleContent을 갖는 insert 메서드!
		PreparedStatement pstmt = null;
		try {
			// 쿼리문으로 PreparedStatement를 얻어옴
			pstmt = conn.prepareStatement("insert into article_content (article_no, content) values(?, ?)");
			// 각각의 쿼리문에 articleContent 객체의 멤버 필드 값 대입
			pstmt.setLong(1, content.getNumber());
			pstmt.setString(2, content.getContent());
			int insertedCount = pstmt.executeUpdate();
			
			if(insertedCount > 0) {
				return content;
			} else {
				return null;
			}
		}finally {
			JdbcUtil.close(pstmt);
		}
	}
	// 글 번호로 특정 게시글을 불러와, 해당 게시글의 내용(ArticleContent 객체)을 가져오는 메서드
	public ArticleContent selectById(Connection conn, int no) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement("select * from article_content where article_no = ?");
			pstmt.setInt(1, no);
			rs = pstmt.executeQuery();
			ArticleContent content = null;
			
			if(rs.next()) {
				content = new ArticleContent(rs.getInt("article_no"), rs.getString("content"));
			}
			return content;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}
	
	// 글 번호로 특정 게시글을 불러와, 해당 게시글의 내용을 변경하는 메서드
	public int update(Connection conn, int no, String content) throws SQLException {
		try(PreparedStatement pstmt = conn.prepareStatement("update article_content set content = ? where article_no = ?")) {
			pstmt.setString(1, content);
			pstmt.setInt(2, no);
			return pstmt.executeUpdate();
		}
	}
	
	// 글 번호로 특정 게시글을 불러와, 해당 게시글을 제거하는 메서드
	public int delete(Connection conn, int no) throws SQLException {
		try(PreparedStatement pstmt = conn.prepareStatement("delete from article_content where article_no = ?")) {
			pstmt.setInt(1, no);
			return pstmt.executeUpdate();
		}
	}

}
