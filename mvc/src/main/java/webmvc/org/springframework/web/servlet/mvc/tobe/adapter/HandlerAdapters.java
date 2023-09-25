package webmvc.org.springframework.web.servlet.mvc.tobe.adapter;

import java.util.ArrayList;
import java.util.List;
import webmvc.org.springframework.web.servlet.mvc.tobe.exception.HandlerAdapterNotFoundException;

public class HandlerAdapters {

    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(final Object handler) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.isSupport(handler))
                .findFirst()
                .orElseThrow(HandlerAdapterNotFoundException::new);
    }
}
