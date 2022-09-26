package nextstep.mvc;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class HandlerAdapterRegistry {

    private static final String NO_ADAPTER_ERROR_MESSAGE = "일치하는 어댑터가 없습니다.";

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapterRegistry() {
        this.handlerAdapters = new ArrayList<>();
    }

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    HandlerAdapter findAdapter(Object handler) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(handler))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(NO_ADAPTER_ERROR_MESSAGE));
    }
}
