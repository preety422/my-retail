package com.retail.orchestrator.service;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import com.retail.common.model.CustomHttpResponse;
import com.retail.common.model.ItemResponse;
import com.retail.common.model.PaymentResponse;
import com.retail.common.model.ServiceRequest;
import com.retail.common.model.ServiceResponse;
import com.retail.common.util.ErrorResponseUtil;
import com.retail.common.service.RestService;
import com.retail.common.util.JsonOutputFormatter;
import com.retail.common.util.ObjectMapperUtils;
import com.retail.orchestrator.config.RestClientProperties;
import com.retail.orchestrator.model.MROrder;
import com.retail.orchestrator.repository.OrderRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import java.io.IOException;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({RetailOrchService.class, HttpClientBuilder.class, EntityUtils.class,
    ErrorResponseUtil.class})
public class RetailOrchServiceTest {


  private AuthService authService;

  @Mock
  private RestService restService;

  private OrderRepository repository;

  @InjectMocks
  private RetailOrchService retailOrchService;

  private RestClientProperties restClientProperties;

  @Before
  public void setUp() {
    restClientProperties = new RestClientProperties();
    authService = Mockito.mock(AuthService.class);
    repository = Mockito.mock(OrderRepository.class);
    retailOrchService = new RetailOrchService(restClientProperties, authService, repository);

  }

  @Test
  public void getItemDetailsTest() throws Exception {
    mockHttpPostCall();
    ServiceResponse<ItemResponse> serviceResponse = new ServiceResponse();
    serviceResponse.setStatus("200");
    String response = "{\n"
        + "                \"itemBarcode\": \"12345678\",\n"
        + "                \"upcBarcode\": \"987654321\",\n"
        + "                \"unitPrice\": 100.00 }";
    serviceResponse.setPayload(
        ObjectMapperUtils.convertStringToObject(response, ItemResponse.class));

    Mockito.when(authService.isValidUser(anyString(), anyString()))
        .thenReturn(true);

    Mockito.when(restService.post(anyString(), any(), anyString(), anyString(), anyString()))
        .thenReturn(new CustomHttpResponse());
    when(EntityUtils.toString(any())).thenReturn(JsonOutputFormatter.generateJson(serviceResponse));

    ServiceResponse<ItemResponse> orchServiceResponse = retailOrchService
        .getItemDetails(new ServiceRequest<>(), "token", "user");
    assertNotNull(orchServiceResponse);
    assertNotNull(orchServiceResponse.getPayload());
  }

  @Test
  public void getItemDetailsInvalidUserTokenMAlformsTest() throws Exception {
    mockHttpPostCall();
    ServiceResponse<ItemResponse> serviceResponse = new ServiceResponse();
    serviceResponse.setStatus("401");
    ServiceResponse<ItemResponse> orchServiceResponse = null;
    try {
      Mockito.when(authService.isValidUser(anyString(), anyString()))
          .thenThrow(new MalformedJwtException("malformed"));
      PowerMockito.mockStatic(ErrorResponseUtil.class);
      orchServiceResponse = retailOrchService
          .getItemDetails(new ServiceRequest<>(), "token", "user");
      assertNotNull(orchServiceResponse);
    } catch (MalformedJwtException e) {
      assertEquals("malformed", e.getMessage());
      assertEquals("401", orchServiceResponse.getStatus());
    }
  }

  @Test
  public void getItemDetailsInvalidUserTokenExpiredsTest() throws Exception {
    mockHttpPostCall();
    ServiceResponse<ItemResponse> serviceResponse = new ServiceResponse();
    serviceResponse.setStatus("401");
    ServiceResponse<ItemResponse> orchServiceResponse = null;
    try {
      Mockito.when(authService.isValidUser(anyString(), anyString()))
          .thenThrow(new ExpiredJwtException(null, null, "expired"));

      orchServiceResponse = retailOrchService
          .getItemDetails(new ServiceRequest<>(), "token", "user");
      PowerMockito.mockStatic(ErrorResponseUtil.class);
      assertNotNull(orchServiceResponse);

    } catch (ExpiredJwtException e) {
      assertEquals("expired", e.getMessage());
      assertEquals("401", orchServiceResponse.getStatus());
    }
  }

