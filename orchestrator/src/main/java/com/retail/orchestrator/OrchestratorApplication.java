package com.retail.orchestrator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


@SpringBootApplication(scanBasePackages = {"com.retail.orchestrator", "com.retail.common"})
@ConfigurationPropertiesScan("com.retail.orchestrator.config")
public class OrchestratorApplication extends SpringBootServletInitializer {

  public static void main(String[] args) {
    SpringApplication.run(OrchestratorApplication.class, args);
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(OrchestratorApplication.class);
  }

}