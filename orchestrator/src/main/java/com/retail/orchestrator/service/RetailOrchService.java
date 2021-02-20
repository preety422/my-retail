package com.retail.orchestrator.service;

import static com.retail.common.util.ErrorResponseUtil.prepareBadRequestResponse;
import static com.retail.common.util.ErrorResponseUtil.prepareServerErrorResponse;
import static com.retail.common.util.ErrorResponseUtil.prepareUnauthorizedServiceResponse;

import com.retail.common.constants.RestConstants;
import com.retail.common.model.CustomHttpResponse;
import com.retail.common.model.ItemRequest;
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
import com.retail.orchestrator.repository.OrderRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RetailOrchService extends RestService {

  private Logger LOGGER = LoggerFactory.getLogger(RetailOrchService.class);


  private AuthService authService;

  private OrderRepository orderRepository;

  private RestClientProperties restClientProperties;

  @Autowired
  public RetailOrchService(RestClientProperties properties, AuthService authService,
      OrderRepository orderRepository) {
    this.restClientProperties = properties;
    this.authService = authService;
    this.orderRepository = orderRepository;
  }

  public ServiceResponse<ItemResponse> getItemDetails(ServiceRequest<ItemRequest> itemRequest,
      String bearerToken, String userId) {
    String requestId = UUID.randomUUID().toString();
    ServiceResponse<ItemResponse> serviceResponse = new ServiceResponse<>();
    serviceResponse.setRequestId(requestId);
    try {
      boolean isAuthorized = authService.isValidUser(bearerToken, userId);
      if (!isAuthorized) {
        prepareUnauthorizedServiceResponse(serviceResponse);
        return serviceResponse;
      }

      Map<String, String> headerMap = new HashMap<>();
      headerMap.put("requestId", requestId);
      CustomHttpResponse customHttpResponse =
          post(restClientProperties.getCatalogUrl() + RestConstants.ITEM_URI, headerMap,
              JsonOutputFormatter.generateJson(itemRequest), requestId,
              RestConstants.EXTERNAL_SERVICE_CATALOG);
      serviceResponse = ObjectMapperUtils
          .convertStringToObject(customHttpResponse.getHttpResponseEntity(),
              ServiceResponse.class);
      serviceResponse.setPayload(serviceResponse.getPayload());
      serviceResponse.setStatus(serviceResponse.getStatus());
    } catch (JwtException e) {
      LOGGER.error("OrchestratorService::Token validation Error in get Item details: ", e);
      prepareUnauthorizedServiceResponse(serviceResponse);
    } catch (IOException | URISyntaxException e) {
      LOGGER.error("OrchestratorService::External Service Error in get Item details: ", e);
      prepareBadRequestResponse(serviceResponse, e.getMessage());
    } catch (Exception e) {
      LOGGER.error("OrchestratorService:: Internal Server Error in get Item details: ", e);
      prepareServerErrorResponse(serviceResponse);
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
        prepareUnauthorizedServiceResponse(serviceResponse);
        return serviceResponse;
      }
      MROrder order = orderRepository.save(checkoutRequest.getPayload());
      serviceResponse.setPayload(order);
    } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
      LOGGER.error("OrchestratorService::Token validation Error in checkout and save order: ", e);
      prepareUnauthorizedServiceResponse(serviceResponse);
    } catch (Exception e) {
      LOGGER.error("OrchestratorService:: Internal Server Error in checkout and save order: ", e);
      prepareServerErrorResponse(serviceResponse);
    }
    return serviceResponse;
  }

  public ServiceResponse<PaymentResponse> createPayment(
      ServiceRequest<PaymentRequest> paymentRequest, String bearerToken, String userId) {
    String requestId = UUID.randomUUID().toString();
    ServiceResponse<PaymentResponse> serviceResponse = new ServiceResponse<>();
    serviceResponse.setRequestId(requestId);
    try {
      boolean isAuthorized = authService.isValidUser(bearerToken, userId);
      if (!isAuthorized) {
        prepareUnauthorizedServiceResponse(serviceResponse);
        return serviceResponse;
      }

      Map<String, String> headerMap = new HashMap<>();
      headerMap.put("requestId", requestId);
      CustomHttpResponse customHttpResponse =
          post(restClientProperties.getPaymentUrl() + RestConstants.CREATE_PAYMENT_URI, headerMap,
              JsonOutputFormatter.generateJson(paymentRequest), requestId,
              RestConstants.EXTERNAL_SERVICE_PAYMENT);
      serviceResponse = ObjectMapperUtils
          .convertStringToObject(customHttpResponse.getHttpResponseEntity(),
              ServiceResponse.class);
      serviceResponse.setPayload(serviceResponse.getPayload());
      serviceResponse.setStatus(serviceResponse.getStatus());
    } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
      LOGGER.error("OrchestratorService::Token validation Error in create payment: ", e);
      prepareUnauthorizedServiceResponse(serviceResponse);
    } catch (IOException | URISyntaxException e) {
      LOGGER.error("OrchestratorService::External Service Error in create payment: ", e);
      prepareBadRequestResponse(serviceResponse, e.getMessage());
    } catch (Exception e) {
      LOGGER.error("OrchestratorService:: Internal Server Error in create payment: ", e);
      prepareServerErrorResponse(serviceResponse);
    }
    return serviceResponse;
  }

  public ServiceResponse<PaymentResponse> capturePayment(
      ServiceRequest<PaymentRequest> paymentRequest, String bearerToken, String userId) {

    String requestId = UUID.randomUUID().toString();
    ServiceResponse<PaymentResponse> serviceResponse = new ServiceResponse<>();
    serviceResponse.setRequestId(requestId);
    try {
      boolean isAuthorized = authService.isValidUser(bearerToken, userId);
      if (!isAuthorized) {
        prepareUnauthorizedServiceResponse(serviceResponse);
        return serviceResponse;
      }

      Map<String, String> headerMap = new HashMap<>();
      headerMap.put("requestId", requestId);
      CustomHttpResponse customHttpResponse =
          post(restClientProperties.getPaymentUrl() + RestConstants.COMPLETE_PAYMENT_URI, headerMap,
              JsonOutputFormatter.generateJson(paymentRequest), requestId,
              RestConstants.EXTERNAL_SERVICE_PAYMENT);
      serviceResponse = ObjectMapperUtils
          .convertStringToObject(customHttpResponse.getHttpResponseEntity(),
              ServiceResponse.class);
      serviceResponse.setPayload(serviceResponse.getPayload());
      serviceResponse.setStatus(serviceResponse.getStatus());
    } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
      LOGGER.error("OrchestratorService::Token validation Error in complete payment: ", e);
      prepareUnauthorizedServiceResponse(serviceResponse);
    } catch (IOException | URISyntaxException e) {
      LOGGER.error("OrchestratorService::External Service Error incomplete payment: ", e);
      prepareBadRequestResponse(serviceResponse, e.getMessage());
    } catch (Exception e) {
      LOGGER.error("OrchestratorService:: Internal Server Error in complete payment: ", e);
      prepareServerErrorResponse(serviceResponse);
    }
    return serviceResponse;
  }

}
