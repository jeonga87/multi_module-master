package kr.co.demo.controller;

import kr.co.demo.common.Base;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Class Name : BaseFormController.java
 * Description : 기본 컨트롤러 클래스
 * Modification Information
 *
 * @author jeonga
 * @since 2019. 10. 28
 * @version 1.0
 *
 * Generated : Source Builder
 */

public class BaseFormController {
	public void setRowNums(List list, int totalCount, int indexNum ) {
		if (list == null) return;

		int rowNum = totalCount - indexNum;
		for (int i = 0; i < list.size(); i++) {
			Base base = (Base) list.get(i);
			base.setRowNum(Integer.toString(rowNum - i));
		}
	}

	/**
	 * 입력받은 성공여부에 따라 Status가 다른 ResponseEntity 생성
	 * @param isSuccess 성공여부
	 * @param headers 성공시 포함될 http header
	 * @param body 성공시 포함될 http body
	 * @return
	 */
	public static ResponseEntity createResponseEntity(boolean isSuccess, HttpHeaders headers, Object body) {
		if(isSuccess == false) {
			headers = null;
			body = null;
		}
		return ResponseEntity.status(isSuccess ? HttpStatus.OK : HttpStatus.BAD_REQUEST).headers(headers).body(body);
	}

	public static ResponseEntity createResponseEntity(boolean isSuccess) {
		return createResponseEntity(isSuccess, null, null);
	}

	public static ResponseEntity createResponseEntity(boolean isSuccess, Object body) {
		return createResponseEntity(isSuccess, null, body);
	}

	public static ResponseEntity createResponseEntity(boolean isSuccess, HttpHeaders headers) {
		return createResponseEntity(isSuccess, headers, null);
	}

}
