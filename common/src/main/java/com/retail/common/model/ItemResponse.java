package com.retail.common.model;


import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemResponse extends MyRetailResponse {

  private String itemBarcode;
  private String upcBarcode;
  private BigDecimal unitPrice;
  private BigDecimal unitUpchargePrice;
  private BigDecimal extendedPrice;
  private BigDecimal extendedUpchargePrice;
  private String Currency;
  private List<Tax> taxes;
  private List<Promotion> promotions;
  private String unitOfMeasure;
  private BigDecimal quantity;
  private String reservationId;
  private String inventoryId;

}
