package com.retail.catalogue.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

@Getter
@Setter
public class AppProperties {

  @Value("${spring.data.mongodb.username}")
  private String userName;

  @Value("${spring.data.mongodb.password}")
  private String password;

  @Value("${spring.data.mongodb.host}")
  private String host;

  @Value("${spring.data.mongodb.port}")
  private String port;

}