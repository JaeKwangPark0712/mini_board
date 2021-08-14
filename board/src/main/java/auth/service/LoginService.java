package auth.service;

import java.sql.Connection;
import java.sql.SQLException;

import jdbc.connection.ConnectionProvider;
import member.dao.MemberDao;
import member.model.Member;

public class LoginService {
	
	// service 클래스에서 사용할 Dao 객체 생성!
	private MemberDao memberDao = new MemberDao();
	
	// LoginService의 메인 메서드
	public User login(String id, String password) {
		try(Connection connection = ConnectionProvider.getConnection()) {
			// Dao 객체의 selectById 메서드를 이용해서 기존에 있던 member 객체를 불러옴!
			Member member = memberDao.selectById(connection, id);
			if(member == null) {
				// 로그인 하려는 유저의 정보(id)가 존재하지 않을경우, 예외 발생!
				throw new LoginFailException();
			}
			if(!member.matchPassword(password)) {
				// 로그인 하려는 유저의 비밀번호가 일치하지 않을경우, 예외 발생!
				throw new LoginFailException();
			}
			// 로그인에 성공했다면, 로그인 한 유저의 정보를 담은 User 객체 반환!
			return new User(member.getId(), member.getName());
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
