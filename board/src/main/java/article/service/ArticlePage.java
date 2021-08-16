package article.service;

import java.util.List;

import article.model.Article;

public class ArticlePage {// 시작페이지, 마지막페이지, 총 페이지 수를 계산하기 위한 클래스
	
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
		// 총 게시글 수가 0일 경우, 모든 페이지 값을 0으로 초기화
		if(total == 0) {
			totalPages = 0;
			startPage = 0;
			endPage = 0;
		} else {
			// 전체 페이지 = 전체 게시글 수 / 한 페이지에 보여줄 게시글 수
			totalPages = total / size;
			if(total % size > 0) {
				// 나누어 떨어지지 않을 경우 페이지 수 1 추가!
				totalPages++;
			}
			// 한 번에 5개의 페이지 리스트를 보여준다고 할 때 현재 페이지와 마지막 페이지 계산
			int modVal = currentPage % 5;
			// 시작 페이지 = 현재 페이지 / 5 * 5 + 1;
			startPage = currentPage / 5 * 5 + 1;
			// 현재 페이지 수가 5로 나누어 떨어질 경우, 계산 한 첫 페이지에서 5 빼주기(다음 페이지로 넘어가지 않게 하기 위해서)
			if(modVal == 0) { startPage -= 5; }
			
			// 마지막 페이지 = 시작 페이지 + 4
			endPage = startPage + 4;
			// 마지막 페이지가 전체 페이지보다 클 경우, 전체 페이지 수를 마지막 페이지로 지정
			if(endPage > totalPages) { endPage = totalPages; }
		}
	}
	
	// Getter 메서드
	public int getTotal() {
		return total;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public List<Article> getContent() {
		return content;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public int getStartPage() {
		return startPage;
	}

	public int getEndPage() {
		return endPage;
	}
	
	// 게시글이 하나도 없는지 확인하는 메서드
	public boolean hasNoArticles() {
		return total == 0;
	}
	
	// 게시글이 하나 이상 있는지 확인하는 메서드
	public boolean hasArticles() {
		return total > 0;
	}
}
