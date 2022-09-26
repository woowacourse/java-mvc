package nextstep.mvc;

import java.util.ArrayList;
import java.util.List;
import nextstep.mvc.exception.HandlerAdapterNotFoundException;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapterRegistry() {
        this.handlerAdapters = new ArrayList<>();
    }

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        this.handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getAdapter(final Object handler) {
        return handlerAdapters.stream()
                .filter(it -> it.supports(handler))
                .findFirst()
                .orElseThrow(() -> new HandlerAdapterNotFoundException("사용할 수 있는 adapter가 없습니다."));
    }
}
