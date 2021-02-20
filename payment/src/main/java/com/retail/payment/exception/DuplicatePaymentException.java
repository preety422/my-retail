package com.retail.payment.exception;

public class DuplicatePaymentException extends Throwable {

  private String message;

  public DuplicatePaymentException(String message) {
    this.message = message;
  }
}
