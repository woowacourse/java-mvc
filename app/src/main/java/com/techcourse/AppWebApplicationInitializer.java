package com.techcourse;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration;
import nextstep.mvc.DispatcherServlet;
import nextstep.mvc.HandlerAdapters;
import nextstep.mvc.HandlerMappings;
import nextstep.mvc.RequestMappingHandlerAdapter;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.web.WebApplicationInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppWebApplicationInitializer implements WebApplicationInitializer {

    private static final Logger log = LoggerFactory.getLogger(AppWebApplicationInitializer.class);

    @Override
    public void onStartup(ServletContext servletContext) {
        final HandlerMappings handlerMappings = new HandlerMappings(new AnnotationHandlerMapping("com.techcourse.controller"));
        final HandlerAdapters handlerAdapters = new HandlerAdapters(new RequestMappingHandlerAdapter());
        final DispatcherServlet dispatcherServlet = new DispatcherServlet(handlerMappings, handlerAdapters);

        final ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", dispatcherServlet);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        log.info("Start AppWebApplication Initializer");
    }
}
