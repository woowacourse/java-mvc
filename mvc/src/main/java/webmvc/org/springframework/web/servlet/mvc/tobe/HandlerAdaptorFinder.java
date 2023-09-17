package webmvc.org.springframework.web.servlet.mvc.tobe;

import java.util.List;

public class HandlerAdaptorFinder {

    private final List<HandlerAdaptor> handlerAdaptors;

    public HandlerAdaptorFinder(final List<HandlerAdaptor> handlerAdaptors) {
        this.handlerAdaptors = handlerAdaptors;
    }

    public HandlerAdaptor find(final Object handler) {
        return handlerAdaptors.stream()
            .filter(handlerAdaptor -> handlerAdaptor.support(handler))
            .findFirst()
            .orElseThrow();
    }
}
