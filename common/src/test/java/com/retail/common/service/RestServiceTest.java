package com.retail.common.service;

import com.retail.common.model.CustomHttpResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.web.client.HttpServerErrorException;


@RunWith(PowerMockRunner.class)
@PrepareForTest({RestService.class, HttpClientBuilder.class,
    EntityUtils.class})
@PowerMockIgnore({"javax.net.ssl.*"})
public class RestServiceTest {


  @InjectMocks
  private RestService restService = Mockito.mock(
      RestService.class,
      Mockito.CALLS_REAL_METHODS);


  @Test
  public void verifyPost() throws IOException, URISyntaxException {
    HttpClientBuilder httpClientBuilder = Mockito.mock(HttpClientBuilder.class);
    PowerMockito.mockStatic(HttpClientBuilder.class);
    Mockito.when(HttpClientBuilder.create()).thenReturn(httpClientBuilder);
    CloseableHttpClient httpClient = PowerMockito.mock(CloseableHttpClient.class);
    Mockito.when(HttpClientBuilder.create().build()).thenReturn(httpClient);
    CloseableHttpResponse response = PowerMockito.mock(CloseableHttpResponse.class);
    StatusLine statusLine = PowerMockito.mock(StatusLine.class);
    Mockito.when(response.getStatusLine()).thenReturn(statusLine);
    Mockito.when(statusLine.getStatusCode()).thenReturn(200);
    Mockito.when(httpClient.execute(Mockito.any())).thenReturn(response);
    PowerMockito.mockStatic(EntityUtils.class);
    String reqBody = "";
    String respString = "";
    PowerMockito.when(EntityUtils.toString(Mockito.any())).thenReturn(respString);

    Map<String, String> requestHeaders = new HashMap<>();
    restService.post("url", requestHeaders, reqBody,
        "identifier", "Catalogue");
    Mockito.verify(httpClient, Mockito.times(1))
        .execute(Mockito.any());
  }

  @Test
  public void verifyNullPost() throws IOException, URISyntaxException {
    HttpClientBuilder httpClientBuilder = Mockito.mock(HttpClientBuilder.class);
    PowerMockito.mockStatic(HttpClientBuilder.class);
    Mockito.when(HttpClientBuilder.create()).thenReturn(httpClientBuilder);
    CloseableHttpClient httpClient = PowerMockito.mock(CloseableHttpClient.class);
    Mockito.when(HttpClientBuilder.create().build()).thenReturn(httpClient);
    CloseableHttpResponse response = PowerMockito.mock(CloseableHttpResponse.class);
    StatusLine statusLine = PowerMockito.mock(StatusLine.class);
    Mockito.when(response.getStatusLine()).thenReturn(statusLine);
    Mockito.when(statusLine.getStatusCode()).thenReturn(200);
    Mockito.when(httpClient.execute(Mockito.any())).thenReturn(response);
    PowerMockito.mockStatic(EntityUtils.class);
    String reqBody = null;
    PowerMockito.when(EntityUtils.toString(Mockito.any())).thenReturn(null);

    Map<String, String> requestHeaders = new HashMap<>();
    restService.post("url", requestHeaders, reqBody,
        "identifier", "Catalogue");
    Mockito.verify(httpClient, Mockito.times(1))
        .execute(Mockito.any());
  }

  @Test(expected = HttpServerErrorException.class)
  public void verifyPostTimeoutExceptionTest() throws IOException, URISyntaxException {
    HttpClientBuilder httpClientBuilder = Mockito.mock(HttpClientBuilder.class);
    PowerMockito.mockStatic(HttpClientBuilder.class);
    Mockito.when(HttpClientBuilder.create()).thenReturn(httpClientBuilder);
    CloseableHttpClient httpClient = PowerMockito.mock(CloseableHttpClient.class);
    Mockito.when(HttpClientBuilder.create().build()).thenReturn(httpClient);
    Exception exception = new Exception("TimeoutException", new TimeoutException());
    Mockito.when(httpClient.execute(Mockito.any())).thenAnswer(invocation -> {
      throw exception;
    });
    String reqBody = "";
    Map<String, String> requestHeaders = new HashMap<>();
    CustomHttpResponse resp = restService.post("url", requestHeaders, reqBody,
        "identifier", "Catalogue");
    Mockito.verify(httpClient, Mockito.times(1))
        .execute(Mockito.any());
    Assert.assertEquals(Integer.valueOf(404), resp.getHttpStatusCode());

  }

