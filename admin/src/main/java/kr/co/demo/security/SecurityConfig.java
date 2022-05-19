package kr.co.demo.security;

import kr.co.demo.config.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.inject.Inject;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private SecLogoutSuccessHandler logoutSuccessHandler;

  @Inject
  private SecAjaxAuthenticationSuccessHandler secAjaxAuthenticationSuccessHandler;

  @Inject
  private SecAjaxAuthenticationFailureHandler secAjaxAuthenticationFailureHandler;

  @Inject
  private SecAuthenticationEntryPoint authenticationEntryPoint;

  @Autowired
  private UserDetailsService userDetailsService;

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(authenticationProvider());
  }

  //허용되어야 할 경로들
  @Override
  public void configure(WebSecurity web) throws Exception	{
    web.ignoring()
      .antMatchers("/assets/**")
      .antMatchers("/upload/**")
      .antMatchers("/font/**");
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .headers() // 헤더 보안
      .frameOptions().sameOrigin()
      .httpStrictTransportSecurity().disable()
    .and()
      .csrf().disable()
      .exceptionHandling()
      .authenticationEntryPoint(authenticationEntryPoint)
      .accessDeniedPage("/login")
    .and()
      .formLogin() //form 태그 기반의 로그인 설정
      .loginProcessingUrl("/authentication")
      .successHandler(secAjaxAuthenticationSuccessHandler)
      .failureHandler(secAjaxAuthenticationFailureHandler)
      .usernameParameter("id")
      .passwordParameter("pwd")
      .permitAll()
    .and() //로그아웃 설정
      .logout()
      .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
      .logoutSuccessHandler(logoutSuccessHandler)
      .permitAll()
    .and() // 페이지 인증, 비인증 설정
      .authorizeRequests()
        /** 인증 필요 */
      .antMatchers( "/admin/**" )
      .hasAnyAuthority(
          Constants.AuthoritiesConsf.ADMIN
      )
      /** 그 외는 인증 불필요 */
      .antMatchers("/**").permitAll();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    authenticationProvider.setUserDetailsService(userDetailsService);
    authenticationProvider.setPasswordEncoder(passwordEncoder());
    return authenticationProvider;
  }
}