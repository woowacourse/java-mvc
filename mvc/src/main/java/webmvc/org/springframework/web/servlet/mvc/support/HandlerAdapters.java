package webmvc.org.springframework.web.servlet.mvc.support;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import webmvc.org.springframework.web.servlet.mvc.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.exception.HandlerAdapterNotFoundException;

public class HandlerAdapters {

    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    @Nonnull
    public HandlerAdapter getHandlerAdapter(Object handler) {
        return handlerAdapters.stream()
            .filter(adapter -> adapter.supports(handler))
            .findAny()
            .orElseThrow(() -> new HandlerAdapterNotFoundException("handler adapter not found! handler: " + handler));
    }
}
