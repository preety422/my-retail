package com.retail.payment.service;

import com.razorpay.Order;
import com.razorpay.Payment;
import com.razorpay.RazorpayException;
import com.retail.common.model.Error;
import com.retail.common.model.PaymentRequest;
import com.retail.common.model.PaymentResponse;
import com.retail.common.model.ServiceRequest;
import com.retail.common.model.ServiceResponse;
import com.retail.payment.entity.MRPayment;
import com.retail.payment.exception.DuplicatePaymentException;
import com.retail.payment.exception.NoPaymentOrderCreatedException;
import com.retail.payment.repository.PaymentRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

  Logger LOGGER = LoggerFactory.getLogger(PaymentService.class);

  @Autowired
  private RazorpayService razorpayService;

  @Autowired
  private PaymentRepository paymentRepository;

  public ServiceResponse<PaymentResponse> capturePayment(
      ServiceRequest<PaymentRequest> paymentRequest) {
    String requestId = UUID.randomUUID().toString();
    ServiceResponse<PaymentResponse> serviceResponse = new ServiceResponse<>();
    serviceResponse.setRequestId(requestId);
    PaymentResponse paymentResponse = new PaymentResponse();
    List<Error> errors = null;
    MRPayment mrPayment = null;
    try {
      PaymentRequest request = paymentRequest.getPayload();
      if (Objects.nonNull(request.getOrderId())) {
        mrPayment = paymentRepository.findMRPaymentByOrderId(request.getOrderId());
      } else if (Objects.nonNull(request.getReceiptId())) {
        mrPayment = paymentRepository.findMRPaymentByReceiptId(request.getReceiptId());
      }
      if (null == mrPayment) {
        throw new NoPaymentOrderCreatedException("Order is not created for this payment request");
      } else if (mrPayment.getTransactionStatus().equals("Completed")) {
        throw new DuplicatePaymentException("Payment Already completed for this order");
      }
      mrPayment.setTransactionStatus("initiated");
      mrPayment.setPaymentId(request.getPaymentId());
      paymentRepository.save(mrPayment);

      Payment payment = razorpayService
          .makePayment(request, mrPayment.getOrderId(), request.getPaymentId());
      paymentResponse.setAmount(BigDecimal.valueOf(request.getAmount()));
      paymentResponse.setPaymentMethod(request.getPaymentMethod());
      paymentResponse.setCurrency("INR");
      paymentResponse.setFraudStatus("ACCEPT");
      paymentResponse.setTransactionStatus("Completed");
      paymentResponse.setTransactionDate(new Date());
      paymentResponse.setOrderId(mrPayment.getOrderId());
      paymentResponse.setReceiptId(mrPayment.getReceiptId());
      paymentResponse.setPaymentId(request.getPaymentId());

      mrPayment.setOperationDate(new Date());
      mrPayment.setPaymentId(request.getPaymentId());
      mrPayment.setTransactionStatus("Completed");
      mrPayment.setFraudStatus("ACCEPT");
      serviceResponse.setPayload(paymentResponse);
      serviceResponse.setStatus("200");

    } catch (RazorpayException | NoPaymentOrderCreatedException | DuplicatePaymentException e) {
      LOGGER.error("Error in Payment: ", e);
      errors = new ArrayList<>();
      Error error = new Error("400", e.getMessage());
      errors.add(error);
      paymentResponse.setTransactionStatus("Failed");
      serviceResponse.setErrors(errors);

      mrPayment.setTransactionStatus("Failed");
      paymentRepository.save(mrPayment);
      serviceResponse.setStatus("400");
    } finally {
      paymentRepository.save(mrPayment);
    }
    return serviceResponse;
  }

  public ServiceResponse<PaymentResponse> createPayment(
      ServiceRequest<PaymentRequest> paymentRequest) {

    ServiceResponse<PaymentResponse> serviceResponse = new ServiceResponse<>();
    PaymentResponse paymentResponse = new PaymentResponse();
    List<Error> errors = null;
    MRPayment mrPayment = new MRPayment();
    try {
      PaymentRequest request = paymentRequest.getPayload();
      mrPayment.setAmount(BigDecimal.valueOf(request.getAmount()));
      mrPayment.setPaymentMethod(request.getPaymentMethod());
      mrPayment.setReceiptId(request.getReceiptId());
      mrPayment.setCurrency("INR");
      mrPayment.setTransactionStatus("initiated");
      mrPayment.setCreatedDate(new Date());
      paymentResponse.setReceiptId(request.getReceiptId());

      paymentResponse.setAmount(BigDecimal.valueOf(request.getAmount()));
      paymentResponse.setPaymentMethod(request.getPaymentMethod());
      paymentResponse.setCurrency("INR");
      paymentResponse.setTransactionStatus("initiated");
      paymentResponse.setTransactionDate(new Date());

      Order order = razorpayService.createOrder(request);

      paymentResponse.setTransactionStatus("created");
      paymentResponse.setOrderId(order.get("id"));
      paymentResponse.setReceiptId(order.get("receipt"));

      mrPayment.setTransactionStatus("created");
      mrPayment.setOrderCreatedDate(order.get("created_at"));
      mrPayment.setUpdatedDate(new Date());
      mrPayment.setOrderId(order.get("id"));
      serviceResponse.setPayload(paymentResponse);
      serviceResponse.setStatus("200");
    } catch (RazorpayException e) {
      LOGGER.error("Error in Payment: ", e);
      errors = new ArrayList<>();
      Error error = new Error("400", e.getMessage());
      errors.add(error);
      paymentResponse.setTransactionStatus("Failed");
      serviceResponse.setErrors(errors);
      mrPayment.setUpdatedDate(new Date());
      mrPayment.setTransactionStatus("Failed");
      serviceResponse.setStatus("400");
    } finally {
      paymentRepository.save(mrPayment);
    }
    return serviceResponse;


  }
}
