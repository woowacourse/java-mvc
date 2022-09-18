package com.techcourse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.ServletContext;
import nextstep.mvc.DispatcherServlet;
import nextstep.mvc.controller.tobe.AnnotationHandlerAdapter;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.web.WebApplicationInitializer;

public class AppWebApplicationInitializer implements WebApplicationInitializer {

    private static final Logger log = LoggerFactory.getLogger(AppWebApplicationInitializer.class);

    @Override
    public void onStartup(final ServletContext servletContext) {
        final var dispatcherServlet = initDispatcherServlet();

        final var dispatcher = servletContext.addServlet("dispatcher", dispatcherServlet);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        log.info("Start AppWebApplication Initializer");
    }

    private DispatcherServlet initDispatcherServlet() {
        final var manualHandlerMapping = new ManualHandlerMapping();
        final var annotationHandlerMapping = new AnnotationHandlerMapping();
        final var manualHandlerAdapter = new ManualHandlerAdapter();
        final var annotationHandlerAdapter = new AnnotationHandlerAdapter();

        final var dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.addHandlerMapping(manualHandlerMapping, annotationHandlerMapping);
        dispatcherServlet.addHandlerAdapter(manualHandlerAdapter, annotationHandlerAdapter);
        return dispatcherServlet;
    }
}