  @Test
  public void getItemDetailsExternalServiceErrorTest() throws Exception {
    mockHttpPostCall();
    ServiceResponse<ItemResponse> serviceResponse = new ServiceResponse();
    serviceResponse.setStatus("400");
    try {
      Mockito.when(authService.isValidUser(anyString(), anyString()))
          .thenReturn(true);
      Mockito.when(restService.post(anyString(), any(), anyString(), anyString(), anyString()))
          .thenReturn(new CustomHttpResponse());

      when(EntityUtils.toString(any()))
          .thenThrow(new IOException("io exception"));

      ServiceResponse<ItemResponse> orchServiceResponse = retailOrchService
          .getItemDetails(new ServiceRequest<>(), "token", "user");

      assertNotNull(orchServiceResponse);
      assertEquals("400", orchServiceResponse.getStatus());
    } catch (IOException e) {
      assertEquals("io exception", e.getMessage());
    }
  }

  @Test
  public void getItemDetailsInternalErrorTest() throws Exception {
    mockHttpPostCall();
    ServiceResponse<ItemResponse> serviceResponse = new ServiceResponse();
    serviceResponse.setStatus("500");
    try {
      Mockito.when(authService.isValidUser(anyString(), anyString()))
          .thenReturn(true);
      Mockito.when(restService.post(anyString(), any(), anyString(), anyString(), anyString()))
          .thenReturn(new CustomHttpResponse());

      when(EntityUtils.toString(any()))
          .thenThrow(new IllegalStateException("illegal state"));

      ServiceResponse<ItemResponse> orchServiceResponse = retailOrchService
          .getItemDetails(new ServiceRequest<>(), "token", "user");

      assertNotNull(orchServiceResponse);
      assertEquals("500", orchServiceResponse.getStatus());
    } catch (Exception e) {
      assertEquals("illegal state", e.getMessage());
    }
  }

  @Test
  public void createPaymentTest() throws Exception {
    mockHttpPostCall();
    ServiceResponse<PaymentResponse> serviceResponse = new ServiceResponse();
    serviceResponse.setStatus("200");
    String response = "{\n"
        + "                \"orderId\": \"order_GdmDV4q5D84jZ9\",\n"
        + "                \"receiptId\": \"txn123003\",\n"
        + "                \"amount\": 100.0,\n"
        + "                \"paymentId\": \"pg_1234\"\n"
        + "            }";
    serviceResponse.setPayload(
        ObjectMapperUtils.convertStringToObject(response, PaymentResponse.class));

    Mockito.when(authService.isValidUser(anyString(), anyString()))
        .thenReturn(true);

    Mockito.when(restService.post(anyString(), any(), anyString(), anyString(), anyString()))
        .thenReturn(new CustomHttpResponse());
    when(EntityUtils.toString(any())).thenReturn(JsonOutputFormatter.generateJson(serviceResponse));

    ServiceResponse<PaymentResponse> orchServiceResponse = retailOrchService
        .createPayment(new ServiceRequest<>(), "token", "user");

    assertNotNull(orchServiceResponse);
    assertNotNull(orchServiceResponse.getPayload());

  }

  @Test
  public void createPaymentInvalidUserTokenMAlformsTest() throws Exception {
    mockHttpPostCall();
    ServiceResponse<PaymentResponse> serviceResponse = new ServiceResponse();
    serviceResponse.setStatus("401");
    ServiceResponse<PaymentResponse> orchServiceResponse = null;
    try {
      Mockito.when(authService.isValidUser(anyString(), anyString()))
          .thenThrow(new MalformedJwtException("malformed"));

      orchServiceResponse = retailOrchService
          .createPayment(new ServiceRequest<>(), "token", "user");
      assertNotNull(orchServiceResponse);
      PowerMockito.mockStatic(ErrorResponseUtil.class);

    } catch (MalformedJwtException e) {
      assertEquals("malformed", e.getMessage());
      assertEquals("401", orchServiceResponse.getStatus());
    }
  }

