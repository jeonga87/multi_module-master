package kr.co.demo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.TimeZone;

public class WebUtil {

    private static final Logger log = LoggerFactory.getLogger(WebUtil.class);

    private static int CACHE_PERIOD_UNIT = Calendar.MONTH;
    private static int CACHE_PERIOD_VALUE = 1;

    public static boolean needFreshResponse(HttpServletRequest request, SimpleDateFormat dateFormat) {
        boolean needFresh = true;
        String modifiedSince = request.getHeader("if-modified-since");
        if(modifiedSince == null) {
            Enumeration<String> sinceHeaders = request.getHeaders("if-modified-since");
            if(sinceHeaders.hasMoreElements()) {
                modifiedSince = sinceHeaders.nextElement();
            }
        }

        if(modifiedSince != null) {
            try {
                Date lastAccess = dateFormat.parse(modifiedSince);

                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                cal.add(CACHE_PERIOD_UNIT, CACHE_PERIOD_VALUE * -1);
                if(cal.getTime().compareTo(lastAccess) < 0) {
                    needFresh = false;
                }
            } catch (Exception ignore) {}
        }

        return needFresh;

    }

    public static void setCacheHeader(HttpServletResponse response, SimpleDateFormat dateFormat) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        response.setHeader("Last-Modified", dateFormat.format(cal.getTime()));

        cal.add(CACHE_PERIOD_UNIT, CACHE_PERIOD_VALUE);

        String maxAgeDirective = "max-age=" + (cal.getTimeInMillis() - System.currentTimeMillis()) / 1000L;

        response.setHeader("Cache-Control",  maxAgeDirective);
        response.setHeader("Expires", dateFormat.format(cal.getTime()));
    }

    public static void writeFile(HttpServletResponse response, File f, FileInputStream fin, FileChannel inputChannel, WritableByteChannel outputChannel) throws IOException {
        try{
            fin = new FileInputStream(f);
            inputChannel = fin.getChannel();

            outputChannel = Channels.newChannel(response.getOutputStream());
            inputChannel.transferTo(0, fin.available(), outputChannel);
        }catch(Exception e){
            throw e;
        }finally{
            try{
                if(fin != null) fin.close();
                if(inputChannel.isOpen()) inputChannel.close();
                if(outputChannel.isOpen()) outputChannel.close();
            }catch(Exception e){
                fin.close();
                inputChannel.close();
                outputChannel.close();
            }
        }
    }

    /**
     * POST로 특정 URL 호출
     * @param urlStr
     */
    public static void postUrl(String urlStr) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept-Language", "ko-kr,ko;q=0.8,en-us;q=0.5,en;q=0.3");
            conn.setConnectTimeout(1000);
            conn.setReadTimeout(1000);

            //전송
            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
            osw.write("");
            osw.flush();

            //응답
            BufferedReader br = null;
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line = null;
            log.debug("POST 호출 성공 : " + urlStr);
            while ((line = br.readLine()) != null) {
                log.debug(line);
            }

            //닫기
            osw.close();
            br.close();
        } catch (Exception e) {
            log.debug("POST 호출 실패 : " + urlStr);
        }
    }
}
