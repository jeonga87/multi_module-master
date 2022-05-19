package kr.co.demo.example.domain;

import kr.co.demo.attach.domain.AttachBag;
import kr.co.demo.common.Base;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.Date;

@Alias("example")
@Data
@EqualsAndHashCode(callSuper=false)
public class Example extends Base implements Serializable {

	/** 예제 게시판 일련번호*/
	private Long idx;

	/** 제목 */
	private String title;

	/** 내용 */
	private String content;

	/** 사용여부 */
	private String useYn;

	/** 삭제여부 */
	private String delYn;

	/** 생성자 */
	private String createdBy;

	/** 등록일 */
	private Date createdDt;

	/** 수정자 */
	private String modifiedBy;

	/** 수정일 */
	private Date modifiedDt;

	/** 첨부파일 key */
	private Long attachIdx;

	/** 첨부파일 name */
	private String attachName;

	/** 첨부파일 */
	private AttachBag attachBag;

}
