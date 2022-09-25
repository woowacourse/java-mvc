package nextstep.mvc;

import java.util.List;
import java.util.NoSuchElementException;

public class HandlerAdapterRegistry {

    private List<HandlerAdapter> handlerAdapters;

    public HandlerAdapter getAdapter(final Object handler) {
        return handlerAdapters.stream()
                .filter(it -> it.supports(handler))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("handler 를 지원하는 handlerAdapter 를 찾을 수 없습니다."));
    }

    public void add(final HandlerAdapter handlerAdapter) {
        this.handlerAdapters.add(handlerAdapter);
    }
}
