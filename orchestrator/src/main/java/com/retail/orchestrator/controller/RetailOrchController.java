package com.retail.orchestrator.controller;

import com.retail.common.model.ItemRequest;
import com.retail.common.model.ItemResponse;
import com.retail.common.model.PaymentRequest;
import com.retail.common.model.PaymentResponse;
import com.retail.common.model.ServiceRequest;
import com.retail.common.model.ServiceResponse;
import com.retail.orchestrator.model.CheckoutRespose;
import com.retail.orchestrator.model.MROrder;
import com.retail.orchestrator.model.MRPayment;
import com.retail.orchestrator.service.RetailOrchService;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orch")
public class RetailOrchController {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(RetailOrchController.class);

  @Autowired
  private RetailOrchService retailOrchService;

  @PostMapping(value = "/item", produces = "application/json")
  @ResponseBody
  public ServiceResponse<ItemResponse> getItem(@Valid @RequestBody ServiceRequest<ItemRequest> itemRequest,
      @RequestHeader("authorization") String authorization,
      @RequestHeader("user") String user) {
    long startTime = System.currentTimeMillis();

    ServiceResponse<ItemResponse> itemResponse = retailOrchService
        .getItemDetails(itemRequest, authorization, user);

    LOGGER.info("Get Item API response time: ", System.currentTimeMillis() - startTime);
    return itemResponse;
  }


  @PostMapping(value = "/payment/create", produces = "application/json")
  @ResponseBody
  public ServiceResponse<PaymentResponse> createPayment(
      @Valid @RequestBody ServiceRequest<PaymentRequest> paymentRequest,
      @RequestHeader("authorization") String authorization,
      @RequestHeader("user") String user) {
    long startTime = System.currentTimeMillis();
    ServiceResponse<PaymentResponse> paymentResponse = retailOrchService.createPayment(paymentRequest, authorization, user);

    LOGGER.info("Charge response time: {}", System.currentTimeMillis() - startTime);
    return paymentResponse;
  }

  @PostMapping(value = "/payment/complete", produces = "application/json")
  @ResponseBody
  public ServiceResponse<PaymentResponse> completePayment(
      @Valid @RequestBody ServiceRequest<PaymentRequest> paymentRequest,
      @RequestHeader("authorization") String authorization,
      @RequestHeader("user") String user) {
    long startTime = System.currentTimeMillis();
    ServiceResponse<PaymentResponse> paymentResponse = retailOrchService.capturePayment(paymentRequest, authorization, user);

    LOGGER.info("Charge response time: {}", System.currentTimeMillis() - startTime);
    return paymentResponse;
  }

  @PostMapping(value = "/checkout", produces = "application/json")
  @ResponseBody
  public ServiceResponse<MROrder> createCheckoutOrder(
      @Valid @RequestBody ServiceRequest<MROrder> checkoutRequest,
      @RequestHeader("authorization") String authorization,
      @RequestHeader("user") String user) {
    long startTime = System.currentTimeMillis();
    ServiceResponse<MROrder> checkoutResponse = retailOrchService.checkout(checkoutRequest, authorization, user);

    LOGGER.info("Create checkout response time: ", System.currentTimeMillis() - startTime);
    return checkoutResponse;
  }

}


