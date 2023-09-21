package com.techcourse;

import java.util.List;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.ManualHandlerAdapter;

public class HandlerAdapterFactory {

    public static List<HandlerAdapter> getHandlerAdapters() {
        return List.of(
            new AnnotationHandlerAdapter(),
            new ManualHandlerAdapter()
        );
    }
}
