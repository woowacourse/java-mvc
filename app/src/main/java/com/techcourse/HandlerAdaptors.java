package com.techcourse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import webmvc.org.springframework.web.servlet.ModelAndView;

public class HandlerAdaptors {

    private final List<HandlerAdaptor> handlerAdaptors;

    public HandlerAdaptors() {
        this.handlerAdaptors = List.of(new LegacyHandlerAdaptor(), new ExecutionHandlerAdaptor());
    }

    public ModelAndView execute(final Object handler, final HttpServletRequest request,
                                final HttpServletResponse response)
            throws Exception {
        final HandlerAdaptor findHandlerAdaptor = handlerAdaptors.stream()
                .filter(adaptor -> adaptor.isSame(handler))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);

        return findHandlerAdaptor.execute(handler, request, response);
    }
}
