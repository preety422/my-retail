package com.retail.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@UtilityClass
public class JsonOutputFormatter {

  private static final Logger LOGGER = LoggerFactory.getLogger(JsonOutputFormatter.class);

  public static String generateJson(Object obj) {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    try {
      return mapper.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      LOGGER.error(e.getMessage(), e);
      return null;
    }
  }

}