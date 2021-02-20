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
@Table(name = DatabaseConstants.ADDRESS)
public class Address {

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  private String id;

  @Setter
  @Column(name = "line1")
  private String line1;

  @Setter
  @Column(name = "line2")
  private String line2;

  @Setter
  @Column(name = "city")
  private String city;

  @Setter
  @Column(name = "state")
  private String state;

  @Setter
  @Column(name = "zip")
  private String zip;

  @Setter
  @Column(name = "phone")
  private String phone;

}
