
package com.retail.common.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.retail.common.util.JsonOutputFormatter;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)
@Getter
@Setter
public class ServiceResponse<T> {

  private String status;
  private List<Error> errors;
  private Map<String, Object> metadata;
  private String requestId;
  private T payload;

  public ServiceResponse() {
    super();
  }

  public ServiceResponse(T payload, List<Error> errors, Map<String, Object> metadata,
      String status) {
    this.status = status;
    this.errors = errors;
    this.metadata = metadata;
    this.payload = payload;
  }

  @Override
  public String toString() {
    return JsonOutputFormatter.generateJson(this);
  }

}