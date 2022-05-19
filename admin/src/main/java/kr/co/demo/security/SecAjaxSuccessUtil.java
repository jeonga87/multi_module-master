package kr.co.demo.security;

import kr.co.demo.admin.domain.Admin;
import kr.co.demo.admin.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;

@Component
public class SecAjaxSuccessUtil {

  private final Logger log = LoggerFactory.getLogger(SecAjaxSuccessUtil.class);

  @Autowired
  private AdminService adminService;

  public void success(HttpServletResponse response) {
    log.info("SecurityUtils.getCurrentAdmin()    " + SecurityUtils.getCurrentAdmin());

    // 사용자 로그인 세션 가져오기
    Admin currentAdmin = SecurityUtils.getCurrentAdmin();

    if (currentAdmin != null) {
      // 로그인 로그
      adminService.insertAdminLog(currentAdmin);

      response.setStatus(HttpServletResponse.SC_OK);
    } else {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
  }
}
