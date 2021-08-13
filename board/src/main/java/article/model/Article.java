package article.model;

import java.util.Date;

public class Article {// 게시글의 정보를 저장하는 클래스
	
	//멤버 필드
	private Integer number;
	private Writer writer;
	private String title;
	private Date regDate;
	private	Date modifiedDate;
	private int readCount;
	
	// 생성자
	public Article(Integer number, Writer writer, String title, Date regDate, Date modifiedDate, int readCount) {
		this.number = number;
		this.writer = writer;
		this.title = title;
		this.regDate = regDate;
		this.modifiedDate = modifiedDate;
		this.readCount = readCount;
	}
	
	// Getter 메서드
	public Integer getNumber() {
		return number;
	}

	public Writer getWriter() {
		return writer;
	}

	public String getTitle() {
		return title;
	}

	public Date getRegDate() {
		return regDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public int getReadCount() {
		return readCount;
	}
	
	
	
}
