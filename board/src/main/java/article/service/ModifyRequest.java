package article.service;

import java.util.Map;

public class ModifyRequest {
	
	// 멤버 필드
	private String userId;
	private int articleNumber;
	private String title;
	private String content;
	
	// 생성자
	public ModifyRequest(String userId, int articleNumber, String title, String content) {
		this.userId = userId;
		this.articleNumber = articleNumber;
		this.title = title;
		this.content = content;
	}
	
	// Getter 메서드
	public String getUserId() {
		return userId;
	}

	public int getArticleNumber() {
		return articleNumber;
	}

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}
	
	// 에러 발생 여부를 확인하는 validate() 메서드! - 제목 칸이 비어있을 경우 errors 맵에 에러 이름과 true 값을 저장한다!
	public void validate(Map<String, Boolean> errors) {
		if(title == null || title.trim().isEmpty()) {
			errors.put("title", Boolean.TRUE);
		}
	}
}
