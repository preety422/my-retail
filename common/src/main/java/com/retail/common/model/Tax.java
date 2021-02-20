package com.retail.common.model;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Tax {

  private String typeCode;
  private String name;
  private double rate;

}
