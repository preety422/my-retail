package com.retail.orchestrator.service;

import com.retail.common.model.UserDetails;
import com.retail.common.util.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
  Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

  @Autowired
  private JwtTokenUtil jwtTokenUtil;

  public boolean isValidUser(String bearerToken, String userId){
    if (null == bearerToken || bearerToken.length() < 8) {
      return false;
    }
    String token = bearerToken.trim().split(" ")[1];
    LOGGER.info("Token: {} ", token);
    return jwtTokenUtil.validateToken(token, new UserDetails(userId));
  }
}
