package nextstep.mvc.handler;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class HandlerAdapters {

    private final Set<HandlerAdapter> values;

    public HandlerAdapters(HandlerAdapter... handlerAdapters) {
        this.values = new HashSet<>();
        this.values.addAll(Arrays.asList(handlerAdapters));
    }

    public HandlerAdapter chooseProperAdapter(Object handler) {
        return values.stream()
            .filter(adapter -> adapter.supports(handler))
            .findAny()
            .orElseThrow();
    }
}
