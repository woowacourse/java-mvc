package nextstep.mvc;

import javassist.NotFoundException;

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

    public HandlerAdapter getAdapter(Object handler) throws NotFoundException {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(handler))
                .findAny()
                .orElseThrow(() -> new NotFoundException("어댑터 찾을 수 없음."));
    }
}
