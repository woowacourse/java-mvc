package webmvc.org.springframework.web.servlet.mvc.adapter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.exception.HandlerAdapterNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapterRegistry() {
        handlerAdapters = new ArrayList<>();
    }

    public void initialize() {
        handlerAdapters.add(new AnnotationHandlerAdapter());
    }

    public ModelAndView handle(final Object handler, final HttpServletRequest request, final HttpServletResponse response) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.isHandlerAdapter(handler))
                .findFirst()
                .map(handlerAdapter -> handlerAdapter.handle(handler, request, response))
                .orElseThrow(HandlerAdapterNotFoundException::new);
    }
}
