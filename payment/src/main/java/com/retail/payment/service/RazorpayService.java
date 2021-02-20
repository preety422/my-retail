package com.retail.payment.service;

import com.razorpay.Order;
import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.retail.common.model.PaymentRequest;
import com.retail.payment.config.RazorpayProperties;
import com.retail.payment.entity.MRPayment;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RazorpayService {

  Logger LOGGER = LoggerFactory.getLogger(RazorpayService.class);

  private RazorpayClient razorpayClient;

  private RazorpayProperties razorpayProperties;

  @Autowired
  public RazorpayService(RazorpayProperties properties) {
    this.razorpayProperties = properties;
    initialize();
  }

  public void initialize() {
    try {
      //   razorpayProperties.getKey();
      razorpayClient = new RazorpayClient(razorpayProperties.getKey(),
          razorpayProperties.getSecret());
    } catch (RazorpayException e) {
      LOGGER.error("RazorpayException while razorpay client initialization ", e);
    } catch (Exception e) {
      LOGGER.error("Error while razorpay client initialization ", e);
    }
  }

  public List<Order> getAllOrders() throws RazorpayException {
    return razorpayClient.Orders.fetchAll();
  }

  public List<Payment> getOrderByOrderId(String orderId) throws RazorpayException {
    return razorpayClient.Orders.fetchPayments(orderId);
  }

  public Order createOrder(PaymentRequest request) throws RazorpayException {
    JSONObject options = new JSONObject();
    options.put("amount", request.getAmount() * 1000); // convert from rupee to paise
    options.put("currency", "INR");
    options.put("receipt", request.getReceiptId());
    Order order = razorpayClient.Orders.create(options);
    LOGGER.info("For receiptid {} Order Created in Razorpay {} ", request.getReceiptId(), order);
    return order;
  }

  public Payment makePayment(PaymentRequest request, String orderId, String paymentId) throws RazorpayException {
    JSONObject options = new JSONObject();
    options.put("amount", request.getAmount() * 1000); // convert from rupee to paise
    options.put("currency", "INR");

    List<Payment> payments = razorpayClient.Orders.fetchPayments(orderId);
    LOGGER.debug("For orderId {} Payment completed in Razorpay {} ", orderId, payments);
    // Need payment Id from app after payment initiation. INCOMPLETE
    //  Payment payment= razorpayClient.Payments.capture(paymentId, options);
    // "BAD_REQUEST_ERROR:The id provided does not exist" from Razorpay

    // return dummy result
    return new Payment(new JSONObject());
  }

  public MRPayment getPayment(String paymentId) throws RazorpayException {
    Payment payment = razorpayClient.Payments.fetch(paymentId);
    int amount = payment.get("amount"); // amount in paise
    String id = payment.get("id");
    Date createdAt = payment.get("created_at");

    MRPayment mrPayment = new MRPayment();
    mrPayment.setPaymentId(id);
    mrPayment.setAmount(
        BigDecimal.valueOf(amount).divide(BigDecimal.valueOf(1000), 2, RoundingMode.HALF_UP));
    mrPayment.setCurrency("INR");
    mrPayment.setCreatedDate(createdAt);
    mrPayment.setOperationDate(createdAt);
    mrPayment.setUpdatedDate(new Date());

    return mrPayment;
  }


}
