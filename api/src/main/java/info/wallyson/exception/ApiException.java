package info.wallyson.exception;

import java.util.List;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiException extends RuntimeException {
  private final ApiError apiError;

  public ApiException(ApiError apiError) {
    this.apiError = apiError;
  }

  public static ApiException fromApiError(HttpStatus status, String message) {
    var api = new ApiError(status, message, List.of());
    return new ApiException(api);
  }

  public static ApiException fromApiError(HttpStatus status, String message, List<String> errors) {
    var api = new ApiError(status, message, errors);
    return new ApiException(api);
  }
}
