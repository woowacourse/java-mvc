package com.techcourse;

import java.util.List;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.RequestMappingHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.SimpleControllerHandlerAdapter;

public class HandlerAdapterFactory {

    public static List<HandlerAdapter> getHandlerAdapters() {
        return List.of(
                new SimpleControllerHandlerAdapter(),
                new RequestMappingHandlerAdapter()
        );
    }
}
