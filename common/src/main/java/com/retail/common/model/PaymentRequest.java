package com.retail.common.model;

import com.retail.common.util.JsonOutputFormatter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequest {

  private Double amount;

  private String paymentMethod;

  private String receiptId;

  private String orderId;

  private String paymentId;

  @Override
  public String toString() {
    return JsonOutputFormatter.generateJson(this);
  }
}
