package kr.co.demo;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.support.ErrorPageFilter;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

@SpringBootApplication
/**
 * ComponentScan 이란?
 * @Component 라는 어노테이션이 붙어 있는 class를 자동으로 Scan 하여 Bean으로 등록해주는 역활을 하는 어노테이션.
 * (@Compoent, @Configuration, @Repository, @Service, @Controller, @RestController ..)
 * */
@ComponentScan({"kr.co"})
/**
 * EnableAutoConfiguration 이란?
 * @EnableAutoConfiguration은 스프링부트의 meta 파일을 읽어서, 미리 정의되어 있는 자바 설정 파일(@Configuration)들을 빈으로 등록하는 역할을 수행한다.
 * spring.factories 파일에 자동으로 가저올 Bean 들이 정의
 * exclude와 excludeName이라는 2가지 속성
 * 만약 자동 설정을 하고 싶지 않은 클래스가 있다면 이 속성들을 사용하여 자동설정에서 제외할 수 있다
 * -----------------------------------------------------------------------
 * DataSourceAutoConfiguration.class = datasource 기본 체크 제외
 * */
@EnableAutoConfiguration
/**
 * SpringBootServletInitializer 이란?
 * WAR로 배포할 때 상속
 *  Tomcat 같은 Servlet Container 환경에서 Spring Boot 애플리케이션 동작 가능 하도록 웹 애플리케이션 컨텍스트 구성 한다는 의미
 * */
public class DemoApplication extends SpringBootServletInitializer {

    /**
     * Spring Framework는 Servlet 3.0 이상 환경에서 web.xml 대신하여 ServletContext를 프로그래밍적으로 다룰 수 있게 WebApplicationInitializer 인터페이스를 제공
     * */
    @Override
    public void onStartup(final ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(DemoApplication.class);
    }

    /**
     *  errorPageFilter 미설정시 기본 spring boot 제공 whitelabel Error page 이동
     *  spring boot 2.1 부터 bean overriding 불가능 ErrorPageFilter exception 발생
     *  application.yml 추가
     *  --------------------------------------------------------------------------
     *  spring:
     *    main:
     *      allow-bean-definition-overriding: true
     *  --------------------------------------------------------------------------
     * */
    @Bean
    public ErrorPageFilter errorPageFilter() {
        return new ErrorPageFilter();
    }

    @Bean
    public FilterRegistrationBean disableSpringBootErrorFilter(ErrorPageFilter filter) {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(filter);
        filterRegistrationBean.setEnabled(false);
        return filterRegistrationBean;
    }

}
