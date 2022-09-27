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
        for (HandlerAdapter handlerAdapter : handlerAdapters) {
            if (handlerAdapter.supports(object)) {
                return handlerAdapter;
            }
        }
        throw new NoSuchElementException("지원하지 않는 어댑터입니다.");
    }
}
