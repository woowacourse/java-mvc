package nextstep.mvc.controller.tobe;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import nextstep.mvc.HandlerAdapter;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    public void add(final HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(final Object object) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(object))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("지원하지 않는 어댑터입니다."));
    }
}
