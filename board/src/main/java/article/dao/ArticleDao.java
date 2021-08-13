package article.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import article.model.Article;
import article.model.Writer;
import jdbc.JdbcUtil;

public class ArticleDao {// 게시글의 정보를 DB에 저장하는 DAO 클래스
	
	public Article insert(Connection conn, Article article) throws SQLException {// 리턴값으로 Article을 갖는 insert 메서드!
		PreparedStatement pstmt = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			// 쿼리문으로 PreparedStatement를 얻어옴
			pstmt = conn.prepareStatement("insert into article (writer_id, writer_name, title, regdate, moddate, read_cnt) "
					+ "values(?, ?, ?, ?, ?, 0)");
			// 각각의 쿼리문에 article 객체의 멤버 필드 값 대입
			pstmt.setString(1, article.getWriter().getId());
			pstmt.setString(2, article.getWriter().getName());
			pstmt.setString(3, article.getTitle());
			// Date 타입의 값들은 별도의 메서드를 통해 'Timestamp'로 변환 후 대입
			pstmt.setTimestamp(4, toTimestamp(article.getRegDate()));
			pstmt.setTimestamp(5, toTimestamp(article.getModifiedDate()));
			// PreparedStatement.executeUpdate() : DML 문장을 실행시킨 횟수 반환(DML 문장 실행에 실패했을 경우 0 반환)
			int insertedCount = pstmt.executeUpdate();
			
			if(insertedCount > 0) { // DML 문장 실행에 성공했을 때 실행
				stmt = conn.createStatement();
				// last_insert_id() : 가장 최근에 추가된 데이터의 PK를 구한다!
				rs = stmt.executeQuery("select last_insert_id() from article");
				if(rs.next()) {
					Integer newNum = rs.getInt(1);
					// ResultSet으로 받아온 게시글 번호 정보를 포함한 Article 객체 생성 후 반환
					return new Article(newNum, article.getWriter(), article.getTitle(), article.getRegDate(), article.getModifiedDate(), 0);
				}
			}
			// DML 문장 실행에 실패했을 때 null 반환
			return null;
		} finally {
			// 메모리 확보를 위해 사용을 마친 프로세스 닫아주기!
			JdbcUtil.close(rs);
			JdbcUtil.close(stmt);
			JdbcUtil.close(pstmt);
		}
	}

	private Timestamp toTimestamp(Date date) {
		return new Timestamp(date.getTime());
	}
	
	// 전체 게시글 개수를 구하기 위한 메서드
	public int selectCount(Connection conn) throws SQLException {
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			// 게시글의 개수를 세는 쿼리문 작성
			rs = stmt.executeQuery("select count(*) from article");
			// 쿼리문 실행에 성공했을 경우 ResultSet에서 쿼리문 실행 결과를 가져옴
			if(rs.next()) {
				return rs.getInt(1);
			}
			// 쿼리문 실행에 실패했을 경우 0을 반환
			return 0;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(stmt);
		}
	}
	
	// 특정 구간의 게시글 전체를 불러오기 위한 메서드
	public List<Article> select(Connection conn, int startRow, int size) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			// SQL의 'limit a, b' : a번째 레코드에서부터 b개의 레코드를 얻어옴(a는 0부터 시작!)
			pstmt = conn.prepareStatement("select * from article order by article_no desc limit ?, ?");
			pstmt.setInt(1, startRow);
			pstmt.setInt(2, size);
			//쿼리문 실행의 결과를 ResultSet에 담아옴
			rs = pstmt.executeQuery();
			// 선택한 범위의 레코드들을 담을 ArrayList 객체 생성
			List<Article> result = new ArrayList<>();
			// 쿼리문 실행에 성공했을 경우 ResultSet에 있는 객체를 Article로 변환해서 하나씩 result 리스트에 넣어준다!
			while(rs.next()) {
				result.add(convertArticle(rs));
			}
			return result;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}
	
	// ResultSet 으로 얻어온 오브젝트를 Article 객체로 변환해주는 메서드!
	private Article convertArticle(ResultSet rs) throws SQLException {
		return new Article(rs.getInt("article_no"), new Writer(rs.getString("writer_id"), rs.getString("writer_name")),
				rs.getString("title"), toDate(rs.getTimestamp("regdate")), toDate(rs.getTimestamp("moddate")), rs.getInt("read_cnt"));
	}
	
	// Timestamp 객체를 Date 객체로 변환해주는 메서드!
	private Date toDate(Timestamp timestamp) {
		return new Date(timestamp.getTime());
	}
	
	// 글 번호로 특정 게시글 하나를 얻어오는 메서드
	public Article selectById(Connection conn, int no) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			// 게시글 번호를 통해 특정한 게시글의 정보를 불러오는 메서드
			pstmt = conn.prepareStatement("select * from article where article_no = ?");
			pstmt.setInt(1, no);
			rs = pstmt.executeQuery();
			Article article = null;
			
			// ResultSet으로 얻어온 객체를 통해 새로운 Article 객체 생성
			if(rs.next()) {
				// 106행에서 만든 메서드 활용!
				article = convertArticle(rs);
			}
			return article;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}

	}
	
	// 글 번호로 특정 게시글을 불러와, 해당 게시글의 조회수를 1 증가시키는 메서드
	public void increseReadCount(Connection conn, int no) throws SQLException {
		try(PreparedStatement pstmt = conn.prepareStatement("update article set read_cnt = read_cnt + 1 "
				+ "where article_no = ?")) {
			pstmt.setInt(1, no);
			pstmt.executeUpdate();
		}
	}
	
	// 글 번호로 특정 게시글을 불러와, 해당 게시글의 제목과 수정 시간을 변경하는 메서드
	public int update(Connection conn, int no, String title) throws SQLException {
		try(PreparedStatement pstmt = conn.prepareStatement("update article set title = ?, moddate = now() where article_no = ?")) {
			pstmt.setString(1, title);
			pstmt.setInt(2, no);
			return pstmt.executeUpdate();
		}
	}
	
	// 글 번호로 특정 게시글을 불러와, 해당 게시글을 제거하는 메서드
	public int delete(Connection conn, int no) throws SQLException {
		try(PreparedStatement pstmt = conn.prepareStatement("delete from article where article_no = ?")) {
			pstmt.setInt(1, no);
			return pstmt.executeUpdate();
		}
	}
}
