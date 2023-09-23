package com.techcourse;

import jakarta.servlet.ServletContext;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.WebApplicationInitializer;
import webmvc.org.springframework.web.servlet.mvc.DispatcherServlet;
import webmvc.org.springframework.web.servlet.mvc.HandlerAdapters;
import webmvc.org.springframework.web.servlet.mvc.HandlerMappings;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;

/**
 * Base class for {@link WebApplicationInitializer} implementations that register a {@link DispatcherServlet} in the
 * servlet context.
 */
public class DispatcherServletInitializer implements WebApplicationInitializer {

    private static final Logger log = LoggerFactory.getLogger(DispatcherServletInitializer.class);

    private static final String DEFAULT_SERVLET_NAME = "dispatcher";

    @Override
    public void onStartup(final ServletContext servletContext) {
        final HandlerMappings handlerMappings = new HandlerMappings(List.of(
                new ManualHandlerMapping(),
                new AnnotationHandlerMapping("com.techcourse.controller")
        ));
        final HandlerAdapters handlerAdapters = new HandlerAdapters(List.of(
                new ManualHandlerAdapter(),
                new AnnotationHandlerAdapter()
        ));
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
