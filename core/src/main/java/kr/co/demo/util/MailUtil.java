package kr.co.demo.util;

import kr.co.demo.member.domain.Member;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.io.InputStream;

@Component
public class MailUtil implements EnvironmentAware {

    private static final Logger log = LoggerFactory.getLogger(MailUtil.class);

    private static Environment env;

    @Override
    public void setEnvironment(Environment environment) {
        this.env = environment;
    }

    /**
     * 회원 가입 완료 메일 발송
     * @param member 회원 정보
     */
    public static void sendMemberJoinMail(Member member) {
        String toEmail = member.getEmail();
        String toName = member.getName();
        String content = getTemplate("member_join");
        String joinDt = DateConverter.toDateDot(member.getCreatedDt().substring(0,10).replaceAll("\\-", ""));

        content = content.replaceAll("\\{\\{MEM_NAME\\}\\}", toName);       // 회원 이름
        content = content.replaceAll("\\{\\{MEM_EMAIL\\}\\}", toEmail);     // 회원 이메일
        content = content.replaceAll("\\{\\{JOIN_DT\\}\\}", joinDt);        // 회원 가입일

        sendMail(toEmail, toName, "회원가입을 축하 드립니다.", content);
    }

    /**
     * 아이디 찾기 메일 발송
     * @param member 회원 정보
     */
    public static void sendFindIdMail(Member member) {
        String toEmail = member.getEmail();
        String toName = member.getName();
        String content = getTemplate("find_id");
        String toMemId = member.getId();

        content = content.replaceAll("\\{\\{MEM_NAME\\}\\}", toName);          // 회원 이름
        content = content.replaceAll("\\{\\{MEM_ID\\}\\}", toMemId);           // 회원 아이디

        sendMail(toEmail, toName, "아이디 확인 안내입니다.", content);
    }

    /**
     * 비밀번호 찾기(임시비밀번호 발급) 메일 발송
     * @param member 회원 정보
     */
    public static void sendFindPwdMail(Member member, String tempPassword) {
        String toEmail = member.getEmail();
        String toName = member.getName();
        String content = getTemplate("find_pwd");

        content = content.replaceAll("\\{\\{MEM_NAME\\}\\}", toName);            // 회원 이름
        content = content.replaceAll("\\{\\{MEM_TMP_PWD\\}\\}", tempPassword);   // 임시비밀번호

        sendMail(toEmail, toName, "임시비밀번호 발급 안내입니다.", content);
    }

    /**
     * 휴면회원 전환 안내 메일 발송
     * (1개월 이전에 발송 할 것)
     * @param member 회원 정보
     */
    public static void sendInactiveMemberInfoMail(Member member) {
        String toEmail = member.getEmail();
        String toName = member.getName();
        String content = getTemplate("member_inactive");

        content = content.replaceAll("\\{\\{MEM_NAME\\}\\}", toName); // 회원 이름
        String inactiveDt = DateConverter.toDate(
                DateUtil.moveYear(member.getLastLoginDt().replace("-", ""), 1)
        );
        content = content.replaceAll("\\{\\{INACTIVE_DT\\}\\}", inactiveDt); // 휴면 전환 예정일 (최종 로그인으로 부터 1년)
        String activeLastDt = DateConverter.toDate(
                DateUtil.moveDay(inactiveDt.replace("-", ""), -1)
        );
        content = content.replaceAll("\\{\\{ACTIVE_LAST_DT\\}\\}", activeLastDt); // 휴면 전환 예정일 - 1일

        sendMail(toEmail, toName, "휴면 계정 처리안내", content);
    }

    /**
     * 휴면계정 처리 안내 메일
     * (휴면계정 처리예정일로 부터 1개월 전 발송)
     * @param toEmail 수신자 메일주소
     * @param toName 수신자 이름
     * @param dormancyDt 휴면계정 처리예정일(yyyyMMdd)
     */
    public static void sendDormancyInfoMail(String toEmail, String toName, String dormancyDt) {
        if(DateUtil.isValidDate(dormancyDt, "yyyyMMdd")) {
            String content = getTemplate("dormancy");
            dormancyDt = DateConverter.toDateDot(dormancyDt);
            content = content.replaceAll("\\{\\{DORMANCY_DT\\}\\}", dormancyDt);   // 휴면계정 처리예정일
            sendMail(toEmail, toName, "에이치엘 사이언스 공식몰 휴면 개인정보 분리저장안내 메일입니다.", content);
        } else {
            log.debug("휴면계정 처리 안내메일 발송 실패. email: " + toEmail);
        }
    }

    /**
     * 메일 발송
     * @param toEmail
     * @param toName
     * @param title
     * @param content
     */
    private static void sendMail(String toEmail, String toName, String title, String content) {
        // 홈페이지 주소 넣기
        String homeUrl = env.getProperty("demo.url.pc-web");  // 홈페이지 주소(PC-WEB)
        content = content.replaceAll("\\{\\{HOME_URL\\}\\}", homeUrl);     // SMS 서비스 수신 여부

        log.info("##### 메일 발송 #####");
        log.info("toEmail : " + toEmail);
        log.info("toName : " + toName);
        log.info("title : " + title);
        log.info("content : " + content);
        if(StringUtils.isNotEmpty(toEmail)
                && StringUtils.isNotEmpty(toName)
                && StringUtils.isNotEmpty(title)
                && StringUtils.isNotEmpty(content)) {
            Mail mail = new Mail();
            // 발송자 정보(gmail)
            String fname = env.getProperty("demo.mail.name");
            String fid = env.getProperty("demo.mail.id");
            String fpw = env.getProperty("demo.mail.pw");
            /*log.info("fname : " + fname);
            log.info("fid : " + fid);
            log.info("fpw : " + fpw);*/
            if(StringUtils.isNotEmpty(fname) && StringUtils.isNotEmpty(fid) && StringUtils.isNotEmpty(fpw)) {
                mail.init(fname, fid, fpw, toEmail, toName, "수영연맹 | " + title, content);
                mail.sendMail();
            }
        }
    }

    /**
     * 메일 템플릿 문자열로 가져오기
     * @param templateName 템플릿명
     */
    private static String getTemplate(String templateName) {
        StringBuffer sb = new StringBuffer();
        try {
            WebApplicationContext applicationContext = ApplicationUtil.getApplicationContext();
            Resource resource = applicationContext.getResource("classpath:mail/" + templateName + ".html");
            InputStream is = resource.getInputStream();

            byte[] b = new byte[4096];
            for (int n; (n = is.read(b)) != -1;) {
                sb.append(new String(b, 0, n));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

}