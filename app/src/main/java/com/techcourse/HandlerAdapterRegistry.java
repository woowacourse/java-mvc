package com.techcourse;

import com.techcourse.exception.NotFoundHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.HandlerAdapter;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdapterRegistry {

    final private List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(final Object object) {
        return handlerAdapters.stream()
                              .filter(handlerAdapter -> handlerAdapter.isSupport(object))
                              .findFirst()
                              .orElseThrow(() -> new NotFoundHandlerAdapter("해당 핸들러 어댑터를 찾지 못했습니다."));
    }
}
