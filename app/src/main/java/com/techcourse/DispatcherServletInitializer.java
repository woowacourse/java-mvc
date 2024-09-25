package com.techcourse;

import com.interface21.web.WebApplicationInitializer;
import jakarta.servlet.ServletContext;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for {@link WebApplicationInitializer}
 * implementations that register a {@link DispatcherServlet} in the servlet context.
 */
public class DispatcherServletInitializer implements WebApplicationInitializer {

    private static final Logger log = LoggerFactory.getLogger(DispatcherServletInitializer.class);

    private static final String DEFAULT_SERVLET_NAME = "dispatcher";
    private static final List<HandlerMapping> handlerMappings = List.of(
            new ManualHandlerMapping(),
            new AnnotationHandlerMapping()
    );

    @Override
    public void onStartup(final ServletContext servletContext) {
        final var dispatcherServlet = new DispatcherServlet(handlerMappings);

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
