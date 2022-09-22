package nextstep.mvc;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdapterRegistry {
    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapterRegistry() {
        handlerAdapters = new ArrayList<>();
    }

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(final Object handler) {
        return handlerAdapters.stream()
            .filter(adapter -> adapter.supports(handler))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("해당 핸들러를 처리할 수 있는 핸들러 어댑터를 찾지 못했습니다."));
    }

}
