package com.retail.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.retail.common.util.JsonOutputFormatter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentResponse extends MyRetailResponse{

  private String orderId;
  private String receiptId;
  private String paymentId;
  private String paymentMethod;
  private BigDecimal amount;
  private String currency;
  private String fraudStatus;
  private String transactionStatus;

  @Ignore
  private Date transactionDate;


  @Override
  public String toString() {
    return JsonOutputFormatter.generateJson(this);
  }
}
