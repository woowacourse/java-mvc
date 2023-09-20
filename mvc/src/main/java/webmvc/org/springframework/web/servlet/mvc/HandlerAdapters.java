package webmvc.org.springframework.web.servlet.mvc;

import java.util.HashSet;
import java.util.Set;

public class HandlerAdapters {

    private final Set<HandlerAdapter> handlerAdapters;

    public HandlerAdapters() {
        this(new HashSet<>());
    }

    public HandlerAdapters(final Set<HandlerAdapter> handlerAdapters) {
        this.handlerAdapters = handlerAdapters;
    }

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        this.handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(final Object handler) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(handler))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 handler 입니다"));
    }
}
