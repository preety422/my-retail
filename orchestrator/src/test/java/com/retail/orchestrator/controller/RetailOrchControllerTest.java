package com.retail.orchestrator.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.retail.common.model.ItemResponse;
import com.retail.common.model.ServiceResponse;
import com.retail.orchestrator.service.AuthService;
import com.retail.orchestrator.service.RetailOrchService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(PowerMockRunner.class)
@PrepareForTest({RetailOrchControllerTest.class})
public class RetailOrchControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Mock
  private AuthService authService;

  @Mock
  private RetailOrchService retailOrchService;

  @InjectMocks
  private RetailOrchController retailOrchController;

  @Before
  public void setup() {
    this.mockMvc = MockMvcBuilders.standaloneSetup(retailOrchController).build();
  }

  @Test
  public void createPayPalPaymentTestMockChargeResponse() throws Exception {
    Mockito.when(retailOrchService.getItemDetails(any(), anyString(), anyString()))
        .thenReturn(new ServiceResponse(new ItemResponse(), null, null, "200"));

    mockMvc
        .perform(post("/orch/item").content(getItemRequestJson())
            .header("authorization", "abc")
            .header("user", "user123")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.status").value("200"));
  }

  private String getItemRequestJson() {
    return "{\n"
        + "    \"payload\": {\n"
        + "        \"barcode\": \"12345678\",\n"
        + "        \"quantity\": 2\n"
        + "    }\n"
        + "}";
  }
}