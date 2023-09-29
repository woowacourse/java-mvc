package com.techcourse;

import context.org.springframework.context.ApplicationContext;
import jakarta.servlet.ServletContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.WebApplicationInitializer;
import webmvc.org.springframework.web.servlet.DispatcherServlet;

/**
 * Base class for {@link WebApplicationInitializer} implementations that register a {@link DispatcherServlet} in the
 * servlet context.
 */
public class DispatcherServletInitializer implements WebApplicationInitializer {

    private static final Logger log = LoggerFactory.getLogger(DispatcherServletInitializer.class);
    private static final String APPLICATION_BASE_PACKAGE = "com.techcourse";
    private static final String INTERNAL_BASE_PACKAGE = "webmvc.org.springframework.web.servlet";
    private static final String DEFAULT_SERVLET_NAME = "dispatcher";

    @Override
    public void onStartup(final ServletContext servletContext) {
        final ApplicationContext applicationContext = new ApplicationContext(APPLICATION_BASE_PACKAGE,
                                                                             INTERNAL_BASE_PACKAGE);
        final var dispatcherServlet = new DispatcherServlet(applicationContext);

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
