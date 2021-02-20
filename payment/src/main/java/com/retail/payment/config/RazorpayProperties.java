package com.retail.payment.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("razorpay")
public class RazorpayProperties {

  @Value("${razorpay.key}")
  private String key;

  @Value("${razorpay.secret}")
  private String secret;
}
