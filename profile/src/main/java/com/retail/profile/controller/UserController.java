package com.retail.profile.controller;


import com.retail.common.model.ServiceRequest;
import com.retail.common.model.ServiceResponse;
import com.retail.profile.model.SigninRequest;
import com.retail.profile.model.SignupRequest;
import com.retail.profile.model.User;
import com.retail.profile.service.UserService;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
  private static final Logger LOGGER = LoggerFactory
      .getLogger(UserController.class);
  @Autowired
  private UserService userService;


  @PostMapping(value = "/signup", produces = "application/json")
  @ResponseBody
  public ServiceResponse<User> signUp(
      @Valid @RequestBody ServiceRequest<SignupRequest> signupRequest) {
    long startTime = System.currentTimeMillis();
    ServiceResponse<User> userServiceResponse = userService.signUp(signupRequest);
    LOGGER.info("Sign up response time: {}", System.currentTimeMillis() - startTime);
    return userServiceResponse;
  }

  @PostMapping(value = "/signin", produces = "application/json")
  @ResponseBody
  public ServiceResponse<User> signInn(
      @Valid @RequestBody ServiceRequest<SigninRequest> signinRequest) {
    long startTime = System.currentTimeMillis();
    ServiceResponse<User> userServiceResponse = userService.signIn(signinRequest);
    LOGGER.info("Sign in response time: {}", System.currentTimeMillis() - startTime);
    return userServiceResponse;
  }
}
