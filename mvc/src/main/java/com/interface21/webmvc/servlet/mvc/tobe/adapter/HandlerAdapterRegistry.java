package com.interface21.webmvc.servlet.mvc.tobe.adapter;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdapterRegistry {

    private  final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    public void addHandlerAdapter(HandlerAdapter adapter) {
        handlerAdapters.add(adapter);
    }

    public HandlerAdapter getHandlerAdapter(Object handler) {
        return handlerAdapters.stream()
                .filter(adapter -> adapter.supports(handler))
                .findFirst()
                .orElseThrow(() -> new NoHandlerAdapterFoundException(
                        "핸들러 [" + handler.getClass().getName() + "] 를 실행할 수 있는 어댑터가 없습니다."
                ));
    }
}
