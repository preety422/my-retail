package com.retail.orchestrator.model;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Price {

  private BigDecimal unitPrice;
  private BigDecimal unitUpchargePrice;
  private String currency;

}
