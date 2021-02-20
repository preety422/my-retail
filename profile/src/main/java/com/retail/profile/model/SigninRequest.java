package com.retail.profile.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SigninRequest {

  private String email;
  private String phoneNumber;
  private String password;

}
