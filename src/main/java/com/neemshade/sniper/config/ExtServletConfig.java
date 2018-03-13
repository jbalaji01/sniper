package com.neemshade.sniper.config;

import java.io.File;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;

import com.neemshade.sniper.service.ExtUploaderService;

@Configuration
public class ExtServletConfig {

	  @Bean
	  public ServletRegistrationBean dispatcherRegistration(DispatcherServlet dispatcherServlet) {
	    final ServletRegistrationBean registration = new ServletRegistrationBean(dispatcherServlet);
	    final String location = ExtUploaderService.ROOT_TEMP_DIR;
	    final long maxFileSize = 256 * 1024 * 1024;
	    final long maxRequestSize = maxFileSize;
	    final MultipartConfigElement multipartConfig  = new MultipartConfigElement(location, maxFileSize, maxRequestSize, 0);
	    registration.setMultipartConfig(multipartConfig);
	    
		// create root dir, if not exists
		// delete files in root dir
		File directory = new File(location);
		if (!directory.exists()) {
			directory.mkdirs();
		}
	 	    
	 	    
	    return registration;
	  }

}
