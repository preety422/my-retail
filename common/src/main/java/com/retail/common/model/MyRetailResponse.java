package com.retail.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;


@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(
    ignoreUnknown = true
)
@Getter
@Setter
public class MyRetailResponse implements Serializable {

  private static final long serialVersionUID = -3710354647100630087L;
  private Date timestamp;
  private String error;
  private Integer status;
  private String message;
  private String path;
}