  @Test
  public void createPaymentInvalidUserTokenExpiredsTest() throws Exception {
    mockHttpPostCall();
    ServiceResponse<PaymentResponse> serviceResponse = new ServiceResponse();
    serviceResponse.setStatus("401");
    ServiceResponse<PaymentResponse> orchServiceResponse = null;
    try {
      Mockito.when(authService.isValidUser(anyString(), anyString()))
          .thenThrow(new ExpiredJwtException(null, null, "expired"));
      PowerMockito.mockStatic(ErrorResponseUtil.class);
      orchServiceResponse = retailOrchService
          .createPayment(new ServiceRequest<>(), "token", "user");
      assertNotNull(orchServiceResponse);
    } catch (ExpiredJwtException e) {
      assertEquals("expired", e.getMessage());
      assertEquals("401", orchServiceResponse.getStatus());
    }
  }

  @Test
  public void createPaymentExternalServiceErrorTest() throws Exception {
    mockHttpPostCall();
    ServiceResponse<PaymentResponse> serviceResponse = new ServiceResponse();
    serviceResponse.setStatus("400");
    try {
      Mockito.when(authService.isValidUser(anyString(), anyString()))
          .thenReturn(true);
      Mockito.when(restService.post(anyString(), any(), anyString(), anyString(), anyString()))
          .thenReturn(new CustomHttpResponse());
      when(EntityUtils.toString(any()))
          .thenThrow(new IOException("io exception"));

      ServiceResponse<PaymentResponse> orchServiceResponse = retailOrchService
          .createPayment(new ServiceRequest<>(), "token", "user");

      assertNotNull(orchServiceResponse);
      assertEquals("400", orchServiceResponse.getStatus());
    } catch (IOException e) {
      assertEquals("io exception", e.getMessage());
    }
  }

  @Test
  public void createPaymentInternalErrorTest() throws Exception {
    mockHttpPostCall();
    ServiceResponse<PaymentResponse> serviceResponse = new ServiceResponse();
    serviceResponse.setStatus("500");
    try {
      Mockito.when(authService.isValidUser(anyString(), anyString()))
          .thenReturn(true);
      Mockito.when(restService.post(anyString(), any(), anyString(), anyString(), anyString()))
          .thenReturn(new CustomHttpResponse());
      when(EntityUtils.toString(any()))
          .thenThrow(new IllegalStateException("illegal state"));

      ServiceResponse<PaymentResponse> orchServiceResponse = retailOrchService
          .createPayment(new ServiceRequest<>(), "token", "user");

      assertNotNull(orchServiceResponse);
      assertEquals("500", orchServiceResponse.getStatus());
    } catch (Exception e) {
      assertEquals("illegal state", e.getMessage());
    }
  }

