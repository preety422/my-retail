package com.retail.profile;

import com.retail.profile.config.SpringConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


@SpringBootApplication(scanBasePackages = {"com.retail.profile", "com.retail.common"})
@ConfigurationPropertiesScan("com.retail.profile.config")
public class ProfileApplication extends SpringBootServletInitializer {

  public static void main(String[] args) {
    SpringApplication.run(ProfileApplication.class, args);
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(ProfileApplication.class, SpringConfig.class);
  }

}
