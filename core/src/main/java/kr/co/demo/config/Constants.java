package kr.co.demo.config;

public class Constants {

  /**
   * Spring Profile 관련 상수 Class
   */
  public final class SpringProfileConst {

      /** 로컬 */
      public static final String LOCAL = "local";

      /** 개발 */
      public static final String DEVELOPMENT = "dev";

      /** 운영 */
      public static final String PRODUCTION = "prod";

  }

  /**
   * Spring Security Role 관련 상수 Class
   * (홈페이지에서 회원/비회원 구분용)
   */
  public final class AuthoritiesConsf {
    public static final String ADMIN = "ROLE_ADMIN";

    public static final String MEMBER = "ROLE_MEMBER";

    public static final String NON_MEMBER = "ROLE_NON_MEMBER";
  }

  /**
   * Paging 관련 상수 Class
   */
  public final class PaginationConst {

    public static final String SKIP_PAGING = "SKIP_PAGING";

    public static final String CONDITION_PARAM_KEY = "q.";

    public static final String CURRENT_PAGE = "currentPage";

    public static final String DATA_PER_PAGE = "dataPerPage";

    public static final String LINK_PER_PAGE = "linkPerPage";

    public static final String ORDER_KEY = "orderKey";

    public static final String ORDER_VALUE = "orderValue";

    public static final int DEFAULT_DATA_PER_PAGE = 10;

    public static final int DEFAULT_PAGE_LINK_COUNT = 10;

  }


  /**
   * Excel 관련 상수 Class
   */
  public final class ExcelConst {

    /** 다운로드시 엑셀 컨텐츠 타입 헤더값 */
    public static final String CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    /** 다운로드시 엑셀 파일명(확장자 제외) */
    public static final String FILE_NAME = "fileName";

    /** ExcelView로 넘겨줄 데이터 Model 이름 */
    public static final String EXCEL_DATA = "excelData";

    /** 컬럼 헤더 배경색 값 */
    public static final String HEADER_BG_COLOR = "bgColor";

    /** 컬럼 헤더 및 데이터 정보 */
    public static final String HEADER_LIST = "headerList";

    /** 데이터 목록 */
    public static final String ROW_LIST = "rowList";

  }

  private Constants() {
  }
}

