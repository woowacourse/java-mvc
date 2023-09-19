package webmvc.org.springframework.web.servlet;

import java.util.List;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapterRegistry(List<HandlerAdapter> handlerAdapters) {
        this.handlerAdapters = handlerAdapters;
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
