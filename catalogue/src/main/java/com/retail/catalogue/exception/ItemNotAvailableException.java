package com.retail.catalogue.exception;

public class ItemNotAvailableException extends Throwable {

  private String message;
  public ItemNotAvailableException(String message) {
    this.message = message;
  }
}
