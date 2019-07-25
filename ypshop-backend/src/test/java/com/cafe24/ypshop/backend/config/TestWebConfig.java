package com.cafe24.ypshop.backend.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import com.cafe24.config.web.MessageConfig;
import com.cafe24.config.web.SwaggerConfig;
import com.cafe24.ypshop.backend.config.TestMVCConfig;

@Configuration
@EnableWebMvc
@EnableAspectJAutoProxy
@ComponentScan({"com.cafe24.ypshop.backend.controller", "com.cafe24.ypshop.backend.exception", "com.cafe24.ypshop.backend.controller.api"})
@Import({TestMVCConfig.class, MessageConfig.class, SwaggerConfig.class})
public class TestWebConfig {
	
}
