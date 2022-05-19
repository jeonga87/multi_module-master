package kr.co.demo.attach.domain;

import kr.co.demo.common.Base;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.Date;


@Alias("attach")
@Data
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Attach extends Base implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 첨부파일 아이디(시퀀스) */
    private Long idx;

    /** 참조타입(참조테이블) */
    private String refType;

    /** 참조키(참조테이블의 키값) */
    private String refKey;

    /** 참조 상세 구분 코드 */
    private String refMapCode;

    /** 파일 순서 */
    private Integer order;

    /** 원본 파일 명 */
    private String displayName;

    /** 저장된 파일명 */
    private String savedName;

    /** 저장된 파일 디렉토리 */
    private String savedDir;

    /** 파일 설명 (이미지 ALT 값) */
    private String altValue;

    /** 파일타입(확장자) */
    private String fileType;

    /** 파일 사이즈 */
    private Long fileSize;

    /** 생성자 */
    private String createdBy;

    /** 등록일 */
    private Date createdDt;

    /** 아이피 */
    private String ip;


    private int positionIdx = 0;

}
