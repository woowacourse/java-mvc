package webmvc.org.springframework.web.servlet.mvc.tobe.adapter;

import jakarta.servlet.ServletException;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdapters {

    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(Object handler) throws ServletException {
        return handlerAdapters.stream()
                .filter(it -> it.supports(handler))
                .findAny()
                .orElseThrow(() -> new ServletException("handlerAdapter not found for handler: " + handler));
    }
}
