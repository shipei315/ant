package com.pine.ant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ErrorMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;

@Configuration
@SpringBootApplication
@EnableAutoConfiguration(exclude = {ErrorMvcAutoConfiguration.class})
public class AntApplication extends SpringBootServletInitializer{
	
	Logger logger=LoggerFactory.getLogger(AntApplication.class);
	
	public static void main(String[] args) {
		System.setProperty("spring.devtools.restart.enabled", "false");
		//拿到上下文对象，利用该上下文可以直接调用bean
		ConfigurableApplicationContext context=SpringApplication.run(AntApplication.class, args);
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(AntApplication.class);
	}
	
}
