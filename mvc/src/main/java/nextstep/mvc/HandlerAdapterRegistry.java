package nextstep.mvc;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapterRegistry() {
        this(new ArrayList<>());
    }

    private HandlerAdapterRegistry(final List<HandlerAdapter> handlerAdapters) {
        this.handlerAdapters = handlerAdapters;
    }

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(final Object object) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(object))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("요청을 처리할 adapter가 존재하지 않습니다."));
    }
}
