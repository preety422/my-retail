package com.retail.payment.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

@Getter
@Setter
public class AppProperties {

  @Value("${spring.datasource.username}")
  private String userName;

  @Value("${spring.datasource.username}")
  private String password;

  @Value("${spring.datasource.url}")
  private String jdbcUrl;


}