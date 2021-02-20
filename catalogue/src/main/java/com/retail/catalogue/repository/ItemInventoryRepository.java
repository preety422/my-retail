package com.retail.catalogue.repository;

import com.retail.catalogue.model.ItemInventory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemInventoryRepository extends MongoRepository<ItemInventory, String> {

  ItemInventory findItemByUpcIdOrItemId(String upcBarcode, String itemBarcode);
}