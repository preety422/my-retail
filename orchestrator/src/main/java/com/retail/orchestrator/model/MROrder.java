package com.retail.orchestrator.model;


import com.retail.common.constants.DatabaseConstants;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document(DatabaseConstants.MR_ORDER)
public class MROrder {
  @Id
  private String id;

  private List<Item> items;

  private List<MRPayment> payments;

  private Date createDate;

  private Date updateDate;

  private String customerProfileId;

}
