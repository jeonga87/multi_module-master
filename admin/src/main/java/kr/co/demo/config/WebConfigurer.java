package kr.co.demo.config;

import kr.co.demo.filter.SiteMeshFilter;
import org.springframework.boot.web.server.MimeMappings;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.boot.web.servlet.support.ErrorPageFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.ArrayList;
import java.util.List;

@Configuration
/**
 * ServletContextInitializer 이란?
 * web.xml을 사용하지 않고 개발시에 Servlet 및 Filter를 등록하고자 할 때 spring boot 용 인터페이스
 * WebApplicationInitializer(spring3.1), AbstractAnnotationConfigDispatcherServletInitializer(spring3.2) 와 같은 기능
 * */
/**
 * WebServerFactoryCustomizer 이란?
 * 커스터마이징을위한 전략 인터페이스 web server factories
 * */
public class WebConfigurer implements ServletContextInitializer, WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {

  @Override
  public void onStartup(ServletContext servletContext) throws ServletException { }

  /**
   * MIME이란? Multipurpose Internet Mail Extensions의 약자로 파일 변환을 뜻한다.
   * MIME는 이메일과 함께 동봉할 파일을 텍스트 문자로 전환해서 이메일 시스템을 통해 전달하기 위해 개발되었기 때문에 이름에 Internet Mail Extension 있다.
   * 그렇지만 현재는 웹을 통해서 여러형태의 파일 전달하는데 쓰이고 있습니다.
   */
  @Override
  public void customize(ConfigurableServletWebServerFactory factory) {
    MimeMappings mappings = new MimeMappings(MimeMappings.DEFAULT);
    mappings.add("html", "text/html;charset=utf-8"); // IE issue
    mappings.add("json", "text/html;charset=utf-8"); // CloudFoundry issue
    factory.setMimeMappings(mappings);
  }

  /**
   * spring boot - web.xml 대신 아래와 같이 변경됨
   * filter 설정
   * */
  @Bean
  public FilterRegistrationBean characterEncodingFilter() {
    FilterRegistrationBean filterRegBean = new FilterRegistrationBean();
    filterRegBean.setFilter(new CharacterEncodingFilter());
    List<String> urlPatterns = new ArrayList<>();
    urlPatterns.add("/*");
    filterRegBean.setUrlPatterns(urlPatterns);
    filterRegBean.setOrder(-200);
    filterRegBean.setAsyncSupported(true);
    filterRegBean.addInitParameter("encoding", "UTF-8");
    return filterRegBean;
  }

  @Bean
  public FilterRegistrationBean siteMeshFilter() {
    FilterRegistrationBean filter = new FilterRegistrationBean();
    filter.setFilter(new SiteMeshFilter());
    return filter;
  }

  @Bean
  public ErrorPageFilter errorPageFilter() {
    return new ErrorPageFilter();
  }

  @Bean
  public FilterRegistrationBean disableSptringBootErrorFilter(ErrorPageFilter filter){
    FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
    filterRegistrationBean.setFilter(filter);
    filterRegistrationBean.setEnabled(false);
    return filterRegistrationBean;
  }
}
