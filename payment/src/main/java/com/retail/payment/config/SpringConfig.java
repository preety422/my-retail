package com.retail.payment.config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

  @Autowired
  private AppProperties appProperties;

  @Autowired
  private RazorpayProperties razorpayProperties;

  @Bean
  public DataSource getDataSource() {
    DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
    dataSourceBuilder.username(appProperties.getUserName());
    dataSourceBuilder.password(appProperties.getPassword());
    dataSourceBuilder.url(appProperties.getJdbcUrl());
    return dataSourceBuilder.build();
  }

  @Bean("appProperties")
  @ConfigurationProperties(prefix = "spring")
  public AppProperties appProperties() {
    return new AppProperties();
  }

}