  @Test(expected = RuntimeException.class)
  public void verifyPostExceptionTest() throws IOException, URISyntaxException {
    HttpClientBuilder httpClientBuilder = Mockito.mock(HttpClientBuilder.class);
    PowerMockito.mockStatic(HttpClientBuilder.class);
    Mockito.when(HttpClientBuilder.create()).thenReturn(httpClientBuilder);
    CloseableHttpClient httpClient = PowerMockito.mock(CloseableHttpClient.class);
    Mockito.when(HttpClientBuilder.create().build()).thenReturn(httpClient);
    Mockito.when(httpClient.execute(Mockito.any())).thenAnswer(invocation -> {
      throw new RuntimeException();
    });
    String reqBody = "";
    Map<String, String> requestHeaders = new HashMap<>();
    CustomHttpResponse resp = restService.post("url", requestHeaders, reqBody,
        "identifier", "Catalogue");
    Mockito.verify(httpClient, Mockito.times(1))
        .execute(Mockito.any());
    Assert.assertEquals(Integer.valueOf(404), resp.getHttpStatusCode());
  }

  @Test
  public void verifyGet() throws IOException, URISyntaxException {
    HttpClientBuilder httpClientBuilder = Mockito.mock(HttpClientBuilder.class);
    PowerMockito.mockStatic(HttpClientBuilder.class);
    Mockito.when(HttpClientBuilder.create()).thenReturn(httpClientBuilder);
    CloseableHttpClient httpClient = PowerMockito.mock(CloseableHttpClient.class);
    Mockito.when(HttpClientBuilder.create().build()).thenReturn(httpClient);
    CloseableHttpResponse response = PowerMockito.mock(CloseableHttpResponse.class);
    StatusLine statusLine = PowerMockito.mock(StatusLine.class);
    Mockito.when(response.getStatusLine()).thenReturn(statusLine);
    Mockito.when(statusLine.getStatusCode()).thenReturn(200);
    Mockito.when(httpClient.execute(Mockito.any())).thenReturn(response);
    PowerMockito.mockStatic(EntityUtils.class);
    String respString = "";
    PowerMockito.when(EntityUtils.toString(Mockito.any())).thenReturn(respString);
    Map<String, String> requestHeaders = new HashMap<>();
    Map<String, String> qParams = new HashMap<>();
    qParams.put("header", "value");
    restService.get("url", requestHeaders, qParams,
        "identifier", "Catalogue");
    Mockito.verify(httpClient, Mockito.times(1))
        .execute(Mockito.any());
  }

  @Test
  public void verifyGet2() throws IOException, URISyntaxException {
    HttpClientBuilder httpClientBuilder = Mockito.mock(HttpClientBuilder.class);
    PowerMockito.mockStatic(HttpClientBuilder.class);
    Mockito.when(HttpClientBuilder.create()).thenReturn(httpClientBuilder);
    CloseableHttpClient httpClient = PowerMockito.mock(CloseableHttpClient.class);
    Mockito.when(HttpClientBuilder.create().build()).thenReturn(httpClient);
    CloseableHttpResponse response = PowerMockito.mock(CloseableHttpResponse.class);
    StatusLine statusLine = PowerMockito.mock(StatusLine.class);
    Mockito.when(response.getStatusLine()).thenReturn(statusLine);
    Mockito.when(statusLine.getStatusCode()).thenReturn(200);
    Mockito.when(httpClient.execute(Mockito.any())).thenReturn(response);
    PowerMockito.mockStatic(EntityUtils.class);
    String respString = "";
    PowerMockito.when(EntityUtils.toString(Mockito.any())).thenReturn(respString);

    Map<String, String> requestHeaders = new HashMap<>();
    restService.get("url", requestHeaders, null,
        "identifier", "Catalogue");
    Mockito.verify(httpClient, Mockito.times(1))
        .execute(Mockito.any());
  }

