package webmvc.org.springframework.web.servlet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HandlerAdapters {

    private final List<HandlerAdapter> items = new ArrayList<>();

    public HandlerAdapters() {
        this(Collections.emptyList());
    }

    public HandlerAdapters(final List<HandlerAdapter> handlerAdapters) {
        this.items.addAll(handlerAdapters);
    }

    public void add(final HandlerAdapter handlerAdapter) {
        items.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(final Object handler) {
        return items.stream()
                .filter(handlerAdapter -> handlerAdapter.isSupport(handler))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("invalid handler!"));
    }
}
