package webmvc.org.springframework.web.servlet.mvc.adapter;

import java.util.List;
import webmvc.org.springframework.web.servlet.mvc.exception.HandlerAdapterException;

public class HandlerAdapters {

    private final List<HandlerAdapter> adapters;

    public HandlerAdapters() {
        this.adapters = List.of(
            new AnnotationHandlerAdapter()
        );
    }

    public HandlerAdapter getHandlerAdapter(final Object handler) {
        return adapters.stream()
            .filter(adapter -> adapter.supports(handler))
            .findFirst()
            .orElseThrow(HandlerAdapterException.NotFoundException::new);
    }
}
