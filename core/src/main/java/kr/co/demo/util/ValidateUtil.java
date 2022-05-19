package kr.co.demo.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * 유효성 검사 유틸 Class
 * ##### 수정시 주의! : validation.js와 정규식을 동일하게 맞출 것 #####
 */
public class ValidateUtil {

    /** 사용자 아이디 정규식 */
    // 영문 소문자, 숫자를 포함하는 6~12자 길이의 문자열
    private static final String MEMBER_ID_PATTERN = "^((?=.*[a-z])(?=.*\\d)).{6,12}$";
    /** 사용자 비밀번호 정규식 */
    // 영문 대/소문자, 숫자 혹은 특수문자를 포함하는 8~20자 길이의 문자열
    private static final String MEMBER_PW_PATTERN = "^(?=.*[a-zA-Z])((?=.*\\d)|(?=.*\\W)).{8,20}$";
    /** 이메일 정규식 */
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    /** 일반전화 번호 정규식 */
    private static final String TEL_NUM_PATTERN = "^\\d{2,3}-\\d{3,4}-\\d{4}$";
    /** 휴대폰 번호 정규식 */
    private static final String PHONE_NUM_PATTERN = "(01[016789])-(\\d{3,4})-(\\d{4})";
    /** 우편번호(5자리) 정규식 */
    private static final String ZIPCODE_PATTERN = "^\\d{5}$";
    /** YN 정규식 */
    private static final String YN_PATTERN = "^(Y|N){1}$";
    /** 날짜(YYYY-MM-DD) 정규식 */
    private static final String DATE_STR_PATTERN = "^(19|20)\\d{2}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[0-1])$";

    /**
     * 사용자 아이디 포맷 Validator
     * @param str
     * @return
     */
    public static boolean isMemberId(String str) {
        return isValid(MEMBER_ID_PATTERN, str);
    }

    /**
     * 사용자 비밀번호 포맷 Validator
     * @param str
     * @return
     */
    public static boolean isMemberPw(String str) {
        return isValid(MEMBER_PW_PATTERN, str);
    }

    /**
     * 이메일 포맷 Validator
     * @param str
     * @return
     */
    public static boolean isEmail(String str) {
        return isValid(EMAIL_PATTERN, str);
    }

    /**
     * 일반전화 번호 포맷 Validator
     * @param str
     * @return
     */
    public static boolean isTel(String str) {
        return isValid(TEL_NUM_PATTERN, str);
    }

    /**
     * 휴대폰 번호 포맷 Validator
     * @param str
     * @return
     */
    public static boolean isPhone(String str) {
        return isValid(PHONE_NUM_PATTERN, str);
    }

    /**
     * 우편번호(5자리) 포맷 Validator
     * @param str
     * @return
     */
    public static boolean isZipCode(String str) {
        return isValid(ZIPCODE_PATTERN, str);
    }

    /**
     * Y/N 포맷 Validator
     * @param str
     * @return
     */
    public static boolean isYn(String str) {
        return isValid(YN_PATTERN, str);
    }

    /**
     * 날짜(YYYY-MM-DD) 포맷 Validator
     * @param str
     * @return
     */
    public static boolean isDateStr(String str) {
        return isValid(DATE_STR_PATTERN, str);
    }

    /**
     * 문자열이 정규식에 맞는 포맷인지 체크
     * @param regex
     * @param target
     * @return isValid
     */
    private static boolean isValid(String regex, String target) {
        return Pattern.compile(regex).matcher(StringUtils.defaultIfEmpty(target,"")).matches();
    }

}
