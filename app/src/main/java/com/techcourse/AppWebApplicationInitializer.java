package com.techcourse;

import jakarta.servlet.ServletContext;
import nextstep.mvc.AnnotationModelAndViewHandlerAdapter;
import nextstep.mvc.AnnotationViewNameHandlerAdapter;
import nextstep.mvc.DispatcherServlet;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.web.WebApplicationInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppWebApplicationInitializer implements WebApplicationInitializer {

    private static final Logger log = LoggerFactory.getLogger(AppWebApplicationInitializer.class);

    @Override
    public void onStartup(final ServletContext servletContext) {
        final var dispatcherServlet = new DispatcherServlet();
        initHandlerMappings(dispatcherServlet);
        initHandlerAdapters(dispatcherServlet);

        final var dispatcher = servletContext.addServlet("dispatcher", dispatcherServlet);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        log.info("Start AppWebApplication Initializer");
    }

    private void initHandlerMappings(final DispatcherServlet dispatcherServlet) {
        dispatcherServlet.addHandlerMapping(new AnnotationHandlerMapping(getClass().getPackageName()));
    }

    private void initHandlerAdapters(final DispatcherServlet dispatcherServlet) {
        dispatcherServlet.addHandlerAdapter(new ManualHandlerAdapter());
        dispatcherServlet.addHandlerAdapter(new AnnotationViewNameHandlerAdapter());
        dispatcherServlet.addHandlerAdapter(new AnnotationModelAndViewHandlerAdapter());
    }
}
