package nextstep.mvc;

import java.util.ArrayList;
import java.util.List;
import nextstep.mvc.controller.HandlerExecution;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();


    public void add(HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandler(HandlerExecution handler) {
        return handlerAdapters.stream()
                .filter(adapter -> adapter.supports(handler))
                .findFirst()
                .orElseThrow();
    }
}
