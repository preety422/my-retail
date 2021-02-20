package com.retail.catalogue.controller;

import com.retail.catalogue.model.Item;
import com.retail.catalogue.model.ItemInventory;
import com.retail.catalogue.repository.ItemInventoryRepository;
import com.retail.catalogue.repository.ItemRepository;
import com.retail.common.model.ServiceRequest;
import com.retail.common.model.ServiceResponse;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/item")
public class ItemController {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(com.retail.catalogue.controller.CatalogueController.class);

  @Autowired
  private ItemRepository itemRepository;

  @Autowired
  private ItemInventoryRepository itemInventoryRepository;

  @PostMapping(value = "", produces = "application/json")
  @ResponseBody
  public ServiceResponse<Item> createItem(
      @Valid @RequestBody ServiceRequest<Item> itemRequest) {
    long startTime = System.currentTimeMillis();
    Item item = itemRepository.save(itemRequest.getPayload());

    LOGGER.info("Get Item details response time: {}", System.currentTimeMillis() - startTime);
    return new ServiceResponse(item, null, null, "200");
  }

  @PostMapping(value = "/inventory", produces = "application/json")
  @ResponseBody
  public ServiceResponse<ItemInventory> createItemInventory(
      @Valid @RequestBody ServiceRequest<ItemInventory> inventoryServiceRequest) {
    long startTime = System.currentTimeMillis();
    ItemInventory itemInventory = itemInventoryRepository
        .save(inventoryServiceRequest.getPayload());

    LOGGER.info("Get Item details response time: {}", System.currentTimeMillis() - startTime);
    return new ServiceResponse(itemInventory, null, null, "200");
  }


}
