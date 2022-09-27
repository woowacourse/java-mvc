package nextstep.mvc;

import java.util.List;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapterRegistry(List<HandlerAdapter> handlerAdapters) {
        this.handlerAdapters = handlerAdapters;
    }

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        this.handlerAdapters.add(handlerAdapter);
    }

    public Object getHandlerAdapter(Object handler) {
        return handlerAdapters.stream()
                .filter(each -> each.supports(handler))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 핸들러를 지원하는 어댑터가 없습니다."));
    }
}
