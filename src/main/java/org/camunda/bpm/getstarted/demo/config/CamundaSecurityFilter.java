package org.camunda.bpm.getstarted.demo.config;

import javax.servlet.Filter;
import org.camunda.bpm.engine.rest.security.auth.ProcessEngineAuthenticationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CamundaSecurityFilter {

  @Bean
  public FilterRegistrationBean<Filter> processEngineAuthenticationFilter() {
    FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
    registration.setName("camunda-auth");
    registration.setFilter(getProcessEngineAuthenticationFilter());
    registration.addInitParameter("authentication-provider",
        "org.camunda.bpm.engine.rest.security.auth.impl.HttpBasicAuthenticationProvider");
    registration.addUrlPatterns("/engine-rest/*");
    registration.setOrder(1);
    return registration;
  }

  @Bean
  public Filter getProcessEngineAuthenticationFilter() {
    return new ProcessEngineAuthenticationFilter();
  }
}
