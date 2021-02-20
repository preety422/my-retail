package com.retail.profile.model;

import com.retail.common.constants.DatabaseConstants;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Entity
@Table(name = DatabaseConstants.USER)
public class User {

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  private String id;

  @Setter
  @Column(name = "first_name")
  private String firstName;

  @Setter
  @Column(name = "last_name")
  private String lastName;

  @Setter
  @Column(name = "password")
  private String password;

  @Setter
  @Column(name = "phone_number")
  private Long phoneNumber;

  @Setter
  @Column(name = "email")
  private String email;

  @Setter
  @Column(name = "address")
  private String addressId;

  @Setter
  @Column(name = "accessToken")
  private String jwtToken;

}

