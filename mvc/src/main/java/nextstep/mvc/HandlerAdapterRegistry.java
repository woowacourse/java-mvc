package nextstep.mvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    public HandlerAdapterRegistry() {
    }

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(Object handler) {
        handlerAdapters.stream()
                .filter(adapter -> adapter.supports(handler))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("올바른 요청이 아닙니다. HandlerAdapter Mapping Error!"));
    }
}
