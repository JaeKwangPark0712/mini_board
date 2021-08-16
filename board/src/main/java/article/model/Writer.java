package article.model;

public class Writer {// 작성자 정보를 담는 자바빈 클래스
	
	// 멤버 필드
	private String id;
	private String name;
	
	// 생성자
	public Writer(String id, String name) {
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
