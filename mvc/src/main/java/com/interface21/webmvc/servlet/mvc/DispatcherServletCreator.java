package com.interface21.webmvc.servlet.mvc;

import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;

public class DispatcherServletCreator {

    public static DispatcherServlet create() {
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.addHandlerMapping(0, new AnnotationHandlerMapping("com"));
        dispatcherServlet.addHandlerAdapter(0, new AnnotationHandlerAdapter());
        return dispatcherServlet;
    }
}
