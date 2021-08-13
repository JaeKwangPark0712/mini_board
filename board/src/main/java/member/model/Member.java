package member.model;

import java.util.Date;

public class Member {// 회원의 데이터를 담는 자바빈 클래스
	
	// 멤버 필드
	private String id;
	private String name;
	private String password;
	private Date regDate;
	
	// 생성자
	public Member(String id, String name, String password, Date regDate) {
		super();
		this.id = id;
		this.name = name;
		this.password = password;
		this.regDate = regDate;
	}
	
	// Getter 메서드
	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}

	public Date getRegDate() {
		return regDate;
	}
	
	// 입력받은 암호와 저장된 암호가 일치하는지 확인하는 메서드(로그인 기능 구현할 때 사용)
	public boolean matchPassword(String pwd) {
		return password.equals(pwd);
	}
}
