package kr.co.demo.util;

import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Component
public class ApplicationUtil {

    /**
     * static method에서 Application Context 가져오기
     * @return
     */
    public static WebApplicationContext getApplicationContext() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        ServletContext context = session.getServletContext();
        return WebApplicationContextUtils.getWebApplicationContext(context);
    }

}
