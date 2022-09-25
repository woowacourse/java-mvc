package com.techcourse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.ServletContext;
import nextstep.mvc.DispatcherServlet;
import nextstep.mvc.handlerAdapter.HandlerExecutionHandlerAdapter;
import nextstep.mvc.handlerMapping.AnnotationHandlerMapping;
import nextstep.web.WebApplicationInitializer;

public class AppWebApplicationInitializer implements WebApplicationInitializer {

    private static final Logger log = LoggerFactory.getLogger(AppWebApplicationInitializer.class);

    @Override
    public void onStartup(final ServletContext servletContext) {
        final var dispatcherServlet = new DispatcherServlet();

        dispatcherServlet.addHandlerMapping(new AnnotationHandlerMapping(getClass().getPackageName()));

        dispatcherServlet.addHandlerAdapter(new HandlerExecutionHandlerAdapter());

        final var dispatcher = servletContext.addServlet("dispatcher", dispatcherServlet);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        log.info("Start AppWebApplication Initializer");
    }
}
