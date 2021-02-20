package com.retail.orchestrator.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("rest-client")
public class RestClientProperties {

  @Value("${client.catalog.url}")
  private String catalogUrl;

  @Value("${client.payment.url}")
  private String paymentUrl;

  @Value("${client.profile.url}")
  private String profileUrl;

}
