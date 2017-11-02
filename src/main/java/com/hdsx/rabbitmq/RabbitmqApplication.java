package com.hdsx.rabbitmq;

import com.hdsx.rabbitmq.config.CrossDomainFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RabbitmqApplication {

	/**
	 * 支持跨域
	 * @return
	 */
	@Bean
	public FilterRegistrationBean addCrossDomainFilter() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean(new CrossDomainFilter());
		registrationBean.addUrlPatterns("/*");
		return registrationBean;
	}
	public static void main(String[] args) {
		SpringApplication.run(RabbitmqApplication.class, args);
	}
}
