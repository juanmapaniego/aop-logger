package com.jmpaniego.aoplogger.around;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class LogAroundConfiguration {

  @Bean
  public LogAroundAdvice logAroundAdvice() {
    return new LogAroundAdvice();
  }

  @Bean
  public LogAroundService logAroundService() {
    return new LogAroundService();
  }
}
