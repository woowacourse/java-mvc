package com.interface21.web;

import com.interface21.webmvc.servlet.mvc.DispatcherServlet;
import jakarta.servlet.ServletContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractDispatcherServletInitializer implements WebApplicationInitializer {

    private static final Logger log = LoggerFactory.getLogger(AbstractDispatcherServletInitializer.class);
    private static final String DEFAULT_SERVLET_NAME = "dispatcher";


    @Override
    public void onStartup(final ServletContext servletContext) {

        final var dispatcherServlet = new DispatcherServlet("com.techcourse");

        dispatcherServlet.init();

        final var registration = servletContext.addServlet(DEFAULT_SERVLET_NAME, dispatcherServlet);
        if (registration == null) {
            throw new IllegalStateException("Failed to register servlet with name '" + DEFAULT_SERVLET_NAME + "'. " +
                    "Check if there is another servlet registered under the same name.");
        }

        registration.setLoadOnStartup(1);
        registration.addMapping("/");

        log.info("Start AppWebApplication Initializer");
    }

    protected abstract String getBasePackage();
}
