package webmvc.org.springframework.web.servlet.mvc.tobe.adapter;

import com.sun.jdi.InternalException;
import java.util.List;

public class HandlerAdapterFinder {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapterFinder(List<HandlerAdapter> handlerAdapters) {
        this.handlerAdapters = handlerAdapters;
    }

    public HandlerAdapter find(Object handler) {
        return handlerAdapters.stream()
            .filter(it -> it.supports(handler))
            .findFirst()
            .orElseThrow(InternalException::new);
    }
}
