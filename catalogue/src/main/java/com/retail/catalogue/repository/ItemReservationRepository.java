package com.retail.catalogue.repository;

import com.retail.catalogue.model.ItemReservation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemReservationRepository extends MongoRepository<ItemReservation, String> {

}