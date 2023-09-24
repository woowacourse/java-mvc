package webmvc.org.springframework.web.servlet.mvc.tobe;

import java.util.HashSet;
import java.util.Set;

public class HandlerAdapters {

    private Set<HandlerAdapter> values = new HashSet<>();

    public void add(final HandlerAdapter handlerAdapter) {
        values.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(Object handler) {
        return values.stream()
                .filter(adapter -> adapter.supports(handler))
                .findAny()
                .orElseThrow();
    }
}
