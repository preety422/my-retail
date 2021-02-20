
package com.retail.common.model;

import com.retail.common.util.JsonOutputFormatter;
import javax.validation.Valid;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author vrawal
 *
 */

@Getter
@Setter
@NoArgsConstructor
public class ServiceRequest<T> {

  @Valid
  private T payload;

  @Override
  public String toString() {
    return JsonOutputFormatter.generateJson(this);
  }

}
