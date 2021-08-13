package article.service;

import article.model.Article;
import article.model.ArticleContent;

public class ArticleData {
	
	// 멤버 필드 : 게시글의 정보와 게시글의 내용을 담고 있는 객체를 멤버로 설정함
	private Article article;
	private ArticleContent content;
	
	// 생성자
	public ArticleData(Article article, ArticleContent content) {
		super();
		this.article = article;
		this.content = content;
	}
	
	// Getter 메서드
	public Article getArticle() {
		return article;
	}
	// ArticleContent 객체의 content 내용을 얻어오기 위한 메서드
	public String getContent() {
		return content.getContent();
	}
	
	
}
