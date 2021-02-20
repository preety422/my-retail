package com.retail.common.util;

import com.retail.common.constants.RestConstants;
import com.retail.common.model.Error;
import com.retail.common.model.ServiceResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ErrorResponseUtil {

  public static void prepareUnauthorizedServiceResponse(ServiceResponse<?> serviceResponse) {
    serviceResponse.setStatus(RestConstants.ERR_401);
    List<Error> errors = new ArrayList<>();
    Error error = new Error(RestConstants.ERR_401, RestConstants.UNAUTHORIZED);
    errors.add(error);
    serviceResponse.setErrors(errors);
  }


  public static void prepareServerErrorResponse(
      ServiceResponse<?> serviceResponse) {
    List<Error> errors = new ArrayList<>();
    Error error = new Error(RestConstants.ERR_500, RestConstants.ERR_500_MESSAGE);
    errors.add(error);
    serviceResponse.setErrors(errors);
    serviceResponse.setStatus(RestConstants.ERR_500);
  }

  public static void prepareBadRequestResponse(
      ServiceResponse<?> serviceResponse, String errMessage) {
    List<Error> errors = new ArrayList<>();
    Error error = new Error(RestConstants.ERR_400, errMessage);
    errors.add(error);
    serviceResponse.setErrors(errors);
    serviceResponse.setStatus(RestConstants.ERR_400);
  }

}
