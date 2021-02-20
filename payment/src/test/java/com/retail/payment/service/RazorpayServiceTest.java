//package com.retail.payment.service;
//
//import com.razorpay.Payment;
//import com.razorpay.RazorpayClient;
//import com.razorpay.RazorpayException;
//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.powermock.core.classloader.annotations.PrepareForTest;
//import org.powermock.modules.junit4.PowerMockRunner;
//
//
//@RunWith(PowerMockRunner.class)
//@PrepareForTest(RazorpayService.class)
//public class RazorpayServiceTest {
//
//  @Mock
//  private RazorpayClient razorpayClient;
//
//  @InjectMocks
//  private RazorpayService razorpayService;
//
//  @Test
//  public void makePaymentTest() throws RazorpayException {
//
//    PaymentRequest request= new PaymentRequest();
//    request.setAmount(100.00);
//    request.setPaymentId("pay-123");
//  //  Mockito.when(razorpayClient.Payments.capture(anyString(), any())).thenReturn(new Payment(new JSONObject()));
//    Payment payment= razorpayService.makePayment(request);
//    Assert.assertNotNull(payment);
//  }
//
//}
