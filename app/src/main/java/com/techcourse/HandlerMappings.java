package com.techcourse;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

public class HandlerMappings implements HandlerMapping {

  private final List<HandlerMapping> handlerMappings;

  public HandlerMappings(final List<HandlerMapping> handlerMappings) {
    this.handlerMappings = handlerMappings;
  }

  @Override
  public void initialize() {
    handlerMappings.forEach(HandlerMapping::initialize);
  }

  @Override
  public Object getHandler(final HttpServletRequest httpServletRequest) {
    for (final HandlerMapping handlerMapping : handlerMappings) {
      return handlerMapping.getHandler(httpServletRequest);
    }

    throw new IllegalArgumentException("요청을 처리할 수 있는 Handler가 존재하지 않습니다.");
  }
}
