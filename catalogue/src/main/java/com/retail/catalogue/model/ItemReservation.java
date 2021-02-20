package com.retail.catalogue.model;

import com.retail.common.constants.DatabaseConstants;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(DatabaseConstants.ITEM_RESERVATION)
@Getter
@Setter
public class ItemReservation {

  @Id
  private String id;
  private String itemId;
  private String upcId;
  private String inventoryId;
  private int status; // 1-> booked, 2-> completed, 0 -> cancelled/available
  private BigDecimal quantity;

}
