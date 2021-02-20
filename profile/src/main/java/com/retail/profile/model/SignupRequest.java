package com.retail.profile.model;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {

  @NotEmpty(message = "First Name must not be empty")
  private String firstName;

  private String lastName;

  @NotEmpty(message = "Password must not be empty")
  private String password;

  @NotNull(message = "Phone Number must not be empty")
  private Long phoneNumber;

  @NotEmpty(message = "Email must not be empty")
  @Email(message = "'${validatedValue}' must be a valid email")
  private String email;

  @Valid
  private Address address;
}
