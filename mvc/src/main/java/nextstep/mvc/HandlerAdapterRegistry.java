package nextstep.mvc;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> adapters;

    public HandlerAdapterRegistry() {
        this.adapters = new ArrayList<>();
    }

    public void addHandlerAdapter(final HandlerAdapter adapter) {
        adapters.add(adapter);
    }

    public HandlerAdapter getHandlerAdapter(final Object handler) {
        return adapters.stream()
                .filter(adapter -> adapter.supports(handler))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(handler.getClass().getName() + "는 처리할 수 없는 핸들러 타입 입니다.")
                );
    }
}
