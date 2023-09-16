package webmvc.org.springframework.web.servlet.mvc.tobe;

import java.util.List;

public class HandlerAdapterFinder {

    private final List<HandlerAdapter> handlerAdapters;


    public HandlerAdapterFinder(List<HandlerAdapter> handlerAdapters) {
        this.handlerAdapters = handlerAdapters;
    }

    public HandlerAdapter find(Object handler)  {
        return handlerAdapters.stream()
            .filter(it -> it.supports(handler))
            .findFirst()
            .orElseGet(null);
    }
}
