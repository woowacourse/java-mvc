package webmvc.org.springframework.web.servlet.mvc.tobe.handleradapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HandlerAdapters {
    private final List<HandlerAdapter> adapters;

    public HandlerAdapters() {
        this.adapters = new ArrayList<>();
    }

    public void init() {
        adapters.add(new AnnotationHandlerAdapter());
        adapters.add(new ManualHandlerAdapter());
    }

    public Optional<HandlerAdapter> getAdapter(final Object handler) {
        for (final HandlerAdapter handlerAdapter : adapters) {
            if (handlerAdapter.supports(handler)) {
                return Optional.of(handlerAdapter);
            }
        }
        return Optional.empty();
    }
}
