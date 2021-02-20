package com.retail.payment.controller;

import com.retail.common.model.PaymentRequest;
import com.retail.common.model.PaymentResponse;
import com.retail.common.model.ServiceRequest;
import com.retail.common.model.ServiceResponse;
import com.retail.payment.service.PaymentService;
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
@RequestMapping("/payment")
public class PaymentController {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(PaymentController.class);

  @Autowired
  private PaymentService paymentService;

  @PostMapping(value = "/create", produces = "application/json")
  @ResponseBody
  public ServiceResponse<PaymentResponse> createPayment(
      @Valid @RequestBody ServiceRequest<PaymentRequest> paymentRequest) {
    long startTime = System.currentTimeMillis();
    ServiceResponse<PaymentResponse> paymentResponse = paymentService.createPayment(paymentRequest);

    LOGGER.info("Charge response time: {}", System.currentTimeMillis() - startTime);
    return paymentResponse;
  }

  @PostMapping(value = "/complete", produces = "application/json")
  @ResponseBody
  public ServiceResponse<PaymentResponse> completePayment(
      @Valid @RequestBody ServiceRequest<PaymentRequest> paymentRequest) {
    long startTime = System.currentTimeMillis();
    ServiceResponse<PaymentResponse> paymentResponse = paymentService.capturePayment(paymentRequest);

    LOGGER.info("Charge response time: {}", System.currentTimeMillis() - startTime);
    return paymentResponse;
  }

}


