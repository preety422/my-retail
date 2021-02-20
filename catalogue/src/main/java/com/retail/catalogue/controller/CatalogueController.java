package com.retail.catalogue.controller;

import com.retail.common.model.ItemRequest;
import com.retail.common.model.ItemResponse;
import com.retail.catalogue.service.CatalogueService;
import com.retail.common.model.ServiceRequest;
import com.retail.common.model.ServiceResponse;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/catalogue")
public class CatalogueController {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(CatalogueController.class);

  @Autowired
  private CatalogueService catalogueService;

  @PostMapping(value = "/item", produces = "application/json")
  @ResponseBody
  public ServiceResponse<ItemResponse> itemDetails(
      @Valid @RequestBody ServiceRequest<ItemRequest> itemRequest) {
    long startTime = System.currentTimeMillis();
    ServiceResponse<ItemResponse> itemDetails = catalogueService.getItemDetails(itemRequest);

    LOGGER.info("Get Item details response time: {}", System.currentTimeMillis() - startTime);
    return itemDetails;
  }


}


