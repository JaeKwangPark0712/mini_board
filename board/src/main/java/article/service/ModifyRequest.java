package article.service;

import java.util.Map;

public class ModifyRequest {// 게시글 수정에 필요한 정보를 담는 클래스
	
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
	
	//오류 발생시 맵 객체에 에러코드를 넣어주는 매서드
	public void validate(Map<String, Boolean> errors) {
		if(title == null || title.trim().isEmpty()) {
			errors.put("title", Boolean.TRUE);
		}
	}
	

}
