package com.techcourse.servlet.handler.adapter;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.NoSuchElementException;

public class HandlerAdapterRegistry {

    private static final List<HandlerAdapter> DEFAULT_HANDLER_ADAPTERS = List.of(
            new AnnotationHandlerAdapter(),
            new ControllerAdapter()
    );

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapterRegistry(List<HandlerAdapter> handlerAdapters) {
        this.handlerAdapters = handlerAdapters;
    }

    public HandlerAdapterRegistry() {
        this(DEFAULT_HANDLER_ADAPTERS);
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        return handlerAdapters.stream()
                .filter(adapter -> adapter.supports(handler))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("handler를 지원하는 adapter가 없습니다"))
                .handle(request, response, handler);
    }
}
