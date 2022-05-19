package kr.co.demo.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateUtil {

		public final static String FORMAT_YYYYMMDD = "yyyyMMdd";
		public static final SimpleDateFormat yyyyMMdd = new SimpleDateFormat(FORMAT_YYYYMMDD);

		final static String[] lDays = {"31", "29", "31", "30", "31", "30", "31", "31", "30", "31", "30", "31"};
		final static String[] sDays = {"31", "28", "31", "30", "31", "30", "31", "31", "30", "31", "30", "31"};

		public final static int SUNDAY = Calendar.SUNDAY;
		public final static int MONDAY = Calendar.MONDAY;
		public final static int TUESDAY = Calendar.TUESDAY;
		public final static int WEDNESDAY = Calendar.WEDNESDAY;
		public final static int THURSDAY = Calendar.THURSDAY;
		public final static int FRIDAY = Calendar.FRIDAY;
		public final static int SATURDAY = Calendar.SATURDAY;

		/**
		 * Getting Current Date String
		 * @param format
		 * @return String
		 */
		public static String getCurrent(String format){
			return new SimpleDateFormat(format, Locale.getDefault()).format(new Date());
		}

		/**
		 * Getting Current Date String
		 * @param format
		 * @return String
		 */
		public static String getYesterDate(String format){
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -1);
			return new SimpleDateFormat(format, Locale.getDefault()).format(cal.getTime());
		}

		/**
		 * Getting Current Date String
		 * @param format
		 * @param locale
		 * @return String
		 */
		public static String getCurrent(String format, Locale locale){
			return new SimpleDateFormat(format, locale).format(new Date());
		}

		/**
		 * Getting Current Date String (yyyy-MM-dd)
		 * @return String
		 */
		public static String getCurrentDate(){
			return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
		}

		/**
		 * Getting Current Time (HH:mm:ss)
		 * @return String
		 */
		public static String getCurrentTime(){
			return new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
		}

		/**
		 * Getting Current Time (yyyy-MM-dd HH:mm:ss)
		 * @return String
		 */
		public static String getCurrentDateTime(){
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
		}

		/**
		 * Getting Current Time 24 (HH:mm:ss)
		 * @param locale
		 * @return String
		 */
		public static String getCurrentTime24(Locale locale){
			return getCurrent("HH:mm:ss", locale);
		}

		/**
		 * Getting Current Time 12 (hh:mm:ss)
		 * @param locale
		 * @return String
		 */
		public static String getCurrentTime12(Locale locale){
			return getCurrent("hh:mm:ss", locale);
		}

		/**
		 * Getting Current Year
		 * @return int
		 */
		public static int getCurrentYear(){
			return Integer.parseInt(getCurrentYearString());
		}

		/**
		 * Getting Current Month
		 * @return int
		 */
		public static int getCurrentMonth(){
			return Integer.parseInt(getCurrentMonthString());
		}

		/**
		 * Getting Current Day
		 * @return int
		 */
		public static int getCurrentDay(){
			return Integer.parseInt(getCurrentDayString());
		}

		/**
		 * Getting Current Hour
		 * @return int
		 */
		public static int getCurrentHour(){
			return Integer.parseInt(getCurrent("HH"));
		}

		/**
		 * Getting Current Minute
		 * @return int
		 */
		public static int getCurrentMinute(){
			return Integer.parseInt(getCurrent("mm"));
		}

		/**
		 * Getting Current Year String
		 * @return String
		 */
		public static String getCurrentYearString(){
			return getCurrent("yyyy");
		}

		/**
		 * Getting Current Month String
		 * @return String
		 */
		public static String getCurrentMonthString(){
			return getCurrent("MM");
		}

		/**
		 * Getting Current Year and Month String
		 * @return String
		 */
		public static String getCurrentYearMonthString(){
			return getCurrent("yyyyMM");
		}

		/**
		 * Getting Current Day String
		 * @return String
		 */
		public static String getCurrentDayString(){
			return getCurrent("dd");
		}

		/**
		 * Getting Current Month and Day String
		 * @return String
		 */
		public static String getMonthDayString(){
			return getCurrent("MMdd");
		}

		/**
		 * 현재의 분기를 리턴한다. (1:1분기, 2:2분기, 3:4분기, 4:4분기)
		 * @return int
		 */
		public static int getCurrentQuarter(){
			int currentMonth = getCurrentMonth();
			if(currentMonth < 4)
				return 1;
			else if(currentMonth < 7)
				return 2;
			else if(currentMonth < 10)
				return 3;
			else
				return 4;
		}

		/**
		 * 현재의 반기를 리턴한다. (1:상반기, 2:하반기)
		 * @return int
		 */
		public static int getCurrentHalf(){
			int currentMonth = getCurrentMonth();
			if(currentMonth >= 6)
				return 1;
			return 2;
		}

		/**
		 * 금월의 첫날을 리턴한다.
		 * @return Date
		 */
		public static Date getFirstDayOfCurrentMonth(){
			String dateStr = null;
			int month = getCurrentMonth();
			if(month < 10){
				dateStr = getCurrentYearString() + "0" + month + "01";
			}else{
				dateStr = getCurrentYearString() + month + "01";
			}
			try {
				return yyyyMMdd.parse(dateStr);
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		}

		/**
		 * 금월의 마지막날을 리턴한다.
		 * @return Date
		 */
		public static Date getLastDayOfCurrentMonth(){
			String dateStr = null;
			if(getCurrentYear() % 4 == 0){
				dateStr = lDays[getCurrentMonth() -1];
			}else{
				dateStr = sDays[getCurrentMonth() - 1];
			}
			try {
				return yyyyMMdd.parse(DateUtil.getCurrent("yyyyMM") + dateStr);
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		}

		/**
		 * 입력된 년월의 첫날로 변환하여 리턴한다.
		 * @param yyyyMM
		 * @return String
		 */
		public static String toFirstDay(String yyyyMM){
			Date firstDate = getFirstDayOf(yyyyMM.substring(0, 4), yyyyMM.substring(4, 6));
			return new SimpleDateFormat("yyyyMMdd").format(firstDate);
		}

		/**
		 * 입력된 년월의 첫날을 리턴한다.
		 * @param year
		 * @param month
		 * @return Date
		 */
		public static Date getFirstDayOf(String year, String month){
			return getFirstDayOf(Integer.parseInt(year), Integer.parseInt(month));
		}

		/**
		 * 입력된 년월의 첫날을 리턴한다.
		 * @param year
		 * @param month
		 * @return Date
		 */
		public static Date getFirstDayOf(int year, int month){
			String dateStr = null;
			if(month < 10){
				dateStr = year + "0" + month + "01";
			}else{
				dateStr = year + "" + month + "01";
			}
			try {
				return yyyyMMdd.parse(dateStr);
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		}

		/**
		 * 입력된 년월의 마지막날로 변환하여 리턴한다.
		 * @param yyyyMM
		 * @return String
		 */
		public static String toLastDay(String yyyyMM){
			Date lastDate = getLastDayOf(yyyyMM.substring(0, 4), yyyyMM.substring(4, 6));
			return new SimpleDateFormat("yyyyMMdd").format(lastDate);
		}

		/**
		 * 입력된 년월의 마지막날을 리턴한다.
		 * @param year
		 * @param month
		 * @return Date
		 */
		public static Date getLastDayOf(String year, String month){
			return getLastDayOf(Integer.parseInt(year), Integer.parseInt(month));
		}

		/**
		 * 입력된 년월의 마지막날을 리턴한다.
		 * @param year
		 * @param month
		 * @return Date
		 */
		public static Date getLastDayOf(int year, int month){
			String dateStr = null;
			if(month < 10){
				dateStr = year + "0" + month;
			}else{
				dateStr = year + "" + month;
			}
			if(year % 4 == 0){
				dateStr = dateStr + lDays[month -1];
			}else{
				dateStr = dateStr + sDays[month - 1];
			}
			try {
				return yyyyMMdd.parse(dateStr);
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		}

		/**
		 * 입력된 년도의 첫날을 리턴한다.
		 * @param year
		 * @return Date
		 */
		public static Date getFirstDayOfYear(String year){
			try {
				return yyyyMMdd.parse(year + "0101");
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		}

		/**
		 * 입력된 년도의 마지막날을 리턴한다.
		 * @param year
		 * @return Date
		 */
		public static Date getLastDayOfYear(String year){
			try {
				return yyyyMMdd.parse(year + "1231");
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		}

		/**
		 * 분기별 첫날을 리턴한다.
		 * @param quarter
		 * @return Date
		 */
		public static Date getFirstDayOfQuarter(int quarter){
			String dateStr = null;
			switch (quarter) {
				case 1 :
					dateStr = getCurrentYearString() + "0101";
					break;
				case 2 :
					dateStr = getCurrentYearString() + "0401";
					break;
				case 3 :
					dateStr = getCurrentYearString() + "0701";
					break;
				default :
					dateStr = getCurrentYearString() + "1001";
					break;
			}
			try {
				return yyyyMMdd.parse(dateStr);
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		}

		/**
		 * 지정된 년도의 분기별 첫날을 리턴한다.
		 * @param year
		 * @param quarter
		 * @return Date
		 */
		public static Date getFirstDayOfQuarter(String year, int quarter){
			String dateStr = null;
			switch (quarter) {
			case 1 :
				dateStr = year + "0101";
				break;
			case 2 :
				dateStr = year + "0401";
				break;
			case 3 :
				dateStr = year + "0701";
				break;
			default :
				dateStr = year + "1001";
				break;
			}
			try {
				return yyyyMMdd.parse(dateStr);
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		}

		/**
		 * 분기별 마지막 날짜를 리턴한다.
		 * @param quarter
		 * @return Date
		 */
		public static Date getLastDayOfQuarter(int quarter){
			String dateStr = null;
			switch (quarter) {
				case 1 :
					dateStr = getCurrentYearString() + "0331";
					break;
				case 2 :
					dateStr = getCurrentYearString() + "0630";
					break;
				case 3 :
					dateStr = getCurrentYearString() + "0930";
					break;
				default :
					dateStr = getCurrentYearString() + "1231";
					break;
			}
			try {
				return yyyyMMdd.parse(dateStr);
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		}

		/**
		 * 지정된 년도의 분기별 마지막 날짜를 리턴한다.
		 * @param year
		 * @param quarter
		 * @return Date
		 */
		public static Date getLastDayOfQuarter(String year, int quarter){
			String dateStr = null;
			switch (quarter) {
			case 1 :
				dateStr = year + "0331";
				break;
			case 2 :
				dateStr = year + "0630";
				break;
			case 3 :
				dateStr = year + "0930";
				break;
			default :
				dateStr = year + "1231";
				break;
			}
			try {
				return yyyyMMdd.parse(dateStr);
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		}

		/**
		 * 입력된 날짜가 속한 주의 week 기준 요일의 날짜를 리턴
		 * 		ex) 2008-12-11 일의 MONDAY(일요일)를 입력하면 2008-12-08 일이 리턴됨.
		 * @param week (1:일요일, 2:월요일, 3:화요일 ...7:토요일)
		 * @param date
		 * @return Date
		 */
		public static Date getDayOfWeek(int week, Date date){
	        Calendar todayCalendar = Calendar.getInstance();
	        todayCalendar.setTime(date);

	        int dayOfWeek = todayCalendar.get(Calendar.DAY_OF_WEEK);
			String today = yyyyMMdd.format(date);

			String dateStr = null;
			if(dayOfWeek == week){
				dateStr = today;
			}else{
				dateStr = moveDay(today, ((dayOfWeek - (week+1)) * (-1)));
			}
			try {
				return yyyyMMdd.parse(dateStr);
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		}

		/**
		 * 입력된 날짜가 속한 주의 week 기준 요일의 날짜를 리턴
		 * 		ex) 2008-12-11 일의 MONDAY(일요일)를 입력하면 2008-12-08 일이 리턴됨.
		 * @param week (1:일요일, 2:월요일, 3:화요일 ...7:토요일)
		 * @param timeMillis
		 * @return Date
		 */
		public static Date getDayOfWeek(int week, long timeMillis){
			Date date = new Date(timeMillis);
			return getDayOfWeek(week, date);
		}

		/**
		 * 윤년인지 여부 리턴
		 * @param year
		 * @return boolean
		 */
		public static boolean isLunarYear(int year){
			return ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0);
		}

		public static final String COMPARE_TYPE_DAY = "day";
		public static final String COMPARE_TYPE_HOUR = "hour";
		public static final String COMPARE_TYPE_MINUTE = "minute";
		public static final String COMPARE_TYPE_SECOND = "second";

		/**
		 * 현재 날짜(시간)와 입력한 날짜(시간)와의 차이
		 * @param date - 20050707120202
		 * @param type - day, hour, minute, second
		 * @return long
		 */
		public static long compareThisDate(String date, String type) throws Exception{
			if(date == null)
				throw new NullPointerException("DateUtil.compareThisDate : Date is Null");

			date = date.replaceAll("\\/", "");
			date = date.replaceAll("\\-", "");
			date = date.replaceAll("\\:", "");
			date = date.replaceAll("\\.", "");

			int len = date.length();
			long rvGap = 0;

			Calendar cal1=Calendar.getInstance();
			Calendar cal2=Calendar.getInstance();

			int year = 0;
			int month = 0;
			int day = 0;
			int hour = 0;
			int minute = 0;
			int second = 0;

			if(len >= 4)
				year = Integer.parseInt(date.substring(0, 4));
			if(len >= 6)
				month = Integer.parseInt(date.substring(4, 6));
			if(len >= 8)
				day = Integer.parseInt(date.substring(6, 8));
			if(len >= 10)
				hour = Integer.parseInt(date.substring(8, 10));
			if(len >= 12)
				minute = Integer.parseInt(date.substring(10, 12));
			if(len >= 14)
				second = Integer.parseInt(date.substring(12, 14));

			cal1.set(year, month - 1, day, hour, minute, second);

			long cal1sec = cal1.getTime().getTime();
			long cal2sec = cal2.getTime().getTime();

			long gap = cal2sec - cal1sec;

			if (type.equals(COMPARE_TYPE_DAY)) rvGap = (gap / 86400) / 1000;
			else if (type.equals(COMPARE_TYPE_HOUR)) rvGap = (gap / 3600) / 1000;
			else if (type.equals(COMPARE_TYPE_MINUTE)) rvGap = (gap / 60) / 1000;
			else if (type.equals(COMPARE_TYPE_SECOND)) rvGap = gap / 1000;

			return rvGap;
		}

		/**
		 * @param date
		 * @return boolean
		 */
		public static boolean isAfterDay(String date) throws Exception{
			long r = compareThisDate(date, COMPARE_TYPE_DAY);
			if(r <= 0){
				return true;
			}else{
				return false;
			}
		}

		/**
		 * @param date
		 * @return boolean
		 * @throws Exception
		 */
		public static boolean isAfterSecond(String date) throws Exception{
			long r = compareThisDate(date, COMPARE_TYPE_SECOND);
			if(r <= 0){
				return true;
			}else{
				return false;
			}
		}

		/**
		 * @param date
		 * @return boolean
		 */
		public static boolean isBeforeDay(String date)throws Exception{
			long r = compareThisDate(date, COMPARE_TYPE_DAY);
			if(r <= 0){
				return false;
			}else{
				return true;
			}
		}

		/**
		 * @param date
		 * @return boolean
		 * @throws Exception
		 */
		public static boolean isBeforeSecond(String date)throws Exception{
			long r = compareThisDate(date, COMPARE_TYPE_SECOND);
			if(r <= 0){
				return false;
			}else{
				return true;
			}
		}

		/**
		 * 주어진 날짜의 주어진 년도 뒤의 날짜를 리턴한다.<BR>
		 * @param yyyyMMdd 주어진 날짜
		 * @param year 주어진 년도수( ex 1 이면 1년 후의 날짜를 리턴 -1이면 1년 이전의 날짜를 리턴)
		 * @return String 주어진 년도의 주어진 년도 수 뒤의 날짜
		 */
		public static String moveYear(String yyyyMMdd, int year){
			if(yyyyMMdd == null || !(yyyyMMdd.length() == 8 || yyyyMMdd.length() == 14)) {
				throw new RuntimeException(yyyyMMdd);
			}

			int iYear = Integer.parseInt(yyyyMMdd.substring(0, 4));
			int iMonth = Integer.parseInt(yyyyMMdd.substring(4, 6)) - 1;  // 달은 0부터 시작 한다.
			int iDay = Integer.parseInt(yyyyMMdd.substring(6, 8));

			// Calendar객체를 생성하고 날짜와 시간을 설정한다.
			Calendar paramDate = new GregorianCalendar();
			paramDate.set(iYear, iMonth, iDay, 0, 0, 0);

			Calendar c = Calendar.getInstance();
			c.setTime(paramDate.getTime());
			c.add(Calendar.YEAR, year);
			Date newDate = c.getTime();

			return DateConverter.toDateString(newDate, "yyyyMMdd");
		}

	/**
	 * 주어진 날짜의 주어진 년도 뒤의 날짜를 리턴한다.<BR>
	 * @param yyyyMMdd 주어진 날짜
	 * @param year 주어진 년도수( ex 1 이면 1년 후의 날짜를 리턴 -1이면 1년 이전의 날짜를 리턴)
	 * @return String 주어진 년도의 주어진 년도 수 뒤의 날짜
	 */
		public static String moveYear2(String yyyyMMdd, int year){
			if(yyyyMMdd == null || !(yyyyMMdd.length() == 8 || yyyyMMdd.length() == 14)) {
				throw new RuntimeException(yyyyMMdd);
			}

			int iYear = Integer.parseInt(yyyyMMdd.substring(0, 4));
			int iMonth = Integer.parseInt(yyyyMMdd.substring(4, 6)) - 1;  // 달은 0부터 시작 한다.
			int iDay = Integer.parseInt(yyyyMMdd.substring(6, 8));

			// Calendar객체를 생성하고 날짜와 시간을 설정한다.
			Calendar paramDate = new GregorianCalendar();
			paramDate.set(iYear, iMonth, iDay, 0, 0, 0);

			Calendar c = Calendar.getInstance();
			c.setTime(paramDate.getTime());
			c.add(Calendar.YEAR, year);
			Date newDate = c.getTime();

			return DateConverter.toDateString(newDate, "yyyy-MM-dd");
		}

		/**
		 * 주어진 날짜의 주어진 개월 수 뒤의 날짜를 리턴한다.<BR>
		 * @param yyyyMMdd 주어진 날짜
		 * @param day 주어진 일수( ex 1 이면 1일 후의 날짜를 리턴 -1이면 1일 이전의 날짜를 리턴)
		 * @return String 주어진 날짜의 주어진 개월 수 뒤의 날짜
		 */
		public static String moveDay(String yyyyMMdd, int day){

			if(yyyyMMdd == null || !(yyyyMMdd.length() == 8 || yyyyMMdd.length() == 14)) {
				throw new RuntimeException(yyyyMMdd);
			}

			int iYear = Integer.parseInt(yyyyMMdd.substring(0, 4));
			int iMonth = Integer.parseInt(yyyyMMdd.substring(4, 6)) - 1;  // 달은 0부터 시작 한다.
			int iDay = Integer.parseInt(yyyyMMdd.substring(6, 8));

			// 시간을 가져온다.
			int iHour = 0;
			int iMin = 0;
			int iSecond = 0;
			String pattern = "yyyyMMdd";
			if(yyyyMMdd.length() == 14) {
				iHour = Integer.parseInt(yyyyMMdd.substring(8, 10));
				iMin = Integer.parseInt(yyyyMMdd.substring(10, 12));
				iSecond = Integer.parseInt(yyyyMMdd.substring(12, 14));
				pattern = "yyyyMMddHHmmssSSS";
			}

			// Calendar객체를 생성하고 날짜와 시간을 설정한다.
			Calendar calCurDate = new GregorianCalendar();
			calCurDate.set(iYear, iMonth, iDay, iHour, iMin, iSecond);

			// 주어진 개월을 더한다.
			int iReturnMonth = calCurDate.get(Calendar.DATE) + day;

			// 달을 설정한다.
			calCurDate.set(Calendar.DATE, iReturnMonth);
			SimpleDateFormat formatter = new SimpleDateFormat (pattern, Locale.KOREA);

			String strResult = formatter.format(calCurDate.getTime());

			if(yyyyMMdd.length() == 8) {
				strResult = strResult.substring(0, 8);
			}

			return strResult;
		}

		/**
		 * 주어진 날짜의 주어진 개월 수 뒤의 날짜를 리턴한다.<BR>
		 * @param yyyyMMdd 주어진 날짜
		 * @param mon 주어진 개월수( ex 1 이면 1개월 후의 날짜를 리턴 -1이면 1개월 이전의 날짜를 리턴)
		 * @return String 주어진 날짜의 주어진 개월 수 뒤의 날짜
		 */
		public static String moveMonth(String yyyyMMdd, int mon) {
			try {
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
				Date date = df.parse(yyyyMMdd);
				Calendar c = Calendar.getInstance();
				c.setTime(date);
				c.add(Calendar.MONTH, mon);
				date = c.getTime();
				return df.format(date);
			} catch (ParseException e) {
				throw new RuntimeException("문자열 날자 변환 실패" + e.getMessage());
			}
		}

		/**
		 * 두 날짜간의 차이 구하기... 일(day)단위 리턴
		 * @param a
		 * @param b
		 * @return long
		 */
		public static long subtractDays(Date a, Date b) {
			long gap = 0;
			gap = a.getTime() - b.getTime();
			gap = gap / 86400000; //24 * 60 * 60 * 1000
			return gap;
		}

		/**
		 * 두 날짜간의 차이 구하기... 일(day)단위 리턴(소수점까지 계산)
		 * @param a
		 * @param b
		 * @return long
		 */
		public static double subtractDaysDouble(Date a, Date b) {
			double gap = 0;
			gap = a.getTime() - b.getTime();
			gap = gap / 86400000.0; //24 * 60 * 60 * 1000
			return gap;
		}

		/**
		 * 두 날짜간의 차이 구하기... 월(month)단위 리턴
		 * @param a
		 * @param b
		 * @return long
		 */
		public static long subtractMonths(Date a, Date b) {
			long gap = 0;
			gap = a.getTime() - b.getTime();
			gap = gap / 2592000000L; //30 * 24 * 60 * 60 * 1000
			return gap;
		}

		/**
		 * 두 날짜간의 차이 구하기... 일(day)단위 리턴
		 * @param start
		 * @param end
		 * @param format
		 * @return long
		 */
		public static long subtractDays(String start, String end, String format) {
			long gap = 0;
			try {
				if( format == null)
					format = "yyyyMMdd";
				SimpleDateFormat sdf = new SimpleDateFormat(format);
				Date date1 = sdf.parse(start);
				Date date2 = sdf.parse(end);
				gap = subtractDays(date1, date2);
				return gap;
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		}

		/**
		 * 두 날짜간의 차이 구하기... 월(month)단위 리턴
		 * @param start
		 * @param end
		 * @param format
		 * @return long
		 */
		public static long subtractMonths(String start, String end, String format) {
			long gap = 0;
			try {
				if( format == null)
					format = "yyyyMM";
				SimpleDateFormat sdf = new SimpleDateFormat(format);
				Date date1 = sdf.parse(start);
				Date date2 = sdf.parse(end);
				gap = subtractMonths(date1, date2);
				return gap;
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		}

		/**
		 * 두 날짜간의 차이 구하기...일(day)단위 리턴
		 * @param start
		 * @param end
		 * @return long
		 */
		public static long subtractDays(String start, String end) {
			return subtractDays(start, end, null);
		}

		/**
		 * 두 날짜간의 차이 구하기...월(month)단위 리턴
		 * @param start 6자리(yyyyMM)
		 * @param end 6자리(yyyyMM)
		 * @return long
		 */
		public static long subtractMonths(String start, String end) {
			return subtractMonths(start, end, null);
		}

		/**
		 * 제공된 기간내 지정한 요일의 일수를 구하기
		 * @param fromDate
		 * @param toDate
		 * @param weeks
		 * @param format
		 * @return int
		 * @throws Exception
		 */
		public static int countDayOfWeek(String fromDate, String toDate, int[] weeks, String format) throws Exception{
			Date s = DateConverter.parseDate(fromDate, format);
			Date e = DateConverter.parseDate(toDate, format);
			return countDayOfWeek(s, e, weeks);
		}

		/**
		 * 제공된 기간내 지정한 요일의 일수를 구하기
		 * @param fromDate
		 * @param toDate
		 * @param weeks
		 * @return int
		 */
		public static int countDayOfWeek(Date fromDate, Date toDate, int[] weeks){
			int count = 0;

			Calendar c = Calendar.getInstance();
			c.setTime(fromDate);
			while(!c.getTime().equals(toDate)){
				for(int i = 0; i < weeks.length; i++){
					if(weeks[i] == c.get(Calendar.DAY_OF_WEEK)){
						count++;
						break;
					}
				}
				c.add(Calendar.DAY_OF_MONTH, 1);
			}

			return count;
		}

		/**
		 * 특정 일자 (yyyyMMdd)의 값을 받아서 요일을 찾는다.
		 * week (1:일요일, 2:월요일, 3:화요일 ...7:토요일)
		 * @param date
		 * @return int
		 */
		public static int dayOfWeek(String date){

			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.US);
			Calendar cal = Calendar.getInstance();
			int dayOfWeek = 0;

			try {
				cal.setTime(sdf.parse(date));
				dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
			} catch (ParseException e) {
				e.printStackTrace();
			}

			return dayOfWeek;
		}

		/**
		 * 입력된 문자열의 날짜가 유효한 날짜인지 여부 리턴
		 * @param date
		 * @param format
		 * @return boolean
		 */
		public static boolean isValidDate(String date, String format){
			if(date == null){
				return false;
			}
			date = date.replaceAll(" ", "");
			try {
				if(date.length() != format.length()){
					return false;
				}

				SimpleDateFormat sdf = new SimpleDateFormat(format);
				sdf.parse(date);
			} catch (ParseException e) {
				return false;
			}
			return true;
		}

}
