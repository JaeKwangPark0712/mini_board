package article.service;

import article.model.Article;
import article.model.ArticleContent;

public class ArticleData {// 게시글 내용 조회에 필요한 데이터를 담는 클래스 작성

	// 멤버 필드
	private Article article;
	private ArticleContent content;
	
	// 생성자
	public ArticleData(Article aritcle, ArticleContent content) {
		super();
		this.article = aritcle;
		this.content = content;
	}
	
	// Getter 메서드
	public Article getArticle() {
		return article;
	}
	// ArticleData 객체 안의 ArticleContent 객체 속성에서 바로 게시글의 내용 데이터를 가져오는 메서드
	public String getContent() {
		return content.getContent();
	}
	
	
}
