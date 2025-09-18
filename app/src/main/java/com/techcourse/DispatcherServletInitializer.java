package com.techcourse;

import com.interface21.web.WebApplicationInitializer;
import com.interface21.webmvc.servlet.mvc.DispatcherServlet;
import jakarta.servlet.ServletContext;
import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for {@link WebApplicationInitializer} implementations that register a {@link DispatcherServlet} in the
 * servlet context.
 */
public class DispatcherServletInitializer implements WebApplicationInitializer {

    public static final String DEFAULT_PROPERTIES_NAME = "application.properties";
    public static final String CONTROLLER_BASE_PATH = "com.techcourse.controller";
    private static final Logger log = LoggerFactory.getLogger(DispatcherServletInitializer.class);
    private static final String DEFAULT_SERVLET_NAME = "dispatcher";

    @Override
    public void onStartup(final ServletContext servletContext) {
        Properties properties = loadProperties(DEFAULT_PROPERTIES_NAME);
        final var dispatcherServlet = new DispatcherServlet(properties, CONTROLLER_BASE_PATH);

        final var registration = servletContext.addServlet(DEFAULT_SERVLET_NAME, dispatcherServlet);
        if (registration == null) {
            throw new IllegalStateException("Failed to register servlet with name '" + DEFAULT_SERVLET_NAME + "'. " +
                    "Check if there is another servlet registered under the same name.");
        }

        registration.setLoadOnStartup(1);
        registration.addMapping("/");

        log.info("Start AppWebApplication Initializer");
    }

    private Properties loadProperties(String fileName) {
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                throw new IllegalStateException("Could not find " + fileName + " on classpath");
            }
            properties.load(input);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load properties from " + fileName, e);
        }
        return properties;
    }
}
