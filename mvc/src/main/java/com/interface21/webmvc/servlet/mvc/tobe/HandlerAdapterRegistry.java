package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapterRegistry() {
        this.handlerAdapters = new ArrayList<>();
    }

    public void register(final HandlerAdapter handlerAdapter) {
        this.handlerAdapters.add(handlerAdapter);
    }

    public ModelAndView handle(final Object handler, final HttpServletRequest request,
                               final HttpServletResponse response) {
        try {
            // TODO : 스트림으로 바꾸기
            for (HandlerAdapter handlerAdapter : handlerAdapters) {
                if (handlerAdapter.isAdaptable(handler)) {
                    return handlerAdapter.handle(handler, request, response);
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException("핸들러를 실행하는데 실패했습니다.");
        }
        throw new IllegalStateException("핸들러를 실행하는데 실패했습니다.");
    }
}
