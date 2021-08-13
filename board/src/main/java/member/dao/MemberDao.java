package member.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import jdbc.JdbcUtil;
import member.model.Member;

public class MemberDao {
	
	// id를 입력받아 특정 유저의 정보를 가져오는 메서드
	public Member selectById(Connection conn, String id) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			// 유저의 id로 유저의 정보를 얻어는 쿼리문
			pstmt = conn.prepareStatement("select * from member where memberid = ?");
			pstmt.setString(1, id);
			// 얻어온 유저의 정보를 ResultSet에 저장
			rs = pstmt.executeQuery();
			Member member  = null;
			if(rs.next()) {
				// ResultSet에 저장된 정보들을 통해 새로운 Member 객체 생성!
				member = new Member(rs.getString("memberid"), rs.getString("name"), rs.getString("password"), toDate(rs.getTimestamp("regdate")));
			}
			return member;
		} finally {
			// 메모리 자원 확보를 위해 사용을 마친 ResultSet, PreparedStatement 닫아주기!
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
		
	}
	
	// Timestamp 타입의 데이터를 Date 타입으로 변환해주는 메서드
	private Date toDate(Timestamp date) {
		return date == null ? null : new Date(date.getTime());
	}

	// Member 객체를 받아 DB에 추가시키는 메서드
	public void insert(Connection conn, Member member) throws SQLException {
		try(PreparedStatement pstmt = conn.prepareStatement("insert into member values(?, ?, ?, ?)")) {
			pstmt.setString(1, member.getId());
			pstmt.setString(2, member.getName());
			pstmt.setString(3, member.getPassword());
			pstmt.setTimestamp(4, new Timestamp(member.getRegDate().getTime()));
			pstmt.executeUpdate();
		}
	}
}
