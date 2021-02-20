package com.retail.orchestrator.model;


import com.retail.common.model.Promotion;
import com.retail.common.model.Tax;
import com.retail.common.util.JsonOutputFormatter;
import java.util.List;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Item {

  private String id;

  private String itemBarcode;

  private String upcBarcode;

  private Price price;

  private List<Tax> taxes;

  private List<Promotion> promotions;

  private String unitOfMeasure;

  @Override
  public String toString() {
    return JsonOutputFormatter.generateJson(this);
  }
}