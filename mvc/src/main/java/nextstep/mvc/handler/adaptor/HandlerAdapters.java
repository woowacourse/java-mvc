package nextstep.mvc.handler.adaptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HandlerAdapters {

    private final List<HandlerAdapter> values;

    public HandlerAdapters() {
        this.values = new ArrayList<>();
    }

    public HandlerAdapters(List<HandlerAdapter> values) {
        this.values = values;
    }

    public void add(HandlerAdapter handlerAdapter) {
        this.values.add(handlerAdapter);
    }

    public Optional<HandlerAdapter> get(Object handler) {
        return values.stream()
            .filter(ha -> ha.supports(handler))
            .findAny()
            .or(Optional::empty);
    }
}
