package article.service;

import java.util.List;

import article.model.Article;

public class ArticlePage {
	
	// 멤버 필드
	private int total;
	private int currentPage;
	private List<Article> content;
	private int totalPages;
	private int startPage;
	private int endPage;
	
	// 생성자
	public ArticlePage(int total, int currentPage, int size, List<Article> content) {
		this.total = total;
		this.currentPage = currentPage;
		this.content = content;
		// 얻어온 total 값이 0인 경우, 페이지 관련 변수를 전부 0으로 초기화
		if(total == 0) {
			totalPages = 0;
			startPage = 0;
			endPage = 0;
		} else {
			// 총 페이지 수는 모든 게시글 수를 한 페이지에 보여줄 게시글 수로 나눈 값!
			totalPages = total / size;
			if(total % size > 0) {// 전체 게시글 수가 나누어 떨어지지 않을 때는 한 페이지를 추가!
				totalPages++;
			}
			// 페이지 목록에 항상 5개의 페이지만 표시되도록 startPage와 endPage 계산!
			int modVal = currentPage % 5;
			startPage = currentPage / 5 * 5 + 1;
			// 현재 페이지가 5의 배수일 경우, 다음페이지로 목록으로 넘어가면 안됨! - 시작페이지에서 5를 빼준다
			if(modVal == 0) startPage -= 5;
			
			endPage = startPage + 4;
			// 마지막 페이지를 계산했을 때 총 페이지 수 보다 커질 경우, 마지막 페이지를 총 페이지 수로 설정!
			if(endPage > totalPages) endPage = totalPages;
		}
	}
	
	// Getter 메서드
	public int getTotal() {
		return total;
	}
	
	// 게시글 수가 0일 때 True를 반환하는 메서드
	public boolean hasNoArticles() {
		return total == 0;
	}
	
	// 게시글 수가 0보다 클 때 True를 반환하는 메서드
	public boolean hasArticles() {
		return total > 0;
	}
	public void setTotal(int total) {
		this.total = total;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public List<Article> getContent() {
		return content;
	}

	public void setContent(List<Article> content) {
		this.content = content;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getStartPage() {
		return startPage;
	}

	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}

	public int getEndPage() {
		return endPage;
	}

	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}
	
	
	
}
