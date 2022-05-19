package kr.co.demo.security;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;

/**
 * 사용자 상태 관련 Exception
 */
public class SecAccountStatusException extends UsernameNotFoundException {

  public SecAccountStatusException(String msg) {
    super(msg);
    HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
    response.setHeader("X-Unauthorized-Reason", msg);   // 인증 실패 이유. 이 메세지로 브라우저에서 분기처리
  }
}
