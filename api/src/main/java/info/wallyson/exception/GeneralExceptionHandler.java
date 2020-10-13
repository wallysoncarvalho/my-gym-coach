package info.wallyson.exception;

import java.util.HashMap;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class GeneralExceptionHandler {

  @ExceptionHandler({ApiException.class})
  public ResponseEntity<ApiError> handleApiError(ApiException ex) {
    var apiError = ex.getApiError();
    return ResponseEntity.status(apiError.getStatus()).body(apiError);
  }

  @ExceptionHandler({Exception.class})
  public ResponseEntity<ApiError> handleAll(Exception ex, WebRequest request) {
    var apiError =
        new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), "error occurred");

    return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiError> handleNoHandlerFound(NoHandlerFoundException e, WebRequest request) {
        var apiError = new ApiError(HttpStatus.NOT_FOUND, "bro", "no endpoint found !");
        return ResponseEntity.status(apiError.getStatus()).body(null);
    }

}
