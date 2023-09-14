package com.techcourse.support;

import java.util.HashMap;
import java.util.Map;
import webmvc.org.springframework.web.servlet.mvc.HandlerExceptionResolver;
import webmvc.org.springframework.web.servlet.mvc.exception.HandlerExceptionResolverNotFoundException;

public class HandlerExceptionResolvers {

    private final Map<Class<? extends Exception>, HandlerExceptionResolver> handlerExceptionResolverMap = new HashMap<>();


    public void addHandlerExceptionResolver(HandlerExceptionResolver handlerExceptionResolver) {
        handlerExceptionResolverMap.put(handlerExceptionResolver.supportException(), handlerExceptionResolver);
    }

    public HandlerExceptionResolver getExceptionResolver(Exception ex) {
        HandlerExceptionResolver handlerExceptionResolver = handlerExceptionResolverMap.get(ex.getClass());
        if (handlerExceptionResolver == null) {
            throw new HandlerExceptionResolverNotFoundException(ex.getClass() + " is not support!", ex);
        }
        return handlerExceptionResolver;
    }
}
