package webmvc.org.springframework.web.servlet.mvc.handler;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdapters {

    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(final Object handler) {
        for (final HandlerAdapter handlerAdapter : handlerAdapters) {
            if (handlerAdapter.support(handler)) {
                return handlerAdapter;
            }
        }
        throw new IllegalArgumentException("Handler Adapter Not Found");
    }
}
