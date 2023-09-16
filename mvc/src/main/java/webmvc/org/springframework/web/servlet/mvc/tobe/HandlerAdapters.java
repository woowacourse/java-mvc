package webmvc.org.springframework.web.servlet.mvc.tobe;

import java.util.List;

public class HandlerAdapters {

    private final List<HandlerAdapter> handlerAdapters;


    public HandlerAdapters(List<HandlerAdapter> handlerAdapters) {
        this.handlerAdapters = handlerAdapters;
    }

    public HandlerAdapter findAdapter(Object handler)  {
        return handlerAdapters.stream()
            .filter(it -> it.supports(handler))
            .findFirst()
            .orElseGet(null);
    }
}
