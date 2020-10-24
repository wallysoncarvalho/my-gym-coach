package info.wallyson.exception;

import java.util.HashMap;
import java.util.Objects;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    var violations = new HashMap<String, String>();
    for (var fieldError : ex.getBindingResult().getFieldErrors()) {
      violations.put(fieldError.getField(), fieldError.getDefaultMessage());
    }
    return ResponseEntity.badRequest().body(violations);
  }

  @Override
  protected ResponseEntity<Object> handleNoHandlerFoundException(
          NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

    var apiError = new ApiError(HttpStatus.BAD_REQUEST, "bro", "no endpoint found !");
    return ResponseEntity.status(apiError.getStatus()).body(null);
  }

//  @ExceptionHandler({MethodArgumentTypeMismatchException.class})
//  public ResponseEntity<ApiError> handleMethodArgumentTypeMismatch(
//      MethodArgumentTypeMismatchException ex, WebRequest request) {
//
//    var error =
//        ex.getName()
//            + " should be of type "
//            + Objects.requireNonNull(ex.getRequiredType(), "required type must not be null")
//                .getName();
//
//    var apiError = new ApiError(HttpStatus.BAD_REQUEST, "an error occurred with the inputs", error);
//    return ResponseEntity.badRequest().body(apiError);
//  }

//  @Override
//  protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
//      HttpMediaTypeNotSupportedException ex,
//      HttpHeaders headers,
//      HttpStatus status,
//      WebRequest request) {
//    var error =
//        String.format("Accepted content types for this resource: %s", ex.getSupportedMediaTypes());
//    var apiError = new ApiError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Unsuported media type", error);
//    return ResponseEntity.status(apiError.getStatus()).body(apiError);
//  }
}
