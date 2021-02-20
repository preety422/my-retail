package com.retail.orchestrator.service;

import com.retail.common.model.CustomHttpResponse;
import com.retail.common.model.Error;
import com.retail.common.model.ItemResponse;
import com.retail.common.model.PaymentRequest;
import com.retail.common.model.PaymentResponse;
import com.retail.common.model.ServiceRequest;
import com.retail.common.model.ServiceResponse;
import com.retail.common.service.RestService;
import com.retail.common.util.JsonOutputFormatter;
import com.retail.common.util.ObjectMapperUtils;
import com.retail.orchestrator.config.RestClientProperties;
import com.retail.orchestrator.model.MROrder;
import com.retail.orchestrator.model.RestConstants;
import com.retail.orchestrator.repository.OrderRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RetailOrchService extends RestService {

  Logger LOGGER = LoggerFactory.getLogger(RetailOrchService.class);

  @Autowired
  private AuthService authService;

  @Autowired
  private OrderRepository orderRepository;

  private RestClientProperties restClientProperties;

  @Autowired
  public RetailOrchService(RestClientProperties properties) {
    this.restClientProperties = properties;
  }

  public ServiceResponse<ItemResponse> getItemDetails(String store, String barcode,
      String bearerToken, String userId) {
    String requestId = UUID.randomUUID().toString();
    ServiceResponse<ItemResponse> serviceResponse = new ServiceResponse<>();
    serviceResponse.setRequestId(requestId);
    ItemResponse itemResponse = null;

    boolean isAuthorized = authService.isValidUser(bearerToken, userId);
    List<Error> errors = null;
    if (!isAuthorized) {
      return getUnauthorizedServiceResponse(serviceResponse);
    }
    try {
      Map<String, String> headerMap = new HashMap<>();
      headerMap.put("requestId", requestId);
      CustomHttpResponse customHttpResponse =
          get(restClientProperties.getCatalogUrl() + RestConstants.ITEM_URI, headerMap,
              new HashMap<>(), requestId, RestConstants.EXTERNAL_SERVICE_CATALOG);
      itemResponse = ObjectMapperUtils
          .convertStringToObject(customHttpResponse.getHttpResponseEntity(),
              ItemResponse.class);
      serviceResponse.setPayload(itemResponse);
      serviceResponse.setStatus(RestConstants.OK_200);
    } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
      return getUnauthorizedServiceResponse(serviceResponse);
    } catch (IOException | URISyntaxException e) {
      LOGGER.error("OrchestratorService::Error in get Item details: ", e);
      errors = new ArrayList<>();
      Error error = new Error(RestConstants.ERR_404, e.getMessage());
      errors.add(error);
      serviceResponse.setErrors(errors);
      serviceResponse.setStatus(RestConstants.ERR_404);
    }
    return serviceResponse;
  }

  public ServiceResponse<MROrder> checkout(
      ServiceRequest<MROrder> checkoutRequest, String bearerToken, String userId) {
    String requestId = UUID.randomUUID().toString();
    ServiceResponse<MROrder> serviceResponse = new ServiceResponse<>();
    serviceResponse.setRequestId(requestId);
    try {
      boolean isAuthorized = authService.isValidUser(bearerToken, userId);
      if (!isAuthorized) {
        return getUnauthorizedServiceResponse(serviceResponse);
      }
      MROrder order = orderRepository.save(checkoutRequest.getPayload());
      serviceResponse.setPayload(order);
    } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
      return getUnauthorizedServiceResponse(serviceResponse);
    }
    return serviceResponse;
  }

  public ServiceResponse<PaymentResponse> createPayment(
      ServiceRequest<PaymentRequest> paymentRequest, String bearerToken, String userId) {
    String requestId = UUID.randomUUID().toString();
    ServiceResponse<PaymentResponse> serviceResponse = new ServiceResponse<>();
    serviceResponse.setRequestId(requestId);

    boolean isAuthorized = authService.isValidUser(bearerToken, userId);
    if (!isAuthorized) {
      return getUnauthorizedServiceResponse(serviceResponse);
    }

    PaymentResponse paymentResponse = null;
    try {
      Map<String, String> headerMap = new HashMap<>();
      headerMap.put("requestId", requestId);
      CustomHttpResponse customHttpResponse =
          post(restClientProperties.getPaymentUrl() + RestConstants.CREATE_PAYMENT_URI, headerMap,
              JsonOutputFormatter.generateJson(paymentRequest.getPayload()), requestId,
              RestConstants.EXTERNAL_SERVICE_PAYMENT);
      paymentResponse = ObjectMapperUtils
          .convertStringToObject(customHttpResponse.getHttpResponseEntity(),
              PaymentResponse.class);
      serviceResponse.setPayload(paymentResponse);
      serviceResponse.setStatus(RestConstants.OK_200);
    } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
      return getUnauthorizedServiceResponse(serviceResponse);
    } catch (IOException | URISyntaxException e) {
      LOGGER.error("OrchestratorService::Error in create Order : ", e);
      List<Error> errors = new ArrayList<>();
      Error error = new Error(RestConstants.ERR_404, e.getMessage());
      errors.add(error);
      serviceResponse.setErrors(errors);
      serviceResponse.setStatus(RestConstants.ERR_404);
    }
    return serviceResponse;
  }

  public ServiceResponse<PaymentResponse> capturePayment(
      ServiceRequest<PaymentRequest> paymentRequest, String bearerToken, String userId) {

    String requestId = UUID.randomUUID().toString();
    ServiceResponse<PaymentResponse> serviceResponse = new ServiceResponse<>();
    serviceResponse.setRequestId(requestId);

    boolean isAuthorized = authService.isValidUser(bearerToken, userId);
    if (!isAuthorized) {
      return getUnauthorizedServiceResponse(serviceResponse);
    }

    PaymentResponse paymentResponse = null;
    try {
      Map<String, String> headerMap = new HashMap<>();
      headerMap.put("requestId", requestId);
      CustomHttpResponse customHttpResponse =
          post(restClientProperties.getPaymentUrl() + RestConstants.COMPLETE_PAYMENT_URI, headerMap,
              JsonOutputFormatter.generateJson(paymentRequest.getPayload()), requestId,
              RestConstants.EXTERNAL_SERVICE_PAYMENT);
      paymentResponse = ObjectMapperUtils
          .convertStringToObject(customHttpResponse.getHttpResponseEntity(),
              PaymentResponse.class);
      serviceResponse.setPayload(paymentResponse);
      serviceResponse.setStatus(RestConstants.OK_200);
    } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
      return getUnauthorizedServiceResponse(serviceResponse);
    } catch (IOException | URISyntaxException e) {
      LOGGER.error("OrchestratorService::Error in Capture payment : ", e);
      List<Error> errors = new ArrayList<>();
      Error error = new Error(RestConstants.ERR_404, e.getMessage());
      errors.add(error);
      serviceResponse.setErrors(errors);
      serviceResponse.setStatus(RestConstants.ERR_404);
    }
    return serviceResponse;
  }

  private ServiceResponse getUnauthorizedServiceResponse(
      ServiceResponse serviceResponse) {
    serviceResponse.setStatus(RestConstants.ERR_401);
    List<Error> errors = new ArrayList<>();
    Error error = new Error(RestConstants.ERR_401, RestConstants.UNAUTHORIZED);
    errors.add(error);
    serviceResponse.setErrors(errors);
    return serviceResponse;
  }
}
