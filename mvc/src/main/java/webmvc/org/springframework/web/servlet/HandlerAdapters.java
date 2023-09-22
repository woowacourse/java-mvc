package webmvc.org.springframework.web.servlet;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HandlerAdapters {
    private final List<HandlerAdapter> adapter = new ArrayList<>();

    public void add(final HandlerAdapter handlerAdapter) {
        adapter.add(handlerAdapter);
    }

    public HandlerAdapter getAdaptor(final Object handler) {
        return adapter.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(handler))
                .filter(Objects::nonNull)
                .findAny()
                .orElseThrow(IllegalStateException::new);
    }
}
