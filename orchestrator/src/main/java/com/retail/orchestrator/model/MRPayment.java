package com.retail.orchestrator.model;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MRPayment {

  private String orderId;

  private String receiptId;

  private String paymentId;

  private BigDecimal amount;

  private String paymentMethod;

  private String transactionStatus;

  private String fraudStatus;

  private String currency;

  private Date operationDate;

  private Date orderCreatedDate;

  private Date createdDate;

  private Date updatedDate;
}
