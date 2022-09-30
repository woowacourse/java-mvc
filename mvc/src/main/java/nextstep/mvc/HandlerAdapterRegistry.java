package nextstep.mvc;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdapterRegistry {
    private final List<HandlerAdapter> adapters;

    public HandlerAdapterRegistry() {
        adapters = new ArrayList<>();
    }

    public void addHandlerAdapter(final HandlerAdapter adapter) {
        if (adapter != null) {
            adapters.add(adapter);
        }
    }

    public HandlerAdapter getAdapter(final Object handler) {
        return adapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(handler))
                .findFirst()
                .orElseThrow();
    }
}
