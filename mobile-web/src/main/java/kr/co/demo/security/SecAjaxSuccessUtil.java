package kr.co.demo.security;

import kr.co.demo.member.domain.Member;
import kr.co.demo.member.service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;

@Component
public class SecAjaxSuccessUtil {

  private final Logger log = LoggerFactory.getLogger(SecAjaxSuccessUtil.class);

  @Autowired
  private MemberService memberService;

  public void success(HttpServletResponse response) {
    log.info("SecurityUtils.getCurrentMember()    " + SecurityUtils.getCurrentMember());

    // 사용자 로그인 세션 가져오기
    Member currentMember = SecurityUtils.getCurrentMember();

    if (currentMember != null) {
      // 로그인 로그
      memberService.insertMemberLog(currentMember);

      response.setStatus(HttpServletResponse.SC_OK);
    } else {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
  }
}
