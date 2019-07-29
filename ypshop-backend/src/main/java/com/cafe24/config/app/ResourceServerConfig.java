package com.cafe24.config.app;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@EnableResourceServer
@Configuration
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		//http.headers().frameOptions().disable();

		http
			.authorizeRequests()
			//고객_회원
			.antMatchers("/api/member/update").access("#oauth2.hasScope('USER')")
			.antMatchers("/api/member/delete/**").access("#oauth2.hasScope('USER')")
			.antMatchers("/api/member/info/**").access("#oauth2.hasScope('USER')")
			//관리자_카테고리
			.antMatchers("/api/category/info/**").access("#oauth2.hasScope('ADMIN')")
			.anyRequest()
			.permitAll();
		
	}
}
