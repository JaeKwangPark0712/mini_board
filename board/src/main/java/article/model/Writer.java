package article.model;

public class Writer {// 작성자의 정보를 저장하는 클래스(id, 이름)
	
	//멤버 필드
	private String id;
	private String name;
	
	//생성자
	public Writer(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	//Getter 메서드
	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
