package web.org.springframework.web.bind.annotation;

import java.util.Arrays;

public enum RequestMethod {
  GET,
  HEAD,
  POST,
  PUT,
  PATCH,
  DELETE,
  OPTIONS,
  TRACE
  ;

  public static RequestMethod find(final String method) {
    return Arrays.stream(values())
        .filter(it -> it.name().equals(method.toUpperCase()))
        .findAny()
        .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 RequestMethod입니다."));
  }
}
