package info.wallyson.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {
  private static final ObjectMapper mapper = new ObjectMapper();

  public static String toJson(Object obj) throws JsonProcessingException {
    return mapper.writeValueAsString(obj);
  }
}
