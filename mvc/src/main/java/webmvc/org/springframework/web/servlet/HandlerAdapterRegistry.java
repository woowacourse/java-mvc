package webmvc.org.springframework.web.servlet;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapterRegistry(HandlerAdapter... handlerAdapters) {
        this.handlerAdapters = Arrays.stream(handlerAdapters)
                .collect(Collectors.toList());
    }

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(Object handler) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.canHandle(handler))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("%s에 적절한 어댑터를 찾을 수 없습니다.", handler)));
    }

}
