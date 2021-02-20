package com.retail.payment;

import com.retail.payment.config.SpringConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

//@SpringBootApplication
@ConfigurationPropertiesScan("com.retail.payment.config")
@EnableAutoConfiguration
public class PaymentApplication extends SpringBootServletInitializer {

  public static void main(String[] args) {
    SpringApplication.run(PaymentApplication.class, args);
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(PaymentApplication.class, SpringConfig.class);
  }

}
