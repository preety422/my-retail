package com.retail.payment.exception;

public class NoPaymentOrderCreatedException extends Throwable {

  private String message;

  public NoPaymentOrderCreatedException(String message) {
    this.message = message;
  }
}
