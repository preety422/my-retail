package com.retail.payment.entity;

import com.retail.common.constants.DatabaseConstants;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Entity
@Table(name = DatabaseConstants.MR_PAYMENT)
public class  MRPayment {

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  private String id;

  @Setter
  @Column(name = "order_id")
  private String orderId;

  @Setter
  @Column(name = "receipt_id")
  private String receiptId;

  @Setter
  @Column(name = "payment_id")
  private String paymentId;

  @Setter
  @Column(name = "amount")
  private BigDecimal amount;

  @Setter
  @Column(name = "payment_method")
  private String paymentMethod;

  @Setter
  @Column(name = "transaction_status")
  private String transactionStatus;

  @Setter
  @Column(name = "fraud_status")
  private String fraudStatus;

  @Setter
  @Column(name = "currency")
  private String currency;

  @Setter
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "operation_date")
  private Date operationDate;

  @Setter
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "order_created_date")
  private Date orderCreatedDate;

  @Setter
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "created_date")
  private Date createdDate;

  @Setter
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "updated_date")
  private Date updatedDate;
}
