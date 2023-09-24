package webmvc.org.springframework.web.servlet.mvc.adapter;

import java.util.List;

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
            .orElseThrow(() -> new IllegalArgumentException("적합한 핸들러 어댑터가 없습니다."));
    }
}
