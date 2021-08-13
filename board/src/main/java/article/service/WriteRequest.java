package article.service;

import java.util.Map;

import article.model.Writer;

public class WriteRequest {// 게시글 작정에 필요한 정보를 전달할 때 사용하는 메서드
	
	// 멤버 필드
	private Writer writer;
	private String title;
	private String content;
	
	// 생성자
	public WriteRequest(Writer writer, String title, String content) {
		this.writer = writer;
		this.title = title;
		this.content = content;
	}
	
	// Getter 메서드
	public Writer getWriter() {
		return writer;
	}

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}
	
	// validate() : 게시글 작성 폼의 제목칸이 비어있을 때 'errors' Map에 'title error' 키를 넣어주는 메서드
	public void validate(Map<String, Boolean> errors) {
		if(title == null || title.trim().isEmpty()) {
			errors.put("title", Boolean.TRUE);
		}
	}
}
