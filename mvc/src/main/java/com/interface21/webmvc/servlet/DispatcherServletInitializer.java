package com.interface21.webmvc.servlet;

import com.interface21.web.WebApplicationInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration.Dynamic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for {@link WebApplicationInitializer}
 * implementations that register a {@link DispatcherServlet} in the servlet context.
 */
public class DispatcherServletInitializer implements WebApplicationInitializer {

    private static final Logger log = LoggerFactory.getLogger(DispatcherServletInitializer.class);
    private static final String DEFAULT_SERVLET_NAME = "dispatcher";
    private static final HandlerMappingsInitializer handlerMappingsInitializer = new HandlerMappingsInitializer();
    private static final HandlerAdaptersInitializer handlerAdaptersInitializer = new HandlerAdaptersInitializer();

    @Override
    public void onStartup(ServletContext servletContext) {
        DispatcherServlet dispatcherServlet = new DispatcherServlet(
                handlerMappingsInitializer.initialize(),
                handlerAdaptersInitializer.initialize()
        );

        Dynamic registration = servletContext.addServlet(DEFAULT_SERVLET_NAME, dispatcherServlet);
        if (registration == null) {
            throw new IllegalStateException("Failed to register servlet with name '" + DEFAULT_SERVLET_NAME + "'. " +
                    "Check if there is another servlet registered under the same name.");
        }

        registration.setLoadOnStartup(1);
        registration.addMapping("/");

        log.info("Start AppWebApplication Initializer");
    }
}
