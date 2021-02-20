package com.retail.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObjectMapperUtils {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(ObjectMapperUtils.class);

  private ObjectMapperUtils() {
  }

  public static <T> T convertStringToObject(String string, Class<T> clazz) {
    ObjectMapper objectMapper = new ObjectMapper();
    T object = null;
    try {
      object = objectMapper.readValue(string, clazz);
    } catch (JsonProcessingException e) {
      LOGGER
          .error("Error in parsing to object from json string : {} is :", string, e);
    }
    return object;
  }

}