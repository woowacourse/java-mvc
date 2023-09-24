package com.techcourse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerAdapter;

public class HandlerAdapters {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapters(List<HandlerAdapter> handlerAdapters) {
        this.handlerAdapters = new ArrayList<>(handlerAdapters);
    }

    public HandlerAdapters() {
        this(Collections.emptyList());
    }

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public ModelAndView handle(
            Object object,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.isSupport(object))
                .map(handlerAdapter -> handlerAdapter.handle(object, request, response))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

}
