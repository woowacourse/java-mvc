package webmvc.org.springframework.web.servlet.mvc.handler_adapter;

import java.util.HashSet;
import java.util.Set;

public class HandlerAdapterRegistry {

    private final Set<HandlerAdapter> handlerAdapters;

    public HandlerAdapterRegistry(Set<HandlerAdapter> handlerAdapters) {
        this.handlerAdapters = new HashSet<>(handlerAdapters);
    }

    public HandlerAdapter getHandlerAdapter(Object handler) {
        return handlerAdapters.stream()
                .filter(it -> it.support(handler))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("핸들러를 처리할 어댑터가 없습니다."));
    }
}
