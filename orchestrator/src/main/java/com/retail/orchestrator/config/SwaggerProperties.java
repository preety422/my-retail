package com.retail.orchestrator.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("swagger")
public class SwaggerProperties {

  @Value("${swagger.title}")
  private String title;

  @Value("${swagger.description}")
  private String description;

  @Value("${swagger.version}")
  private String version;

  @Value("${swagger.license}")
  private String license;

  @Value("${swagger.contact-name}")
  private String contactName;

  @Value("${swagger.contact-email}")
  private String contactEmail;

}
