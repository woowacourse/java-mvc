package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

public class HandlerMappings implements HandlerMapping {

  private final List<HandlerMapping> handlerMappings;

  public HandlerMappings(final List<HandlerMapping> handlerMappings) {
    this.handlerMappings = handlerMappings;

    initialize();
  }

  @Override
  public void initialize() {
    handlerMappings.forEach(HandlerMapping::initialize);
  }

  @Override
  public Object getHandler(final HttpServletRequest httpServletRequest) {
    return handlerMappings.stream()
        .map(handlerMapping -> handlerMapping.getHandler(httpServletRequest))
        .filter(Objects::nonNull)
        .findAny()
        .orElseThrow(() -> new IllegalArgumentException("요청을 처리할 수 있는 Handler가 존재하지 않습니다."));
  }
}
