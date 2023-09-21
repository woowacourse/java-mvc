package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

public class HandlerAdapters implements HandlerAdapter {

  private final List<HandlerAdapter> handlerAdapters;

  public HandlerAdapters(final List<HandlerAdapter> handlerAdapters) {
    this.handlerAdapters = handlerAdapters;
  }

  @Override
  public boolean isSupport(final Object handler) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Object handle(
      final HttpServletRequest request,
      final HttpServletResponse response,
      final Object handler
  ) {
    return handlerAdapters.stream()
        .filter(handlerAdapter -> handlerAdapter.isSupport(handler))
        .map(handlerAdapter -> handlerAdapter.handle(request, response, handler))
        .findAny()
        .orElseThrow(() -> new IllegalArgumentException("해당 요청은 처리할 수 없습니다."));
  }
}
