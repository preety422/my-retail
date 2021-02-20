package com.retail.profile.service;

import com.retail.common.model.Error;
import com.retail.common.model.ServiceRequest;
import com.retail.common.model.ServiceResponse;
import com.retail.common.model.UserDetails;
import com.retail.common.util.JwtTokenUtil;
import com.retail.profile.model.Address;
import com.retail.profile.model.SigninRequest;
import com.retail.profile.model.SignupRequest;
import com.retail.profile.model.User;
import com.retail.profile.repository.AddressRepository;
import com.retail.profile.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(UserService.class);

  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private AddressRepository addressRepository;

  private JwtTokenUtil jwtTokenUtil;

  @Autowired
  UserService(JwtTokenUtil jwtTokenUtil) {
    this.jwtTokenUtil = jwtTokenUtil;
    this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
  }

  public ServiceResponse<User> signUp(ServiceRequest<SignupRequest> signupRequest) {
    ServiceResponse<User> response = new ServiceResponse<>();
    SignupRequest request = signupRequest.getPayload();
    try {
      User user = new User();
      user.setFirstName(request.getFirstName());
      user.setLastName(request.getLastName());
      user.setPhoneNumber(request.getPhoneNumber());
      user.setEmail(request.getEmail());
      Address address = request.getAddress();
      address = addressRepository.save(address);
      user.setAddressId(address.getId());
      user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
      userRepository.save(user);
      user.setJwtToken(jwtTokenUtil.generateToken(new UserDetails(user.getId())));
      response.setPayload(user);
    } catch (Exception e) {
      LOGGER.error("Error while signup: ", e);
      prepareBadRequestResponse(response, e.getMessage());
    }
    return response;
  }

  public ServiceResponse<User> signIn(ServiceRequest<SigninRequest> signinRequest) {
    ServiceResponse<User> response = new ServiceResponse<>();
    SigninRequest request = signinRequest.getPayload();
    try {
      User user = userRepository.findUserByEmail(request.getEmail());
      boolean isLoginSuccess = (bCryptPasswordEncoder.matches(request.getPassword(), user.getPassword()));
      if (isLoginSuccess) {
        user.setJwtToken(jwtTokenUtil.generateToken(new UserDetails(user.getId())));
      } else {
        throw new Exception("Login Failed! Invalid credential.");
      }
      user.setPassword(null);
      response.setPayload(user);
    } catch (Exception e) {
      LOGGER.error("Error while signin: ", e);
      prepareBadRequestResponse(response, e.getMessage()!= null ? e.getMessage() : e.getLocalizedMessage());
    }
    return response;
  }

  private void prepareBadRequestResponse(ServiceResponse<User> response, String errorMessage) {
    List<Error> errors = new ArrayList<>();
    Error error = new Error("400", errorMessage);
    errors.add(error);
    response.setErrors(errors);
    response.setStatus("400");
  }
}
