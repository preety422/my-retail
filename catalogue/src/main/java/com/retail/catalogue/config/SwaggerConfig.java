package com.retail.catalogue.config;

import static springfox.documentation.builders.PathSelectors.regex;

import com.retail.common.constants.SwaggerConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurationSupport {

  @Autowired
  private SwaggerProperties swaggerProperties;

  @Bean
  public Docket singleProfileAPI() {
    return new Docket(DocumentationType.SWAGGER_2).select()
        .apis(RequestHandlerSelectors.basePackage(SwaggerConstants.CATALOG_BASE_PACKAGE))
        .paths(regex("/.*"))
        .build().apiInfo(metaData());
  }

  private ApiInfo metaData() {
    return new ApiInfoBuilder().title(swaggerProperties.getTitle())
        .description(swaggerProperties.getDescription()).version(swaggerProperties.getVersion())
        .license(swaggerProperties.getLicense())
        .contact(new Contact(swaggerProperties.getContactName(), swaggerProperties.getContactName(),
            swaggerProperties.getContactEmail())).build();
  }

  @Override
  protected void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("swagger-ui.html")
        .addResourceLocations("classpath:/META-INF/resources/");
    registry.addResourceHandler("/webjars/**")
        .addResourceLocations("classpath:/META-INF/resources/webjars/");
  }

}