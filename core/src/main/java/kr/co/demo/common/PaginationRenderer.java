package kr.co.demo.common;

import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class PaginationRenderer {

	private String firstPageLabel;
	private String previousPageLabel;
	private String currentPageLabel;
	private String firstCurrentPageLabel;
	private String otherPageLabel;
	private String nextPageLabel;
	private String lastPageLabel;

	public PaginationRenderer() {
		firstPageLabel = "<a class=\"btn-paging first\" onclick=\"{0}({1}, this); return false;\" style=\"cursor:pointer;\">첫페이지로</a>";
		previousPageLabel = "<a class=\"btn-paging prev\" onclick=\"{0}({1}, this); return false;\" style=\"cursor:pointer;\">이전페이지로</a>";
		currentPageLabel = "<a class=\"active\">{0}</a>";
		firstCurrentPageLabel = "<a class=\"active\">{0}</a>";
		otherPageLabel = "<a onclick=\"{0}({1}, this); return false;\" style=\"cursor:pointer;\">{2}</a>";
		nextPageLabel = "<a class=\"btn-paging next\" onclick=\"{0}({1}, this); return false;\" style=\"cursor:pointer;\">다음페이지로</a>";
		lastPageLabel = "<a class=\"btn-paging last\" onclick=\"{0}({1}, this); return false;\" style=\"cursor:pointer;\">마지막페이지로</a>";
	}

	public String renderPagination(Pagination pagination, String jsFunction) {
		StringBuffer sb = new StringBuffer();

		int startPageNo = pagination.getStartPageNo();
		int endPageNo = pagination.getEndPageNo();
		int currentPageNo = pagination.getCurrentPageNo();
		int prevPageNo = pagination.getPrevPageNo();
		int nextPageNo = pagination.getNextPageNo();
        int lastPageNo = pagination.getLastPageNo();

		if(currentPageNo > prevPageNo) {
			sb.append(MessageFormat.format(firstPageLabel, new Object[]{jsFunction, Integer.toString(1)}));
			sb.append(MessageFormat.format(previousPageLabel, new Object[]{jsFunction, Integer.toString(prevPageNo)}));
		}

		for(int i=startPageNo; i<=endPageNo; i++) {
			if(i == currentPageNo) {
				if(i == 1) {
					sb.append(MessageFormat.format(firstCurrentPageLabel, new Object[]{Integer.toString(i)}));
				} else {
					sb.append(MessageFormat.format(currentPageLabel, new Object[]{Integer.toString(i)}));
				}
			} else {
				sb.append(MessageFormat.format(otherPageLabel, new Object[]{jsFunction, Integer.toString(i), Integer.toString(i)}));
			}
		}

		if(currentPageNo < nextPageNo) {
			sb.append(MessageFormat.format(nextPageLabel, new Object[]{jsFunction, Integer.toString(nextPageNo)}));
			sb.append(MessageFormat.format(lastPageLabel, new Object[]{jsFunction, Integer.toString(lastPageNo)}));
		}

		return sb.toString();
	}
}
