package webmvc.org.springframework.web.servlet.mvc;

import java.util.List;

public class HandlerAdapters {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapters(final List<HandlerAdapter> handlerAdapters) {
        this.handlerAdapters = handlerAdapters;
    }

    public void add(final HandlerAdapter handlerAdapter) {
        this.handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getAdapter(final Object handler) {
        return handlerAdapters.stream()
                .filter(it -> it.isSupported(handler))
                .findFirst()
                .orElseThrow();
    }
}
