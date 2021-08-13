package article.service;

public class DeleteRequest {// 글 삭제에 필요한 데이터들을 담는 request 클래스!
	
	// 멤버 필드
	private String userId;
	private int articleNumber;
	
	// 생성자
	public DeleteRequest(String userId, int articleNumber) {
		super();
		this.userId = userId;
		this.articleNumber = articleNumber;
	}
	
	// Getter 메서드
	public String getUserId() {
		return userId;
	}

	public int getArticleNumber() {
		return articleNumber;
	}
	
	
}
