package kr.co.demo.common;

import lombok.Getter;

@Getter
public class Pagination {
    /**
     * currentPageNo : 현재 페이지 번호
     * pageSize : 페이지 리스트에 게시되는 페이지 건 수
     * dataPerPage : 한 페이지당 게시되는 데이터 건 수
     * totalCount : 전체 데이터 건 수
     *
     * firstPageNo : 첫 번째 페이지 번호
     * lastPageNo : 마지막 페이지 번호
     *
     * prevPageNo : 이전 페이지 번호
     * nextPageNo : 다음 페이지 번호
     *
     * startPageNo : 시작 페이지 (페이징 네비 기준)
     * endPageNo : 끝 페이지 (페이징 네비 기준)
     *
     * fromNo : 페이징 SQL의 조건절에 사용되는 시작 rownum
     * toNo : 페이징 SQL의 조건절에 사용되는 마지막 rownum
     */
    private int currentPageNo;
    private int pageSize;
    private int dataPerPage;
    private int totalCount;

    private int firstPageNo;
    private int lastPageNo;

    private int prevPageNo;
    private int nextPageNo;

    private int startPageNo;
    private int endPageNo;

    private int fromNo;
    private int toNo;

    private Pagination() {
    }

    public Pagination(int currentPageNo, int totalCount) {
        this(currentPageNo, 10, 10, totalCount);
    }

    public Pagination(int currentPageNo, int dataPerPage, int totalCount) {
        this(currentPageNo, 10, dataPerPage, totalCount);
    }

    public Pagination(int currentPageNo, int pageSize, int dataPerPage, int totalCount) {
        this.currentPageNo = currentPageNo;
        this.pageSize = pageSize;
        this.dataPerPage = dataPerPage;
        this.totalCount = totalCount;

        build();
    }

    private void build() {
        int finalPage = (totalCount + (dataPerPage - 1)) / dataPerPage; // 마지막 페이지
        if (currentPageNo > finalPage) currentPageNo = finalPage; // 기본 값 설정

        if (currentPageNo <= 0) currentPageNo = 1; // 현재 페이지 유효성 체크

        boolean isFirst = currentPageNo == 1 ? true : false; // 시작 페이지 (전체)
        boolean isFinal = currentPageNo == finalPage ? true : false; // 마지막 페이지 (전체)

        int startPage = ((currentPageNo - 1) / pageSize) * pageSize + 1; // 시작 페이지 (페이징 네비 기준)
        int endPage = startPage + pageSize - 1; // 끝 페이지 (페이징 네비 기준)

        if (endPage > finalPage) { // [마지막 페이지 (페이징 네비 기준) > 마지막 페이지] 보다 큰 경우
            endPage = finalPage;
        }

        firstPageNo = 1; // 첫 번째 페이지 번호

        if (isFirst) {
            prevPageNo = 1; // 이전 페이지 번호
        } else {
            prevPageNo = ((currentPageNo - 1) < 1 ? 1 : (currentPageNo - 1)); // 이전 페이지 번호
        }

        startPageNo = startPage; // 시작 페이지 (페이징 네비 기준)
        endPageNo = endPage; // 끝 페이지 (페이징 네비 기준)

        if (isFinal) {
            nextPageNo = finalPage; // 다음 페이지 번호
        } else {
            nextPageNo = ((currentPageNo + 1) > finalPage ? finalPage : (currentPageNo + 1)); // 다음 페이지 번호
        }

        lastPageNo = finalPage; // 마지막 페이지 번호

        fromNo = (dataPerPage * (currentPageNo - 1));
        toNo = fromNo + dataPerPage;
    }

}
