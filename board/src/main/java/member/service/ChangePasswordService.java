package member.service;

import java.sql.Connection;
import java.sql.SQLException;

import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;
import member.dao.MemberDao;
import member.model.Member;

public class ChangePasswordService {

	// Service 클래스에서 사용할 DAO 객체 생성
	private MemberDao memberDao = new MemberDao();
	
	// Service 클래스의 주요 메서드
	public void changePassword(String userId, String currPwd, String newPwd) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			// 트랜젝션 처리를 위해 AutoCommit 비활성화
			conn.setAutoCommit(false);
			
			// MemberDao 클래스의 selectById 메서드를 이용해 member 객체를 얻어옴
			Member member = memberDao.selectById(conn, userId);
			if(member == null) {
				// 비밀번호를 변경하려고 하는 유저의 정보를 찾지 못했을 경우 예외 발생!
				throw new MemberNotFoundException();
			}
			if(!member.matchPassword(currPwd)) {
				// 현재 비밀번호와 입력한 비밀번호가 일치하지 않을 경우 예외 발생!
				throw new InvalidPasswordException();
			}
			// 비밀번호를 변경하려는 member를 불러오는데에 성공했다면 새로운 비밀번호로 변경
			member.changePassword(newPwd);
			// 새로운 비밀번호를 가진 member 객체의 정보를 DB에 업데이트!
			memberDao.update(conn, member);
			// 변경사항을 DB에 적용시키기 위해 commit 실행
			conn.commit();
		} catch(SQLException e) {
			// 예외가 발생했을 경우 rollback처리!
			JdbcUtil.rollback(conn);
			throw new RuntimeException(e);
		} finally {
			// 메모리 확보를 위해 사용한 자원 닫아주기!
			JdbcUtil.close(conn);
		}
		
	}
}
