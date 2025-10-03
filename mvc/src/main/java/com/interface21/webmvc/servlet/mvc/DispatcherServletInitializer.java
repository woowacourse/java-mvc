package com.interface21.webmvc.servlet.mvc;

import java.util.Set;

import javax.swing.*;

import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.web.SpringServletContainerInitializer;
import com.interface21.web.WebApplicationInitializer;

/**
 * Base class for {@link WebApplicationInitializer}
 * implementations that register a {@link DispatcherServlet} in the servlet context.
 */
public class DispatcherServletInitializer implements ServletContainerInitializer {

    private static final Logger log = LoggerFactory.getLogger(DispatcherServletInitializer.class);

    private static final String DEFAULT_SERVLET_NAME = "dispatcher";

    @Override
    public void onStartup(Set<Class<?>> c, ServletContext servletContext) {
        final var dispatcherServlet = new DispatcherServlet();

        final var registration = servletContext.addServlet(DEFAULT_SERVLET_NAME, dispatcherServlet);
        if (registration == null) {
            throw new IllegalStateException("Failed to register servlet with name '" + DEFAULT_SERVLET_NAME + "'. " +
                "Check if there is another servlet registered under the same name.");
        }

        registration.setLoadOnStartup(1);
        registration.addMapping("/");

    }
}
