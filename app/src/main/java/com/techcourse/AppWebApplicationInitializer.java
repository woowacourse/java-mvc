package com.techcourse;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration;
import nextstep.mvc.AnnotationHandlerAdapter;
import nextstep.mvc.ManualHandlerAdapter;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.mvc.servlet.DispatcherServlet;
import nextstep.mvc.servlet.HandlerAdapterRegistry;
import nextstep.mvc.servlet.HandlerMappingRegistry;
import nextstep.web.WebApplicationInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppWebApplicationInitializer implements WebApplicationInitializer {

    private static final Logger log = LoggerFactory.getLogger(AppWebApplicationInitializer.class);

    @Override
    public void onStartup(ServletContext servletContext) {
        final DispatcherServlet dispatcherServlet = new DispatcherServlet(
            new HandlerMappingRegistry(), new HandlerAdapterRegistry());
        dispatcherServlet.addHandlerMapping(new ManualHandlerMapping());
        dispatcherServlet.addHandlerMapping(new AnnotationHandlerMapping("com.techcourse"));
        dispatcherServlet.addHandlerAdapter(new AnnotationHandlerAdapter());
        dispatcherServlet.addHandlerAdapter(new ManualHandlerAdapter());

        final ServletRegistration.Dynamic dispatcher = servletContext
            .addServlet("dispatcher", dispatcherServlet);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        log.info("Start AppWebApplication Initializer");
    }
}
