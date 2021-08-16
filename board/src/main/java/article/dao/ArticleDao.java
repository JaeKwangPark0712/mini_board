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

public class ArticleDao {// 게시글 정보 관련한 쿼리문을 다루는 DAO 클래스
	
	//게시글을 작성할 때 사용하는 쿼리문
	public Article insert(Connection conn, Article article) throws SQLException {
		PreparedStatement pstmt = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			// insert 쿼리문 작성하여 PreparedStatement 생성!
			// num_seq.nextVal : 시퀀스에서 다음 값을 자동으로 불러오기 위해 사용!
			// 게시글의 조회수는 0으로 초기화
			pstmt = conn.prepareStatement("insert into article values(num_seq.nextVal, ?, ?, ?, ?, ?, 0)");
			// pstmt의 set 함수로 각각의 쿼리문에 Article 객체에서 얻어온 정보들 삽입
			pstmt.setString(1, article.getWriter().getId());
			pstmt.setString(2, article.getWriter().getName());
			pstmt.setString(3, article.getTitle());
			pstmt.setTimestamp(4, toTimestamp(article.getRegDate()));
			pstmt.setTimestamp(5, toTimestamp(article.getModifiedDate()));
			// executeUpdate() : 쿼리문에서 변경된 자료의 수를 반환하는 함수
			int insertedCount = pstmt.executeUpdate();
			// 정상적으로 쿼리문이 실행됐을 경우(데이터 저장에 성공했을 경우)
			if(insertedCount > 0) {
				stmt = conn.createStatement();
				// Statement 에서 쿼리문을 실행(가장 최근에 저장된 글 번호를 불러움)
				rs = stmt.executeQuery("select max(article_no) from article");
				if(rs.next()) {
					// 쿼리에서 얻어온 글 번호를 저장
					Integer newNum = rs.getInt(1);
					// 글 번호 정보를 포함한 새로운 Article 객체 반환!
					return new Article(newNum, article.getWriter(), article.getTitle(), article.getRegDate(), article.getModifiedDate(), 0);
				}
			}
			// insert 쿼리문이 정상적으로 실행되지 않았을 경우(데이터 저장에 실패했을 경우) null값 반환
			return null;
		} finally {
			// 메모리 확보를 위해 사용한 자원 닫아주기!
			JdbcUtil.close(rs);
			JdbcUtil.close(stmt);
			JdbcUtil.close(pstmt);
		}
	}
	
	// Date 객체를 Timestamp 객체로 바꿔주는 메서드!
	private Timestamp toTimestamp(Date date) {
		return new Timestamp(date.getTime());
	}
	
	// 게시글 전체 수를 반환하는 쿼리문
	public int selectCount(Connection conn) throws SQLException {
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			// 게시글 전체 수를 반환하는 쿼리문
			rs = stmt.executeQuery("select count(*) from article");
			if(rs.next()) {
				return rs.getInt(1);
			}
			// 게시글을 못 불러왔을 경우(혹은 게시글이 없을 경우?) 0을 반환
			return 0;
		} finally {
			// 메모리 확보를 위해 사용한 자원 닫아주기!
			JdbcUtil.close(rs);
			JdbcUtil.close(stmt);
		}
	}
	
	// 지정한 범위의 전체 게시글을 불러들이는 쿼리문
	public List<Article> select(Connection conn, int startRow, int size) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			// 지정한 범위의 전체 게시글을 불러들이는 쿼리문(ROWNUM 활용!)
			pstmt=conn.prepareStatement("select * from "
					+ "(select ROWNUM rnum,article_no,writer_id,"
					+ "writer_name,title,regdate,moddate,read_cnt from "
					+ "(SELECT * from article ORDER by article_no desc) "
					+ "where rownum<=?) where rnum>=?");
			// 시작점은 입력 받은 지점!, 끝나는 부분은 시작점에서 size 만큼 더한 게시글 번호까지!
			pstmt.setInt(1, startRow + size);
			pstmt.setInt(2, startRow);
			rs = pstmt.executeQuery();
			// 지정한 범위 내의 게시글 정보 전부를 담을 ArrayList 객체 생성
			List<Article> result = new ArrayList<>();
			
			while(rs.next()) {
				//ResultSet 의 내용을 Article로 변환하여 ArrayList에 추가!
				result.add(convertArticle(rs));
			}
			// 게시글을 모아둔 List 반환
			return result;
		} finally {
			// 메모리 확보를 위해 사용한 자원 닫아주기!
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// ResultSet 의 내용을 Article로 변환하는 메서드
	private Article convertArticle(ResultSet rs) throws SQLException {
		return new Article(rs.getInt("article_no"), new Writer(rs.getString("writer_id"), rs.getString("writer_name")),
				rs.getString("title"), toDate(rs.getTimestamp("regdate")), toDate(rs.getTimestamp("moddate")), rs.getInt("read_cnt"));
	}
	// Timestamp를 Date로 변환하는 메서드
	private Date toDate(Timestamp timestamp) {
		return new Date(timestamp.getTime());
	}
	
	// 게시글 번호로 특정 게시글 정보를 불러오는 메서드
	public Article selectById(Connection conn, int no) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			// 게시글 번호로 특정 게시글 정보를 불러오는 쿼리문
			pstmt = conn.prepareStatement("select * from article where article_no = ?");
			// 입력받은 게시글 번호를 쿼리문에 세팅
			pstmt.setInt(1, no);
			// ResultSet에 받아온 게시글 정보 저장
			rs = pstmt.executeQuery();
			Article article = null;
			if(rs.next()) {
				//116 행에서 만든 메서드 재활용!
				article = convertArticle(rs);
			}
			return article;
		} finally {
			// 메모리 확보를 위해 사용한 자원 닫아주기!
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}
	
	// 게시글의 조회수를 1 증가시키는 메서드
	public void increaseReadCount(Connection conn, int no) throws SQLException {
		// 게시글 번호로 특정 게시글 정보를 불러와 조회수를 1 증가시키는 쿼리문
		try(PreparedStatement pstmt = conn.prepareStatement("update article set read_cnt = read_cnt + 1 where article_no = ?")) {
			pstmt.setInt(1, no);
			pstmt.executeUpdate();
		}
	}
	
	// 게시글 번호로 게시글 정보를 불러와 게시글의 제목을 수정하는 메서드
	public int update(Connection conn, int no, String title) throws SQLException {
		// 게시글 번호로 게시글 정보를 불러와 게시글의 제목을 수정하는 쿼리문
		try(PreparedStatement pstmt = conn.prepareStatement("update article set title = ?, moddate = systimestamp  where article_no = ?")) {
			pstmt.setString(1, title);
			pstmt.setInt(2, no);
			return pstmt.executeUpdate();
		}
	}
}
