package nextstep.mvc;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class HandlerAdapterRegistry {
    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapterRegistry() {
        this.handlerAdapters = new ArrayList<>();
    }

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(Object handlerMapping) {
        return handlerAdapters.stream().filter(handlerAdapter -> handlerAdapter.supports(handlerMapping))
                .findFirst().orElseThrow(() -> new NoSuchElementException("요청을 처리할 수 있는 handlerAdapter 존재하지 않습니다."));
    }
}
