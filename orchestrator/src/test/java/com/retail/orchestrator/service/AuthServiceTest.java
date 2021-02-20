package com.retail.orchestrator.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import com.retail.common.util.JwtTokenUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


@RunWith(PowerMockRunner.class)
@PrepareForTest({AuthService.class})
public class AuthServiceTest {

  @Mock
  private JwtTokenUtil jwtTokenUtil;

  @InjectMocks
  private AuthService authService;

  @Test
  public void isValidUserTest() {

    Mockito.when(jwtTokenUtil.validateToken(anyString(), any()))
        .thenReturn(true);
    boolean isvalid = authService.isValidUser("bearer token", "user");

    assertTrue(isvalid);
  }

  @Test
  public void isValidUserMalformedTest() {

    Mockito.when(jwtTokenUtil.validateToken(anyString(), any()))
        .thenReturn(true);
    boolean isvalid = authService.isValidUser("bearer ", "user");

    assertFalse(isvalid);
  }

  @Test
  public void isValidUserExpiredTest() {

    Mockito.when(jwtTokenUtil.validateToken(anyString(), any()))
        .thenReturn(false);
    boolean isvalid = authService.isValidUser("bearer token", "user");

    assertFalse(isvalid);
  }

}

