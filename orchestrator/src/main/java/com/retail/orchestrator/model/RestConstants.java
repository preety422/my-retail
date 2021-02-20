package com.retail.orchestrator.model;

import lombok.experimental.UtilityClass;


@UtilityClass
public class RestConstants {

  public static final String ITEM_URI = "/catalogue/item";
  public static final String CREATE_PAYMENT_URI = "/payment/create";
  public static final String COMPLETE_PAYMENT_URI = "\"/payment/complete\"";
  public static final String ERR_404 = "404";
  public static final String EXTERNAL_SERVICE_PAYMENT = "Payment";
  public static final String UNAUTHORIZED = "Unauthorized";
  public static final String ERR_401 = "401";
  public static final String EXTERNAL_SERVICE_CATALOG = "Catalogue";
  public static final String OK_200 = "200";
}