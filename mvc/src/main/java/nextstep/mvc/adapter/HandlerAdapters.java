package nextstep.mvc.adapter;

import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.exception.MvcException;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdapters {

    private final List<HandlerAdapter> adapters;

    public HandlerAdapters() {
        this(new ArrayList<>());
    }

    public HandlerAdapters(List<HandlerAdapter> adapters) {
        this.adapters = adapters;
    }

    public void add(HandlerAdapter handlerAdapter) {
        adapters.add(handlerAdapter);
    }

    public HandlerAdapter findAdapter(Object handler) {
        return adapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(handler))
                .findFirst()
                .orElseThrow(() -> new MvcException(
                        String.format("%s 타입 핸들러를 처리하는 어댑터가 없습니다.", handler.getClass().getSimpleName()))
                );
    }
}
