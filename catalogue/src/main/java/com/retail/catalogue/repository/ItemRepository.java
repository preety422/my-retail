package com.retail.catalogue.repository;

import com.retail.catalogue.model.Item;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ItemRepository extends MongoRepository<Item, String> {

  Item findItemByUpcBarcodeOrItemBarcode(String upcBarcode, String itemBarcode);
}