  @Test
  public void capturePaymentTest() throws Exception {
    mockHttpPostCall();
    ServiceResponse<PaymentResponse> serviceResponse = new ServiceResponse();
    serviceResponse.setStatus("200");
    String response = "{\n"
        + "                \"orderId\": \"order_GdmDV4q5D84jZ9\",\n"
        + "                \"receiptId\": \"txn123003\",\n"
        + "                \"paymentMethod\": \"CreditCard\",\n"
        + "                \"amount\": 100.0,\n"
        + "                \"currency\": \"INR\",\n"
        + "                \"fraudStatus\": \"ACCEPT\",\n"
        + "                \"transactionStatus\": \"Completed\",\n"
        + "                \"transactionDate\": \"2021-02-20T14:54:53\"\n"
        + "            }";
    serviceResponse.setPayload(
        ObjectMapperUtils.convertStringToObject(response, PaymentResponse.class));

    Mockito.when(authService.isValidUser(anyString(), anyString()))
        .thenReturn(true);

    Mockito.when(restService.post(anyString(), any(), anyString(), anyString(), anyString()))
        .thenReturn(new CustomHttpResponse());
    when(EntityUtils.toString(any())).thenReturn(JsonOutputFormatter.generateJson(serviceResponse));

    ServiceResponse<PaymentResponse> orchServiceResponse = retailOrchService
        .capturePayment(new ServiceRequest<>(), "token", "user");

    assertNotNull(orchServiceResponse);
    assertNotNull(orchServiceResponse.getPayload());

  }

  @Test
  public void capturePaymentInvalidUserTokenMAlformsTest() throws Exception {
    mockHttpPostCall();
    ServiceResponse<PaymentResponse> serviceResponse = new ServiceResponse();
    serviceResponse.setStatus("401");
    ServiceResponse<PaymentResponse> orchServiceResponse = null;
    try {
      Mockito.when(authService.isValidUser(anyString(), anyString()))
          .thenThrow(new MalformedJwtException("malformed"));
      PowerMockito.mockStatic(ErrorResponseUtil.class);
      orchServiceResponse = retailOrchService
          .capturePayment(new ServiceRequest<>(), "token", "user");
      assertNotNull(orchServiceResponse);

    } catch (MalformedJwtException e) {
      assertEquals("malformed", e.getMessage());
      assertEquals("401", orchServiceResponse.getStatus());
    }
  }

  @Test
  public void capturePaymentInvalidUserTokenExpiredsTest() throws Exception {
    mockHttpPostCall();
    ServiceResponse<PaymentResponse> serviceResponse = new ServiceResponse();
    serviceResponse.setStatus("401");
    ServiceResponse<PaymentResponse> orchServiceResponse = null;
    try {
      Mockito.when(authService.isValidUser(anyString(), anyString()))
          .thenThrow(new ExpiredJwtException(null, null, "expired"));
      PowerMockito.mockStatic(ErrorResponseUtil.class);
      orchServiceResponse = retailOrchService
          .capturePayment(new ServiceRequest<>(), "token", "user");
      assertNotNull(orchServiceResponse);
    } catch (ExpiredJwtException e) {
      assertEquals("expired", e.getMessage());
      assertEquals("401", orchServiceResponse.getStatus());
    }
  }

  @Test
  public void capturePaymentExternalServiceErrorTest() throws Exception {
    mockHttpPostCall();
    ServiceResponse<PaymentResponse> serviceResponse = new ServiceResponse();
    serviceResponse.setStatus("400");
    try {
      Mockito.when(authService.isValidUser(anyString(), anyString()))
          .thenReturn(true);
      Mockito.when(restService.post(anyString(), any(), anyString(), anyString(), anyString()))
          .thenReturn(new CustomHttpResponse());
      when(EntityUtils.toString(any()))
          .thenThrow(new IOException("io exception"));

      ServiceResponse<PaymentResponse> orchServiceResponse = retailOrchService
          .capturePayment(new ServiceRequest<>(), "token", "user");

      assertNotNull(orchServiceResponse);
      assertEquals("400", orchServiceResponse.getStatus());
    } catch (IOException e) {
      assertEquals("io exception", e.getMessage());
    }
  }