  @Test
  public void verifyGetNotFound() throws IOException, URISyntaxException {
    HttpClientBuilder httpClientBuilder = Mockito.mock(HttpClientBuilder.class);
    PowerMockito.mockStatic(HttpClientBuilder.class);
    Mockito.when(HttpClientBuilder.create()).thenReturn(httpClientBuilder);
    CloseableHttpClient httpClient = PowerMockito.mock(CloseableHttpClient.class);
    Mockito.when(HttpClientBuilder.create().build()).thenReturn(httpClient);
    CloseableHttpResponse response = PowerMockito.mock(CloseableHttpResponse.class);
    StatusLine statusLine = PowerMockito.mock(StatusLine.class);
    Mockito.when(response.getStatusLine()).thenReturn(statusLine);
    Mockito.when(statusLine.getStatusCode()).thenReturn(404);
    Mockito.when(httpClient.execute(Mockito.any())).thenReturn(response);
    PowerMockito.mockStatic(EntityUtils.class);
    String respString = "{\n    \"code\": \"PSP-201\",\n    \"message\": \"Unsuccessful ARS request due to: Unknown host\"\n}{\n  \"basket_header\": {\n    \"source_name\": \"Scan&Go\",\n    \"source_id\": \"123456\",\n    \"include_promotion_flag\": \"CALCULATED\"\n  },\n  \"item_entries\": [\n    {\n      \"barcode\": \"00000006771638\",\n      \"quantity\": 1,\n      \"entry_method\": \"SCANNED\"\n    }\n  ]\n}";
    PowerMockito.when(EntityUtils.toString(Mockito.any())).thenReturn(respString);

    Map<String, String> requestHeaders = new HashMap<>();
    CustomHttpResponse resp = restService.get("url", requestHeaders, null,
        "identifier", "Catalogue");
    Assert.assertEquals(Integer.valueOf(404), resp.getHttpStatusCode());
  }


  @Test(expected = HttpServerErrorException.class)
  public void verifyGetTimeoutExceptionTest() throws IOException, URISyntaxException {
    HttpClientBuilder httpClientBuilder = Mockito.mock(HttpClientBuilder.class);
    PowerMockito.mockStatic(HttpClientBuilder.class);
    Mockito.when(HttpClientBuilder.create()).thenReturn(httpClientBuilder);
    CloseableHttpClient httpClient = PowerMockito.mock(CloseableHttpClient.class);
    Mockito.when(HttpClientBuilder.create().build()).thenReturn(httpClient);
    Exception exception = new Exception("TimeoutException", new TimeoutException());
    Mockito.when(httpClient.execute(Mockito.any())).thenAnswer(invocation -> {
      throw exception;
    });

    Map<String, String> requestHeaders = new HashMap<>();
    Map<String, String> qParams = new HashMap<>();
    CustomHttpResponse resp = restService.get("url", requestHeaders, qParams,
        "identifier", "Catalogue");
    Assert.assertEquals(Integer.valueOf(404), resp.getHttpStatusCode());
  }

  @Test(expected = RuntimeException.class)
  public void verifyGetExceptionTest() throws IOException, URISyntaxException {
    HttpClientBuilder httpClientBuilder = Mockito.mock(HttpClientBuilder.class);
    PowerMockito.mockStatic(HttpClientBuilder.class);
    Mockito.when(HttpClientBuilder.create()).thenReturn(httpClientBuilder);
    CloseableHttpClient httpClient = PowerMockito.mock(CloseableHttpClient.class);
    Mockito.when(HttpClientBuilder.create().build()).thenReturn(httpClient);
    Mockito.when(httpClient.execute(Mockito.any())).thenAnswer(invocation -> {
      throw new RuntimeException();
    });

    Map<String, String> requestHeaders = new HashMap<>();
    Map<String, String> qParams = new HashMap<>();
    CustomHttpResponse resp = restService.get("url", requestHeaders, qParams,
        "identifier", "Catalogue");
    Assert.assertEquals(Integer.valueOf(404), resp.getHttpStatusCode());

  }
}