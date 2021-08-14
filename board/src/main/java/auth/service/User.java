package auth.service;

public class User {// 로그인 구현에 필요한 자바빈 클래스
	
	// 멤버 필드
	private String id;
	private String name;
	
	// 생성자
	public User(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	// Getter 메서드
	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	
}
