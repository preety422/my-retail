package com.retail.catalogue.model;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
public class ItemInventory {

  @Id
  private String id;
  private BigDecimal totalQuantity;
  private BigDecimal availableQuantity;
  private BigDecimal reservedQuantity;
  private String itemId;
  private String upcId;
  private String storeNumber;
}
