package com.techcourse;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.WebApplicationInitializer;
import webmvc.org.springframework.web.servlet.mvc.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.DispatcherServlet;

import static webmvc.org.springframework.web.servlet.mvc.DispatcherServlet.HANDLER_MAPPINGS;

/**
 * Base class for {@link WebApplicationInitializer} implementations that register a {@link DispatcherServlet} in the
 * servlet context.
 */
public class AppInitializer implements WebApplicationInitializer {

    private static final Logger log = LoggerFactory.getLogger(AppInitializer.class);
    private static final String DEFAULT_SERVLET_NAME = "dispatcher";

    @Override
    public void onStartup(final ServletContext servletContext) throws ServletException {
        final var handlerMapping = new AnnotationHandlerMapping(this.getClass().getPackage().getName());
        handlerMapping.initialize();
        servletContext.setAttribute(HANDLER_MAPPINGS, List.of(handlerMapping));

        final var dispatcherServlet = new DispatcherServlet();
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
