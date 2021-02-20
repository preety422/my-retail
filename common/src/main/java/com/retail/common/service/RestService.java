package com.retail.common.service;

import com.retail.common.model.CustomHttpResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeoutException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpServerErrorException;


public class RestService {

  private static final Logger LOGGER = LoggerFactory.getLogger(RestService.class);


  public CustomHttpResponse post(String url, Map<String, String> requestHeaders, String requestBody,
      String identifier, String externalService) throws URISyntaxException, IOException {

    HttpPost request = new HttpPost(new URIBuilder(url).build());
    addDefaultHeaders(request, requestHeaders);
    if (Objects.nonNull(requestBody)) {
      HttpEntity entity = new ByteArrayEntity(requestBody.getBytes(StandardCharsets.UTF_8));
      request.setEntity(entity);
    }
    String reqJson =
        !Objects.isNull(request.getEntity()) ? EntityUtils.toString(request.getEntity()) : url;
    LOGGER.info("Retail Orchestrator to {}: url: {} POST requestBody: {} for identifier {} ",
        externalService, url,
        reqJson, identifier);

    return executeRestCall(url, identifier, externalService,
        request);
  }

  public CustomHttpResponse get(String url, Map<String, String> requestHeaders,
      Map<String, String> qParams, String identifier,
      String externalService)
      throws IOException, URISyntaxException {

    HttpGet request = new HttpGet(getURIFromBuilder(url, qParams));
    addDefaultHeaders(request, requestHeaders);
    LOGGER
        .info("Retail Orchestrator to {}: url: {} GET request {} for identifier {} ",
            externalService, url, request,
            identifier);

    return executeRestCall(url, identifier, externalService,
        request);
  }

  private URI getURIFromBuilder(String url, Map<String, String> qParams) throws URISyntaxException {
    URIBuilder uriBuilder = new URIBuilder(url);
    if (!Objects.isNull(qParams) && !qParams.isEmpty()) {
      for (Map.Entry<String, String> qParam : qParams.entrySet()) {
        uriBuilder.addParameter(qParam.getKey(), qParam.getValue());
      }
    }
    return uriBuilder.build();
  }


  private void addDefaultHeaders(HttpRequestBase request, Map<String, String> requestHeaders) {
    if (Objects.nonNull(requestHeaders)) {
      requestHeaders.forEach(request::addHeader);
    }
    request.addHeader("Content-Type", "application/json");
  }

  private CustomHttpResponse executeRestCall(String url, String identifier,
      String externalService,
      HttpRequestBase request)
      throws IOException {
    CustomHttpResponse customHttpResponse;

    long startTime = 0;
    try {

      RequestConfig config = RequestConfig.custom()
          .setConnectTimeout(10 * 1000)
          .setSocketTimeout(3600 * 1000).build();
      HttpClient httpClient =
          HttpClientBuilder.create().setDefaultRequestConfig(config).build();

      startTime = System.currentTimeMillis();
      HttpResponse response = httpClient.execute(request);
      customHttpResponse = new CustomHttpResponse(
          response.getStatusLine().getStatusCode(), EntityUtils.toString(response.getEntity()));
      LOGGER.info(
          "{} to Retail Orchestrator: url:{} responseBody: {}, responseCode: {} for identifier {}. Time taken: {}",
          externalService, url, customHttpResponse.getHttpResponseEntity(),
          customHttpResponse.getHttpStatusCode(), identifier,
          (System.currentTimeMillis() - startTime));

    } catch (Exception e) {
      if (e.getCause() instanceof TimeoutException) {

        LOGGER.error("Timout exception occurred for API call for url: {}, identifier: {}",
            url, MDC.get("IDENTIFIER"), e);
        throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR,
            "Something went wrong!");
      } else {
        throw e;
      }
    }
    return customHttpResponse;
  }

}
