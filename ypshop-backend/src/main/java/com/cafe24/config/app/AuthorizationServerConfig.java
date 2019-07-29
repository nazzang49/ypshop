package com.cafe24.config.app;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

//client_id 저장, accessToken 영속화 담당
//토큰이 만료되기 전까지 동일한 client, accessToken O >> 자원 이용 가능
@EnableAuthorizationServer
@Configuration
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private DataSource dataSource;
	
	//accessToken 저장 >> DB
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//		super.configure(endpoints);
		//스키마 >> 주입 시, 자동 생성
		endpoints.tokenStore(new JdbcTokenStore(dataSource))
			.authenticationManager(authenticationManager);
	}
	
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		super.configure(security);
	}
	
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//		super.configure(clients);
		clients.jdbc(dataSource);
	}
	
}
