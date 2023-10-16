package com.cos.security1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@SuppressWarnings("deprecation")
@Configuration // IoC 빈(bean)을 등록
@EnableWebSecurity // 필터 체인 관리 시작 어노테이션
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true) 
//secured 어노테이션 활성화, preAuthorize, postAuthorize 어노테이션 활성화
// 특정 주소 접근시 권한 및 인증을 위한 어노테이션 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	//해당 메서드의 리턴되는 오브젝트를 IoC로 등록해준다.
	@Bean
	public BCryptPasswordEncoder encodePwd() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests()
			.antMatchers("/user/**").authenticated() //인증만 되면 들어갈 수 있는 주소 !!
			//.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
			.antMatchers("/manager/**").access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
			.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
			.anyRequest().permitAll()
		.and()
			.formLogin()
			.loginPage("/loginForm")
			.loginProcessingUrl("/login") //login 주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인을 진행해줍니다.
			.defaultSuccessUrl("/") //login후 가고싶은 주소
		.and()
		.oauth2Login()
		.loginPage("/loginForm"); //구글 로그인이 완료된 뒤의 후처리가 필요함.
		
	}
}




