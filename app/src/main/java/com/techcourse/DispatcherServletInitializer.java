package com.techcourse;

import jakarta.servlet.ServletContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.WebApplicationInitializer;
import webmvc.org.springframework.web.servlet.mvc.DispatcherServlet;
import webmvc.org.springframework.web.servlet.mvc.tobe.adapter.HandlerAdapters;
import webmvc.org.springframework.web.servlet.mvc.tobe.handler.HandlerMappings;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotaionHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;

/**
 * Base class for {@link WebApplicationInitializer}
 * implementations that register a {@link DispatcherServlet} in the servlet context.
 */
public class DispatcherServletInitializer implements WebApplicationInitializer {

    private static final Logger log = LoggerFactory.getLogger(DispatcherServletInitializer.class);

    private static final String DEFAULT_SERVLET_NAME = "dispatcher";

    @Override
    public void onStartup(final ServletContext servletContext) {

        final HandlerMappings handlerMappings = new HandlerMappings();
        handlerMappings.add(new AnnotationHandlerMapping("com.mvc"));
        final HandlerAdapters handlerAdapters = new HandlerAdapters();
        handlerAdapters.add(new AnnotaionHandlerAdapter());

        final var dispatcherServlet = new DispatcherServlet(handlerMappings, handlerAdapters);

        final var registration = servletContext.addServlet(DEFAULT_SERVLET_NAME, dispatcherServlet);
        if (registration == null) {
            throw new IllegalStateException("Failed to register servlet with name '" + DEFAULT_SERVLET_NAME + "'. " +
                    "Check if there is another servlet registered under the same name.");
        }

        registration.setLoadOnStartup(1);
        registration.addMapping("/");

        log.info("Start AppWebApplication Initializer");
    }
}
