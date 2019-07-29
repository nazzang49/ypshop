package com.cafe24.ypshop.backend.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import com.cafe24.config.app.DBConfig;
import com.cafe24.config.app.MyBatisConfig;
import com.cafe24.config.app.ResourceServerConfig;
import com.cafe24.config.app.AppSecurityConfig;
import com.cafe24.config.app.AuthorizationServerConfig;

@Configuration
@EnableAspectJAutoProxy
@Import({DBConfig.class, MyBatisConfig.class, AppSecurityConfig.class, AuthorizationServerConfig.class, ResourceServerConfig.class})
@ComponentScan({"com.cafe24.ypshop.backend.repository", "com.cafe24.ypshop.backend.service", "com.cafe24.ypshop.backend.security"})
public class AppConfig {
	
}
