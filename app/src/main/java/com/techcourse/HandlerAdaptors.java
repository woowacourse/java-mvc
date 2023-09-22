package com.techcourse;

import com.techcourse.exception.HandlerFieldException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerKey;

public class HandlerAdaptors {

    private static HandlerAdaptors instance;

    private static List<HandlerAdaptor<?>> handlerAdaptors;

    private HandlerAdaptors() {
        this.handlerAdaptors = new ArrayList<>();
        try {
            final AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping();
            annotationHandlerMapping.initialize();
            final ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();
            manualHandlerMapping.initialize();
            handlerAdaptors.add(HandlerAdaptor.of(annotationHandlerMapping));
            handlerAdaptors.add(HandlerAdaptor.of(manualHandlerMapping));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new HandlerFieldException();
        }
    }

    public static synchronized HandlerAdaptors getInstance() {
        if (instance == null) {
            instance = new HandlerAdaptors();
        }
        return instance;
    }

    public HandlerAdaptor getHandler(HttpServletRequest httpServletRequest) {
        final HandlerKey handlerKey = new HandlerKey(
                httpServletRequest.getRequestURI(),
                Enum.valueOf(RequestMethod.class, httpServletRequest.getMethod())
        );
        return handlerAdaptors.stream()
                .filter(handlerAdaptor -> handlerAdaptor.isHandle(handlerKey))
                .findFirst()
                .orElseThrow();
    }
}
