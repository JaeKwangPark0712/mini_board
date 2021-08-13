package article.model;

public class ArticleContent {// 게시글의 작성내용을 저장하는 클래스
	
	//멤버 필드
	private Integer number; // Article의 number 필드와 같은 값을 가진다!
	private String content;
	
	//생성자
	public ArticleContent(Integer number, String content) {
		this.number = number;
		this.content = content;
	}
	
	//Getter 메서드
	public Integer getNumber() {
		return number;
	}

	public String getContent() {
		return content;
	}
	
	
	
	
}	
