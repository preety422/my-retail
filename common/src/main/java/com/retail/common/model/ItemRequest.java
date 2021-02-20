package com.retail.common.model;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemRequest {

  private String barcode;
  private String store;
  private BigDecimal quantity;
}
