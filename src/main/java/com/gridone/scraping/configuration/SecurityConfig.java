package com.gridone.scraping.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;

import com.gridone.scraping.service.UserService;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private LoginAuthenticationSuccessHandler successHandler;
	
	@Autowired
	private LoginAuthenticationFailureHandler failedHandler;
	
	@Autowired
	UserService userService;
	
//	@Autowired
//	CustomAuthenticationProvider customAuthenticationProvider;

	// 암호화에 필요한 PasswordEncoder 를 Bean 등록합니다.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    // authenticationManager를 Bean 등록합니다.
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    
    // 권한 인증 절차(로그인)를 커스텀 할 수 있는 customAuthenticationProvider Bean을 등록합니다.
//    @Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.authenticationProvider(customAuthenticationProvider);
//	}
    
    // 권한인증 제외목록을 설정합니다.
    @Override
	public void configure(WebSecurity web) throws Exception{
		web.ignoring()
		.antMatchers("/css/**", "/fonts/**", "/images/**", "/js/**", "/chartJS/**", "/fontawesome/**");
	}

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
//        		.headers()
//        		.frameOptions().sameOrigin() // 동일 도메인에서는 iframe(X-Frame-Options) 랜더링 가능하도록  설정
//        		.httpStrictTransportSecurity().disable() // HTTPS Protocol 로만 접속하게 하지 않음(http도 지원)
//        		.and()
                .csrf().disable()
                .authorizeRequests() // 요청에 대한 사용권한 체크
                .antMatchers("/admin/**").hasAuthority("ADMIN")
                .antMatchers("/user/**").hasAuthority("USER")
                .antMatchers("/login","/register","/logout", "/join", "/testtest1").permitAll()
        		.antMatchers("/**").authenticated()
//                .anyRequest().permitAll() // 그외 나머지 요청은 누구나 접근 가능
                .and().formLogin()
					.loginPage("/login")
					.usernameParameter("email")
					.passwordParameter("password")
					.successHandler(successHandler)
					.failureHandler(failedHandler)
					.permitAll()
				.and().logout()
					.logoutUrl("/logout")
	            	.invalidateHttpSession(true) /*로그아웃시 세션 제거*/ 
	            	.deleteCookies("JSESSIONID") /*쿠키 제거*/ 
	            	.clearAuthentication(true) /*권한정보 제거*/
					.permitAll()
				.and()
					.exceptionHandling()
					.accessDeniedHandler(accessDeniedHandler()); // 인증실패 핸들링 : 여기서 각 경우에 따른 접근실패 페이지 설정이 가능하다. 따라서
        
        http.sessionManagement()
		.sessionAuthenticationErrorUrl("/logout")
    	.maximumSessions(1) /* session 허용 갯수 */ 
		.expiredUrl("/login") // 세션 만료 시  이동할 페이지
    	.maxSessionsPreventsLogin(false) /* 동일한 사용자 로그인시 x, false 일 경우 기존 사용자 session 종료*/
		.sessionRegistry(sessionRegistry());
    }
    
    @Bean
	public AccessDeniedHandler accessDeniedHandler(){
	    return new AccessDeniedHandlerImpl();
	}
    
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
    
    @Bean
    public SpringSecurityDialect springSecurityDialect(){
        return new SpringSecurityDialect();
    }
}
