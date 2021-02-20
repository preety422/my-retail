package com.retail.catalogue.model;


import com.retail.common.constants.DatabaseConstants;
import com.retail.common.model.Promotion;
import com.retail.common.model.Tax;
import com.retail.common.util.JsonOutputFormatter;
import java.util.List;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Document(DatabaseConstants.ITEM)
public class Item {

  @Id
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