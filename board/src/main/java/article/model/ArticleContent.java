package article.model;

public class ArticleContent {// 게시글 내용 정보를 담는 클래스
	
	private Integer number;
	private String content;
	
	// 생성자
	public ArticleContent(Integer number, String content) {
		this.number = number;
		this.content = content;
		
	// Getter 메서드
	}
	public Integer getNumber() {
		return number;
	}
	public String getContent() {
		return content;
	}
	
}
