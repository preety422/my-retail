package com.retail.catalogue.model.enums;

public enum TaxType {
  VAT(1), STATE(2), MUNICIPAL(3);

  private int code;

  TaxType(int code) {
    this.code = code;
  }
}