  @Test
  public void capturePaymentInternalErrorTest() throws Exception {
    mockHttpPostCall();
    ServiceResponse<PaymentResponse> serviceResponse = new ServiceResponse();
    serviceResponse.setStatus("500");
    try {
      Mockito.when(authService.isValidUser(anyString(), anyString()))
          .thenReturn(true);
      Mockito.when(restService.post(anyString(), any(), anyString(), anyString(), anyString()))
          .thenReturn(new CustomHttpResponse());

      when(EntityUtils.toString(any()))
          .thenThrow(new IllegalStateException("illegal state"));

      ServiceResponse<PaymentResponse> orchServiceResponse = retailOrchService
          .capturePayment(new ServiceRequest<>(), "token", "user");

      assertNotNull(orchServiceResponse);
      assertEquals("500", orchServiceResponse.getStatus());
    } catch (Exception e) {
      assertEquals("illegal state", e.getMessage());
    }
  }

  @Test
  public void checkoutPaymentTest() throws Exception {
    mockHttpPostCall();
    ServiceResponse<MROrder> serviceResponse = new ServiceResponse();
    serviceResponse.setStatus("200");
    String response = "{\n"
        + "        \"items\": [\n"
        + "            {\n"
        + "                \"itemBarcode\": \"12345678\",\n"
        + "                \"upcBarcode\": \"987654321\",\n"
        + "                \"unitPrice\": 100.00,\n"
        + "                \"unitUpchargePrice\": 102.00,\n"
        + "                \"extendedPrice\": 200.00,\n"
        + "                \"extendedUpchargePrice\": 204.00,\n"
        + "                \"taxes\": [\n"
        + "                    {\n"
        + "                        \"typeCode\": \"1\",\n"
        + "                        \"name\": \"VAT\",\n"
        + "                        \"rate\": 16.0\n"
        + "                    },\n"
        + "                    {\n"
        + "                        \"typeCode\": \"2\",\n"
        + "                        \"name\": \"STATE\",\n"
        + "                        \"rate\": 10.0\n"
        + "                    }\n"
        + "                ],\n"
        + "                \"promotions\": [\n"
        + "                    {\n"
        + "                        \"id\": \"2\",\n"
        + "                        \"name\": \"15 % Off\",\n"
        + "                        \"amount\": 30.00,\n"
        + "                        \"rate\": 15.0,\n"
        + "                        \"quantity\": 2\n"
        + "                    }\n"
        + "                ],\n"
        + "                \"quantity\": 2,\n"
        + "                \"reservationId\": \"6030dac69a64406ffbef78a7\",\n"
        + "                \"inventoryId\": \"602dfdd4b0821c0905176ebb\",\n"
        + "                \"currency\": \"INR\"\n"
        + "            }\n"
        + "        ],\n"
        + "        \"payments\": [\n"
        + "            {\n"
        + "                \"orderId\": \"order_GdmDV4q5D84jZ9\",\n"
        + "                \"receiptId\": \"txn123003\",\n"
        + "                \"paymentMethod\": \"CreditCard\",\n"
        + "                \"amount\": 100.0,\n"
        + "                \"currency\": \"INR\",\n"
        + "                \"fraudStatus\": \"ACCEPT\",\n"
        + "                \"transactionStatus\": \"Completed\",\n"
        + "                \"transactionDate\": \"2021-02-20T14:54:53\"\n"
        + "            }\n"
        + "        ],\n"
        + "        \"customerProfileId\": \"47d3df63-2179-48c7-9618-9c694ab3faf7\",\n"
        + "        \"createDate\":\"2021-02-20T14:50:53\",\n"
        + "        \"updateDate\":\"2021-02-20T14:55:53\"\n"
        + "    }";
    serviceResponse.setPayload(
        ObjectMapperUtils.convertStringToObject(response, MROrder.class));

    Mockito.when(authService.isValidUser(anyString(), anyString()))
        .thenReturn(true);

    Mockito.when(repository.save(any())).thenReturn(new MROrder());

    Mockito.when(restService.post(anyString(), any(), anyString(), anyString(), anyString()))
        .thenReturn(new CustomHttpResponse());
    when(EntityUtils.toString(any())).thenReturn(JsonOutputFormatter.generateJson(serviceResponse));

    ServiceResponse<MROrder> orchServiceResponse = retailOrchService
        .checkout(new ServiceRequest<>(), "token", "user");

    assertNotNull(orchServiceResponse);
    assertNotNull(orchServiceResponse.getPayload());

  }

