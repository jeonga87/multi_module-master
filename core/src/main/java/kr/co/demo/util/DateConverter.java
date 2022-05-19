package kr.co.demo.util;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateConverter {

	/**
	 * 입력된 날짜를 yyyy-MM-dd 형식으로 리턴한다.
	 * @param dateStr
	 * @return String
	 */
	public static String toDate(String dateStr){
		if(dateStr == null)
			return null;
		dateStr = dateStr.trim();
		int length = dateStr.length();
		switch(length){
		case 8:
			return dateStr.substring(0, 4) + "-" + dateStr.substring(4, 6) + "-" + dateStr.substring(6, 8);
		case 6:
			return dateStr.substring(0, 4) + "-" + dateStr.substring(4, 6);
		case 4:
			return dateStr.substring(0, 2) + "-" + dateStr.substring(2);
		default:
			return dateStr;
		}
	}


	/**
	 * 입력된 날짜를 yyyy.MM.dd 형식으로 리턴한다.
	 * @param dateStr
	 * @return String
	 */
	public static String toDateDot(String dateStr){
		if(dateStr == null)
			return null;
		dateStr = dateStr.trim();
		int length = dateStr.length();
		switch(length){
		case 8:
			return dateStr.substring(0, 4) + "." + dateStr.substring(4, 6) + "." + dateStr.substring(6, 8);
		case 6:
			return dateStr.substring(0, 4) + "." + dateStr.substring(4, 6);
		case 4:
			return dateStr.substring(0, 2) + "." + dateStr.substring(2);
		default:
			return dateStr;
		}
	}

	/**
	 * yyyyMMdd 혹은 yyyy.MM.dd를 이용해 요일 구하기 (월,화,수...)
	 * @param dateStr
	 * @return String
	 */
	public static String toDateDayOfWeek(String dateStr){
		if(dateStr == null)
			return null;
		dateStr = dateStr.trim().replace(".","");
		Integer year = Integer.parseInt(dateStr.substring(0,4));
		Integer month = Integer.parseInt(dateStr.substring(4,6))-1;
		Integer date = Integer.parseInt(dateStr.substring(6,8));

		Calendar cal = Calendar.getInstance( );
		cal.set(year, month, date);

		final String[] week = { "일", "월", "화", "수", "목", "금", "토" };

		return week[cal.get(Calendar.DAY_OF_WEEK) - 1];
	}

	/**
	 * 입력된 날짜를 yyyy.MM.dd, HH:mm:ss, yyyy.MM.dd HH:mm:ss, yyyy.MM.dd HH:mm 형식으로 적절하게 리턴한다.
	 * @param dateStr
	 * @return String
	 */
	public static String toDateTime(String dateStr){
		if(dateStr == null)
			return null;
		dateStr = dateStr.trim();
		int length = dateStr.length();
		StringBuilder sb = new StringBuilder();
		switch(length){
		case 14:
			//년월일 시분초
			sb.append(toDateTime(dateStr.substring(0, 8)));
			sb.append(" ");
			sb.append(toDateTime(dateStr.substring(8, 14)));
			return sb.toString();
		case 12:
			//년월일 시분
			sb.append(toDateTime(dateStr.substring(0, 8)));
			sb.append(" ");
			sb.append(toDateTime(dateStr.substring(8, 12)));
			return sb.toString();
		case 8:
			//년월일
			return dateStr.substring(0, 4) + "." + dateStr.substring(4, 6) + "." + dateStr.substring(6, 8);
		case 6:
			//시분초
			return dateStr.substring(0, 2) + ":" + dateStr.substring(2, 4) + ":" + dateStr.substring(4, 6);
		case 4:
			//시분
			return dateStr.substring(0, 2) + ":" + dateStr.substring(2, 4);
		default:
			return dateStr;
		}
	}

	/**
	 * 입력된 날짜를 년월일 형식으로 리턴한다.
	 * @param dateStr
	 * @return String
	 * @throws ParseException
	 */
	public static String toDateKorean(String dateStr) {
		if (dateStr == null) return dateStr;

		StringBuilder sb = new StringBuilder();
		for (char ch : dateStr.toCharArray()) {
			if (Character.isDigit(ch)) {
				sb.append(ch);
			}
		}

		StringBuilder result = new StringBuilder();
		switch (sb.length()) {
		case 6:
			result.append(Integer.parseInt(sb.substring(2, 4))).append("월");
			result.append(" ");
			result.append(Integer.parseInt(sb.substring(4, 6))).append("일");
			break;
		case 8:
			result.append(Integer.parseInt(sb.substring(4, 6))).append("월");
			result.append(" ");
			result.append(Integer.parseInt(sb.substring(6, 8))).append("일");
			break;
		default:
			result.append(dateStr);
		}

		return result.toString();
	}

	/**
	 * 입력된 날짜를 한글 년월일 형식으로 리턴한다.
	 * @param dateStr
	 * @return String
	 */
	public static String toDateKoreanSpaced(String dateStr) {
		if (dateStr == null) return "";

		SimpleDateFormat format = new SimpleDateFormat();
		String pattern = null;

		switch (dateStr.length()) {
		case 4:
			format.applyPattern("yyyy");
			pattern = "yyyy년";
			break;
		case 6:
			format.applyPattern("yyyyMM");
			pattern = "yyyy년 MM월";
			break;
		case 7:
			dateStr = dateStr.substring(0,4) + "년 " + dateStr.substring(4,6) + "월 " + dateStr.substring(6) + "주차";
			pattern = null;
			break;
		case 8:
			format.applyPattern("yyyyMMdd");
			pattern = "yyyy년 MM월 dd일";
			break;
		case 10:
			format.applyPattern("yyyyMMddHH");
			pattern = "yyyy년 MM월 dd일 HH시";
			break;
		case 12:
			format.applyPattern("yyyyMMddHHmm");
			pattern = "yyyy년 MM월 dd일 HH시 mm분";
			break;
		case 14:
			format.applyPattern("yyyyMMddHHmmss");
			pattern = "yyyy년 MM월 dd일 HH시 mm분 ss초";
			break;
		}

		if (pattern == null) return dateStr;


		String result = "";

		try {
			Date date = format.parse(dateStr);
			format.applyPattern(pattern);
			result = format.format(date);
		} catch (ParseException e) {
			return dateStr;
		}

		return result;
	}

	/**
	 * 입력된 날짜의 달을 영어 이름 형식으로 리턴한다.
	 * @param dateStr
	 * @return String
	 */
	public static String toDateEngMonth(String dateStr){
		String result = null;

		try {
			result = toDateString(parseDate(dateStr, "yyyyMM"), "MMMMM", Locale.US).toLowerCase();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 입력된 날짜를 HH:mm:ss 형식으로 리턴한다.
	 * @param timeStr
	 * @return String
	 */
	public static String toTime(String timeStr){
		if(timeStr == null)
			return null;
		timeStr = timeStr.trim();
		int len = timeStr.length();
		String result = null;
		if(len == 6){
			//시간만
			result = timeStr.substring(0, 2) + ":" + timeStr.substring(2, 4) + ":" + timeStr.substring(4, 6);
		}else if(len == 4){
			result = timeStr.substring(0, 2) + ":" + timeStr.substring(2, 4);
		}else{
			result = timeStr;
		}
		return result;
	}

	/**
	 * 입력된 날짜를 시분초 형식으로 리턴한다.
	 * @param timeStr
	 * @return String
	 */
	public static String toTimeKorean(String timeStr){
		if(timeStr == null)
			return null;
		timeStr = timeStr.trim();
		int len = timeStr.length();
		String result = null;
		if(len == 6){
			//시간만
			result = timeStr.substring(0, 2) + "시" + timeStr.substring(2, 4) + "분" + timeStr.substring(4, 6) + "초";
		}else if(len == 4){
			result = timeStr.substring(0, 2) + "시" + timeStr.substring(2, 4) + "분";
		}else{
			result = timeStr;
		}
		return result;
	}

	/**
	 * 입력된 날짜를 시분초 형식으로 리턴한다.
	 * @param timeStr
	 * @return String
	 */
	public static String toTimeKoreanSpaced(String timeStr){
		if(timeStr == null)
			return null;
		timeStr = timeStr.trim();
		int len = timeStr.length();
		String result = null;
		if(len == 6){
			//시간만
			result = timeStr.substring(0, 2) + "시 " + timeStr.substring(2, 4) + "분 " + timeStr.substring(4, 6) + "초";
		}else if(len == 4){
			result = timeStr.substring(0, 2) + "시 " + timeStr.substring(2, 4) + "분";
		}else{
			result = timeStr;
		}
		return result;
	}

	/**
	 * Date를 "2013.05.25"형태로 리턴한다.
	 * @param date
	 * @return String
	 */
	public static String toDateString(Date date){
		if (date == null) return null;
		String format = "yyyy.MM.dd";
		return toDateString(date, format);
	}

	/**
	 * Date를 format형태로 리턴한다.
	 * @param date
	 * @param format
	 * @return String
	 */
	public static String toDateString(Date date, String format){
		if (date == null) return null;
		SimpleDateFormat df = new SimpleDateFormat(format);
		return df.format(date);
	}

	/**
	 * Date를 format형태로 리턴한다.
	 * @param date
	 * @param format
	 * @param locale
	 * @return String
	 */
	public static String toDateString(Date date, String format, Locale locale){
		if (date == null) return null;
		SimpleDateFormat df = new SimpleDateFormat(format, locale);
		return df.format(date);
	}

	/**
	 * String형 날짜를 Date 형으로 변환
	 * @param dateStr
	 * @param format
	 * @return Date
	 * @throws ParseException
	 */
	public static Date parseDate(String dateStr, String format) throws ParseException{
		SimpleDateFormat df = new SimpleDateFormat(format);
		return df.parse(dateStr);
	}

	/**
	 * 현재 일자를 'yyyy.MM.dd' 형으로 리턴한다.
	 * @return String
	 */
	public static String toDay() {
		String format = "yyyy.MM.dd";
		return toDateString(new Date(), format);
	}

	/**
	 * 현재 일자를 format 리턴한다.
	 * @return String
	 */
	public static String toDay(String format) {
		return toDateString(new Date(), format);
	}

	/**
	 * 입력된 날짜로 최근 글에 해당하는지 판단
	 *
	 * @return boolean
	 */
	public static boolean isNewContent(Date pDate, int iVal) {
		String format = "yyyyMMdd";
		String sDt = DateConverter.toDateString(pDate, format);
		String today = DateUtil.getCurrent(format);
		long subDtVal = DateUtil.subtractDays(sDt,today);

		return subDtVal < iVal;
	}

	/**
	 * yyyy.MM.dd 형태의 날짜를 M/d 형태로 변경
	 * @param date
	 * @return
	 */
	public static String toDateFormatSlash(String date) {
		if (StringUtils.isBlank(date)) {
			return "";
		}

		String[] dates = date.split("\\.");
		return Integer.parseInt(dates[1]) + "/" + Integer.parseInt(dates[2]);
	}

}
