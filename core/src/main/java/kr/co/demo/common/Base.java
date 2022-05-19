package kr.co.demo.common;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Map;

/**
 * 기본 Base Domain
 * (특별한 경우가 아니면 모든 Domain 객체는 이 객체를 extends 할 것)
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class Base {

    /** row Number  */
    private String rowNum;

    /** 총갯수  */
    private int total_cnt;

    /** 현재페이지 */
    private int pageNo = 1;

    /** 페이징 SQL의 조건절에 사용되는 시작 rownum */
    private int fromNo;

    /** 페이징 SQL의 조건절에 사용되는 가져올 데이터 개수 */
    private int dataPerPage = 10;

    /** 서브 정렬 순서 - 컬렴명 */
    private String subOrderKey;

    /** 서브 정렬 순서 - ASC, DESC */
    private String subOrderValue;

    /** 정렬 순서 - 컬렴명 */
    private String orderKey;

    /** 정렬 순서 - ASC, DESC */
    private String orderValue;

    /** 페이징 여부[true=페이징안함|false=페이징사용] */
    private boolean skipPaging = false;

    /** 검색조건 및 Keyword : mybatis 전달용 */
    private Map<String, Object> searchInfo;

    /** 검색조건 및 Keyword : mybatis 전달용(한 페이지에 두개 필요할 경우 사용) */
    private Map<String, Object> searchInfo2;

    private String[] values;

    /** 검색 내용 */
    private String searchValue;

    /** 검색 내용 2 (한 페이지에 두개 필요할 경우 사용)*/
    private String searchValue2;

    /** 검색 내용(날짜 앞에꺼) */
    private String searchDate1;

    /** 검색 내용(날짜 뒤에꺼) */
    private String searchDate2;

    /** group by - 컬럼명*/
    private String groupByKey;

    /** CurrentInterceptor에서 주입하는 변수값 */
    private Map<String, Object> current;

    /** admin/pc/mobile 비교- 컬럼명*/
    private String module;

    /**
     * 체의 값을 문자열로 변환하여 반환 해주는 메소드로써 직접 구현할려면 노가다성 코드
     * Commons의 ToStringBuilder를 사용하면 는 말 그대로 클래스의 toString() 원하는 스타일로 지정가능
     * */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
