package com.cafe24.ypshop.backend.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.cafe24.config.web.FileUploadConfig;
import com.cafe24.config.web.MVCConfig;
import com.cafe24.config.web.MessageConfig;
import com.cafe24.config.web.SwaggerConfig;

@Configuration
@EnableWebMvc
@EnableAspectJAutoProxy
@ComponentScan({"com.cafe24.ypshop.backend.controller", "com.cafe24.ypshop.backend.exception", "com.cafe24.ypshop.backend.controller.api", "com.cafe24.ypshop.backend.repository"})
@Import({MVCConfig.class, MessageConfig.class, SwaggerConfig.class, FileUploadConfig.class})
public class WebConfig {
	
}
