package nextstep.mvc.handeradapter;

import java.util.ArrayList;
import java.util.List;
import nextstep.mvc.common.exception.ErrorType;
import nextstep.mvc.common.exception.NotFoundHandlerAdapterException;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapterRegistry() {
        this.handlerAdapters = new ArrayList<>();
    }

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(final Object handler) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(handler))
                .findFirst()
                .orElseThrow(() -> new NotFoundHandlerAdapterException(ErrorType.NOT_FOUND_HANDLER_ADAPTER));
    }
}