  @Test
  public void checkoutInvalidUserTokenMAlformsTest() throws Exception {
    mockHttpPostCall();
    ServiceResponse<MROrder> serviceResponse = new ServiceResponse();
    serviceResponse.setStatus("401");
    ServiceResponse<MROrder> orchServiceResponse = null;
    try {
      Mockito.when(authService.isValidUser(anyString(), anyString()))
          .thenThrow(new MalformedJwtException("malformed"));
      PowerMockito.mockStatic(ErrorResponseUtil.class);
      orchServiceResponse = retailOrchService
          .checkout(new ServiceRequest<>(), "token", "user");
      assertNotNull(orchServiceResponse);

    } catch (MalformedJwtException e) {
      assertEquals("401", orchServiceResponse.getStatus());
      assertEquals("malformed", e.getMessage());
    }
  }

  @Test
  public void checkoutInvalidUserTokenExpiredsTest() throws Exception {
    mockHttpPostCall();
    ServiceResponse<MROrder> serviceResponse = new ServiceResponse();
    serviceResponse.setStatus("401");
    ServiceResponse<MROrder> orchServiceResponse = null;
    try {
      Mockito.when(authService.isValidUser(anyString(), anyString()))
          .thenThrow(new ExpiredJwtException(null, null, "expired"));
      PowerMockito.mockStatic(ErrorResponseUtil.class);
      orchServiceResponse = retailOrchService
          .checkout(new ServiceRequest<>(), "token", "user");
      assertNotNull(orchServiceResponse);

    } catch (ExpiredJwtException e) {
      assertEquals("expired", e.getMessage());
      assertEquals("401", orchServiceResponse.getStatus());
    }
  }

  @Test
  public void checkoutInternalErrorTest() throws Exception {
    mockHttpPostCall();
    ServiceResponse<MROrder> serviceResponse = new ServiceResponse();
    serviceResponse.setStatus("500");
    try {
      Mockito.when(authService.isValidUser(anyString(), anyString()))
          .thenThrow(new IllegalStateException("illegal state"));

      ServiceResponse<MROrder> orchServiceResponse = retailOrchService
          .checkout(new ServiceRequest<>(), "token", "user");

      assertNotNull(orchServiceResponse);
      assertEquals("500", orchServiceResponse.getStatus());
    } catch (Exception e) {
      assertEquals("illegal state", e.getMessage());
    }
  }

  private void mockHttpPostCall() throws Exception {
    URIBuilder uriBuilder = mock(URIBuilder.class);
    whenNew(URIBuilder.class).withAnyArguments().thenReturn(uriBuilder);
    when(uriBuilder.setParameter(anyString(), anyString())).thenReturn(uriBuilder);

    HttpPost httpPost = mock(HttpPost.class);
    whenNew(HttpPost.class).withAnyArguments().thenReturn(httpPost);

    HttpClientBuilder httpClientBuilder = mock(HttpClientBuilder.class);
    mockStatic(HttpClientBuilder.class);
    when(HttpClientBuilder.create()).thenReturn(httpClientBuilder);
    when(httpClientBuilder.setDefaultRequestConfig(any())).thenReturn(httpClientBuilder);

    CloseableHttpClient closeableHttpClient = mock(CloseableHttpClient.class);
    when(HttpClientBuilder.create().build()).thenReturn(closeableHttpClient);

    CloseableHttpResponse response = mock(CloseableHttpResponse.class);
    when(closeableHttpClient.execute(any())).thenReturn(response);

    StatusLine statusLine = mock(StatusLine.class);
    when(response.getStatusLine()).thenReturn(statusLine);
    when(statusLine.getStatusCode()).thenReturn(200);

    mockStatic(EntityUtils.class);
  }

}