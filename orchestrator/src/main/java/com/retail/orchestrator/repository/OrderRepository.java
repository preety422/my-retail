package com.retail.orchestrator.repository;

import com.retail.orchestrator.model.MROrder;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends MongoRepository<MROrder, String> {

}