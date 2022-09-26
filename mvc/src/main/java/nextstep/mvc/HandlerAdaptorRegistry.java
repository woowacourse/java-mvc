package nextstep.mvc;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdaptorRegistry {

    private final List<HandlerAdaptor> handlerAdaptors;

    public HandlerAdaptorRegistry() {
        this.handlerAdaptors = new ArrayList<>();
    }

    public void addHandlerAdaptor(HandlerAdaptor handlerAdapter) {
        handlerAdaptors.add(handlerAdapter);
    }

    public HandlerAdaptor getHandlerAdaptor(Object handler) {
        return handlerAdaptors.stream()
            .filter(it -> it.supports(handler))
            .findFirst()
            .orElseThrow();
    }
}
