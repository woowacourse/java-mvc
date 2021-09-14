package nextstep.mvc.adapter;

import nextstep.mvc.exeption.HandlerAdapterException;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapterRegistry() {
        this(new ArrayList<>());
    }

    public HandlerAdapterRegistry(List<HandlerAdapter> handlerAdapters) {
        this.handlerAdapters = handlerAdapters;
    }

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(Object handlerExecution) {
        for (HandlerAdapter handlerAdapter : handlerAdapters) {
            if (handlerAdapter.supports(handlerExecution)) {
                return handlerAdapter;
            }
        }
        throw new HandlerAdapterException("Handler Adapter를 찾을 수 없습니다. 입력 값: " + handlerExecution);
    }
}
