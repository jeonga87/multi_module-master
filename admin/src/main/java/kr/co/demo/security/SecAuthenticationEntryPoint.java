package kr.co.demo.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class SecAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
    String requestedWithHeader = request.getHeader("X-Requested-With");

    // AJAX 통신 일 경우에 response.sendError 를 보내줌.
    if ("XMLHttpRequest".equals(requestedWithHeader)) {
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    } else {
      response.sendRedirect("/login?r="+request.getRequestURI());
    }
  }
}
