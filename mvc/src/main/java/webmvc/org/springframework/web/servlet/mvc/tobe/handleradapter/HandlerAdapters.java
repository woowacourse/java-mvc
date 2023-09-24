package webmvc.org.springframework.web.servlet.mvc.tobe.handleradapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import webmvc.org.springframework.web.servlet.mvc.tobe.exception.HandlerAdapterNotExistException;

public class HandlerAdapters {

    private final List<HandlerAdapter> adapters = new ArrayList<>();

    public void addAdapter(final HandlerAdapter handlerAdapter) {
        adapters.add(handlerAdapter);
    }
    
    public HandlerAdapter getFirstHandleableAdapterForHandler(final Object handler) {
        final List<HandlerAdapter> handleableAdaptersForHandler = getHandleableAdaptersForHandler(handler);
        if (handleableAdaptersForHandler.isEmpty()) {
            throw new HandlerAdapterNotExistException();
        }
        return handleableAdaptersForHandler.get(0);
    }

    private List<HandlerAdapter> getHandleableAdaptersForHandler(final Object handler) {
        return adapters.stream()
            .filter(adapter -> adapter.isHandleable(handler))
            .collect(Collectors.toList());
    }
}
