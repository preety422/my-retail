package com.retail.catalogue.service;

import com.retail.catalogue.exception.ItemNotAvailableException;
import com.retail.catalogue.model.Item;
import com.retail.catalogue.model.ItemInventory;
import com.retail.catalogue.model.ItemReservation;
import com.retail.catalogue.repository.ItemInventoryRepository;
import com.retail.catalogue.repository.ItemRepository;
import com.retail.catalogue.repository.ItemReservationRepository;
import com.retail.common.model.Error;
import com.retail.common.model.ItemRequest;
import com.retail.common.model.ItemResponse;
import com.retail.common.model.Promotion;
import com.retail.common.model.ServiceRequest;
import com.retail.common.model.ServiceResponse;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CatalogueService {

  Logger LOGGER = LoggerFactory.getLogger(CatalogueService.class);

  @Autowired
  private ItemRepository itemRespository;

  @Autowired
  private ItemReservationRepository itemReservationRepository;

  @Autowired
  private ItemInventoryRepository itemInventoryRepository;

  public ServiceResponse<ItemResponse> getItemDetails(ServiceRequest<ItemRequest> itemRequest) {
    ServiceResponse<ItemResponse> serviceResponse = new ServiceResponse<>();
    ItemResponse itemResponse = new ItemResponse();
    ItemRequest request = itemRequest.getPayload();
    List<Error> errors = null;
    serviceResponse.setPayload(itemResponse);
    try {
      ItemInventory itemInventory = itemInventoryRepository
          .findItemByUpcIdOrItemId(request.getBarcode(), request.getBarcode());
      if (null != itemInventory
          && itemInventory.getAvailableQuantity().subtract(request.getQuantity()).doubleValue()
          < 0) {
        throw new ItemNotAvailableException("Requested item is not available");
      }
      Item item = itemRespository
          .findItemByUpcBarcodeOrItemBarcode(request.getBarcode(), request.getBarcode());
      ItemReservation itemReservation = prepareItemReservation(request, item, itemInventory);
      itemReservation = itemReservationRepository.save(itemReservation);
      ItemInventory itemInventoryUpdate = prepapareInventoryToUpdate(itemInventory, request);
      itemInventoryRepository.save(itemInventoryUpdate);
      itemResponse.setTaxes(item.getTaxes());
      List<Promotion> applicablePromotions = getApplicablePromotions(request, item);
      prepareItemResponse(itemResponse, item, applicablePromotions, request, itemInventory,
          itemReservation);
    } catch (ItemNotAvailableException e) {
      LOGGER.error("CatalogueService::Error in get Item details: ", e);
      errors = new ArrayList<>();
      Error error = new Error("404", e.getMessage());
      errors.add(error);
      serviceResponse.setErrors(errors);
      serviceResponse.setStatus("404");
    }
    return serviceResponse;
  }

  private ItemInventory prepapareInventoryToUpdate(
      ItemInventory itemInventory, ItemRequest request) {
    ItemInventory itemInventoryUpdate = new ItemInventory();
    itemInventoryUpdate.setId(itemInventory.getId());
    itemInventoryUpdate.setAvailableQuantity(
        itemInventory.getAvailableQuantity().subtract(request.getQuantity()));
    itemInventoryUpdate.setReservedQuantity(
        itemInventory.getReservedQuantity().add(request.getQuantity()));
    itemInventoryUpdate.setItemId(itemInventory.getItemId());
    itemInventoryUpdate.setStoreNumber(itemInventory.getStoreNumber());
    itemInventoryUpdate.setUpcId(itemInventory.getUpcId());
    itemInventoryUpdate.setTotalQuantity(itemInventory.getTotalQuantity());
    return itemInventoryUpdate;
  }

  private ItemReservation prepareItemReservation(ItemRequest request,
      Item item, ItemInventory itemInventory) {
    ItemReservation itemReservation = new ItemReservation();
    itemReservation.setInventoryId(itemInventory.getId());
    itemReservation.setItemId(item.getItemBarcode());
    itemReservation.setUpcId(item.getUpcBarcode());
    itemReservation.setQuantity(request.getQuantity());
    itemReservation.setStatus(1);
    return itemReservation;
  }

  private List<Promotion> getApplicablePromotions(ItemRequest request,
      Item item) {
    List<Promotion> applicablePromotions = new ArrayList<>();
    for (Promotion p : item.getPromotions()) {
      if (p.getQuantity().doubleValue() >= request.getQuantity().doubleValue()) {
        applicablePromotions.add(p);
      }
    }
    if (!applicablePromotions.isEmpty()) {
      applicablePromotions.sort((a, b) -> b.getQuantity().subtract(a.getQuantity()).intValue());
    }
    return applicablePromotions;
  }

  private void prepareItemResponse(ItemResponse itemResponse, Item item,
      List<Promotion> applicablePromotions, ItemRequest request,
      ItemInventory itemInventory, ItemReservation itemReservation) {
    itemResponse.setPromotions(applicablePromotions);
    itemResponse.setCurrency(item.getPrice().getCurrency());
    itemResponse.setUpcBarcode(item.getUpcBarcode());
    itemResponse.setItemBarcode(item.getItemBarcode());
    itemResponse.setUnitPrice(item.getPrice().getUnitPrice());
    itemResponse.setUnitUpchargePrice(item.getPrice().getUnitUpchargePrice());
    itemResponse.setExtendedPrice(item.getPrice().getUnitPrice().multiply(request.getQuantity()));
    itemResponse.setExtendedUpchargePrice(
        item.getPrice().getUnitUpchargePrice().multiply(request.getQuantity()));
    itemResponse.setInventoryId(itemInventory.getId());
    itemResponse.setQuantity(request.getQuantity());
    itemResponse.setReservationId(itemReservation.getId());
  }


}
