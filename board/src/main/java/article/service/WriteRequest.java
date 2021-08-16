package article.service;

import java.util.Map;

import article.model.Writer;

public class WriteRequest {// 게시글 작성에 필요한 정보를 담는 자바빈 클래스 작성
	
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
	
	// 제목칸이 비어있을 경우 오류 코드를 Map 객체에 담는 메서드
	public void validate(Map<String, Boolean> errors) {
		if(title == null || title.trim().isEmpty()) {
			errors.put("title", Boolean.TRUE);
		}
	}
}
