package webmvc.org.springframework.web.servlet.handler.adaptor;

import webmvc.org.springframework.web.servlet.mvc.HandlerAdaptor;
import java.util.List;
import java.util.Optional;

public class HandlerAdaptors {

    private final List<HandlerAdaptor> values;

    public HandlerAdaptors(final List<HandlerAdaptor> values) {
        this.values = values;
    }

    public Optional<HandlerAdaptor> findHandlerAdaptor(final Object handler) {
        return values.stream()
                .filter(value -> value.supports(handler))
                .findAny();
    }
}
