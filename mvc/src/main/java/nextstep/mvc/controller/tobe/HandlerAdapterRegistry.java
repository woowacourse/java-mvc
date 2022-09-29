package nextstep.mvc.controller.tobe;

import java.util.ArrayList;
import java.util.List;
import nextstep.mvc.HandlerAdapter;

public class HandlerAdapterRegistry {
    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapterRegistry() {
        this.handlerAdapters = new ArrayList<>();
    }

    public void add(final HandlerAdapter handlerMapping) {
        handlerAdapters.add(handlerMapping);
    }

    public HandlerAdapter getAdapter(Object handler) {
        return handlerAdapters.stream()
                .filter(it -> it.supports(handler))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 handler adapter입니다."));
    }
}
