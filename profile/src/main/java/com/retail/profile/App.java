package com.retail.profile;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class App {
  private static BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
  public static void main(String[] args) {

    boolean isPasswordMatch = bCryptPasswordEncoder.matches("preety", "$2a$10$MjYE1.ubTdJ4z1XouUBM.OFmcrx0w2bzNAfsXleFJGIJ1ztcj5blu");
    System.out.println(isPasswordMatch);
  }

}
