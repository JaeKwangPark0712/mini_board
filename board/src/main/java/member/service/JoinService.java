package member.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;
import member.dao.MemberDao;
import member.model.Member;

public class JoinService {
	
	// Service 클래스에서 사용할 Dao 객체 생성!
	private MemberDao memberDao = new MemberDao();
	
	// JoinService 클래스의 메인 메서드
	public void join(JoinRequest joinReq) {
		Connection conn  = null;
		try {
			// 커넥션 풀에서 커넥션을 얻어옴
			conn = ConnectionProvider.getConnection();
			// 트랜젝션 처리를 위해 AutoCommit 비활성화
			conn.setAutoCommit(false);
			
			// MemberDao 객체의 selectById() 메서드를 통해 새로운 Member 객체를 얻어옴
			Member member = memberDao.selectById(conn, joinReq.getId());
			// 가입하려고 하는 id가 이미 존재할 경우 변경사항을 rollback 시키고 익셉션을 발생시킴
			if(member != null) {
				JdbcUtil.rollback(conn);
				throw new DuplicateIdException();
			}
			
			// 가입하려고 하는 id가 없으면 MemberDao 객체의 insert() 메서드를 이용해서 DB에 새로운 유저의 정보를 저장함
			memberDao.insert(conn, new Member(member.getId(), member.getName(), member.getPassword(), new Date()));
			// DB에 정보 저장을 성공했을 경우 commit시킴!
			conn.commit();
		} catch (SQLException e) {
			// 예외가 발생했을 경우 변경 사항을 rollback 시킴
			JdbcUtil.rollback(conn);
		} finally {
			// 사용한 Connection을 닫아줌
			JdbcUtil.close(conn);
		}
	}
}
