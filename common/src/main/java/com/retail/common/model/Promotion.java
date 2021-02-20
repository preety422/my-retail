package com.retail.common.model;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Promotion {
  private String id;
  private String name;
  private BigDecimal amount;
  private double rate;
  private BigDecimal quantity;
}
