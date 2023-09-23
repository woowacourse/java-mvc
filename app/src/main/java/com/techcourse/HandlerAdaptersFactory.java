package com.techcourse;

import java.util.List;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerAdaptor;

public class HandlerAdaptersFactory {
    private HandlerAdaptersFactory() {
    }

    public static List<HandlerAdaptor> createHandlerAdaptors() {
        return List.of(new AnnotationHandlerAdapter(), new ManualHandlerAdapter());
    }
}
