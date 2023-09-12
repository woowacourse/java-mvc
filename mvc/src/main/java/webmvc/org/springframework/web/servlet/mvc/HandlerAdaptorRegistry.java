package webmvc.org.springframework.web.servlet.mvc;

import java.util.List;
import java.util.Objects;

public class HandlerAdaptorRegistry {

    private final List<HandlerAdaptor> handlerAdaptors;

    public HandlerAdaptorRegistry(final List<HandlerAdaptor> handlerAdaptors) {
        this.handlerAdaptors = handlerAdaptors;
    }

    public void addHandlerAdaptor(HandlerAdaptor handlerAdaptor) {
        if (Objects.nonNull(this.handlerAdaptors)) {
            this.handlerAdaptors.add(handlerAdaptor);
        }
    }

    public HandlerAdaptor getHandlerAdaptor(Object handler) {
        for (HandlerAdaptor handlerAdaptor : handlerAdaptors) {
            if (handlerAdaptor.supports(handler)) {
                return handlerAdaptor;
            }
        }
        throw new IllegalArgumentException("Unsupported Handler Type");
    }
}
