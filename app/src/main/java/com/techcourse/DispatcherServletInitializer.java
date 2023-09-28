package com.techcourse;

import com.techcourse.exception.NotFoundExceptionHandler;
import jakarta.servlet.ServletContext;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.WebApplicationInitializer;
import webmvc.org.springframework.web.servlet.DispatcherServlet;
import webmvc.org.springframework.web.servlet.mvc.tobe.adapter.AnnotationHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.adapter.HandlerAdapterFinder;
import webmvc.org.springframework.web.servlet.mvc.tobe.exception.ExceptionResolver;
import webmvc.org.springframework.web.servlet.mvc.tobe.handler.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.handler.HandlerMapping;

/**
 * Base class for {@link WebApplicationInitializer}
 * implementations that register a {@link DispatcherServlet} in the servlet context.
 */
public class DispatcherServletInitializer implements WebApplicationInitializer {

    private static final Logger log = LoggerFactory.getLogger(DispatcherServletInitializer.class);

    private static final String DEFAULT_SERVLET_NAME = "dispatcher";

    @Override
    public void onStartup(final ServletContext servletContext) {
        final var dispatcherServlet = initDispatcherServlet();

        final var registration = servletContext.addServlet(DEFAULT_SERVLET_NAME, dispatcherServlet);
        if (registration == null) {
            throw new IllegalStateException("Failed to register servlet with name '" + DEFAULT_SERVLET_NAME + "'. " +
                    "Check if there is another servlet registered under the same name.");
        }

        registration.setLoadOnStartup(1);
        registration.addMapping("/");

        log.info("Start AppWebApplication Initializer");
    }

    public DispatcherServlet initDispatcherServlet() {
        HandlerMapping handlerMapping = new AnnotationHandlerMapping();
        handlerMapping.initialize();
        HandlerAdapterFinder handlerAdapterFinder = new HandlerAdapterFinder(List.of(new AnnotationHandlerAdapter()));
        ExceptionResolver exceptionResolver = new ExceptionResolver(List.of(new NotFoundExceptionHandler()));
        return new DispatcherServlet(handlerMapping, handlerAdapterFinder, exceptionResolver);
    }
}
