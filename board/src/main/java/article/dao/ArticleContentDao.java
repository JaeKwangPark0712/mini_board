package article.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import article.model.ArticleContent;
import jdbc.JdbcUtil;

public class ArticleContentDao {
	
	public ArticleContent insert(Connection conn, ArticleContent content) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			// insert 쿼리문 작성하여 PreparedStatement 생성!
			pstmt = conn.prepareStatement("insert into article_content values(?, ?)");
			// pstmt의 set 함수로 각각의 쿼리문에 Article 객체에서 얻어온 정보들 삽입
			pstmt.setLong(1, content.getNumber());
			pstmt.setString(2, content.getContent());
			// executeUpdate() : 쿼리문에서 변경된 자료의 수를 반환하는 함수
			int insertedCount = pstmt.executeUpdate();
			// 정상적으로 쿼리문이 실행됐을 경우(데이터 저장에 성공했을 경우)
			if(insertedCount > 0) {
				// content 객체 반환
				return content;
			} else {
				// insert 쿼리문이 정상적으로 실행되지 않았을 경우(데이터 저장에 실패했을 경우) null값 반환
				return null;
			}
		} finally {
			// 메모리 확보를 위해 사용한 자원 닫아주기!
			JdbcUtil.close(pstmt);
		}
	}
	
	// 게시글 번호로 특정 게시글의 내용을 불러오는 메서드
	public ArticleContent selectById(Connection conn, int no) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			// 게시글 번호로 특정 게시글의 내용을 불러오는 쿼리문
			pstmt = conn.prepareStatement("select * from article_content where article_no = ?");
			pstmt.setInt(1, no);
			// ResultSet에 받아온 게시글 내용 저장
			rs = pstmt.executeQuery();
			ArticleContent content = null;
			if(rs.next()) {
				// 받아온 게시글 정보를 갖고 새로운 ArticleContent 객체 생성
				content = new ArticleContent(rs.getInt("article_no"), rs.getString("content"));
			}
			return content;
		} finally {
			// 메모리 확보를 위해 사용한 자원 닫아주기!
			JdbcUtil.close(rs);
		}
	}
}
