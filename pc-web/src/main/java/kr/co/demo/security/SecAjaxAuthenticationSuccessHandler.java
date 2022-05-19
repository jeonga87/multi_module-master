package kr.co.demo.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Spring Security success handler, specialized for Ajax requests.
 */
@Component
public class SecAjaxAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private final Logger log = LoggerFactory.getLogger(SecAjaxAuthenticationSuccessHandler.class);

  @Autowired
  private SecAjaxSuccessUtil secAjaxSuccessUtil;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
    secAjaxSuccessUtil.success(response);
  }
}
