package nextstep.mvc.controller.tobe;

import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.exception.AdapterNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdapters {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapters() {
        this.handlerAdapters = new ArrayList<>();
    }

    public void add(HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(HandlerExecution handlerExecution) throws AdapterNotFoundException {
        return handlerAdapters.stream()
                .filter(adapter -> adapter.supports(handlerExecution))
                .findAny()
                .orElseThrow(AdapterNotFoundException::new);
    }
}
