package nextstep.mvc.handleradapter;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapterRegistry() {
        this.handlerAdapters = new ArrayList<>();
    }

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(Object handler) {
        return handlerAdapters.stream()
                .filter(hadt -> hadt.supports(handler))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("[ERROR] HandlerAdapter 가 존재하지 않습니다."));
    }
}
