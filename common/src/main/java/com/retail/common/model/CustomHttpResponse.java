package com.retail.common.model;

import com.retail.common.util.JsonOutputFormatter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomHttpResponse {

  private Integer httpStatusCode;
  private String httpResponseEntity;

  @Override
  public String toString() {
    return JsonOutputFormatter.generateJson(this);
  }

}