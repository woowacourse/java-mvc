package nextstep.mvc.controller.tobe;

import java.util.ArrayList;
import java.util.List;
import nextstep.mvc.HandlerAdapter;

public class HandlerAdapterRegister {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapterRegister() {
        this.handlerAdapters = new ArrayList<>();
    }

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(Object handler) {
        for (HandlerAdapter handlerAdapter : handlerAdapters) {
            if (handlerAdapter.supports(handler)) {
                return handlerAdapter;
            }
        }
        throw new IllegalStateException("HandlerAdapter not found");
    }
}
