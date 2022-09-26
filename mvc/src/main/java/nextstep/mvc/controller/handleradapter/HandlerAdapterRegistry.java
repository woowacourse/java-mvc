package nextstep.mvc.controller.handleradapter;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapterRegistry() {
        this.handlerAdapters = new ArrayList<>();
    }

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(Object object) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(object))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(object + " Handler에 맞는 Adapter를 찾지 못했습니다."));
    }
}
