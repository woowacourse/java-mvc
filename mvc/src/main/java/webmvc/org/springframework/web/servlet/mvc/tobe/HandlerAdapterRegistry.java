package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import webmvc.org.springframework.web.servlet.mvc.asis.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.asis.HandlerMapping;

import java.util.List;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapterRegistry(final List<HandlerAdapter> handlerAdapters) {
        this.handlerAdapters = handlerAdapters;
    }

    public HandlerAdapter getHandlerAdapter(final HttpServletRequest request, final HandlerMapping handlerMapping) {
        final Object handler = handlerMapping.getHandler(request);
        return handlerAdapters.stream()
                              .filter(handlerAdapter -> handlerAdapter.supports(handler))
                              .findAny()
                              .orElseThrow(() -> new IllegalArgumentException("일치하는 handlerAdapter가 없습니다."));
    }
}
