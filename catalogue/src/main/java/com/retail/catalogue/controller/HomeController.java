package com.retail.catalogue.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

  /**
   * @return static String to indicate the application is running
   */

  @GetMapping("/health-check")
  @ResponseBody
  public String healthCheck() {
    return "Catalog is alive! OK 200";
  }

  @GetMapping("")
  @ResponseBody
  public String health() {
    return "OK 200";
  }
}
