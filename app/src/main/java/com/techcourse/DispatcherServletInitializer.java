package com.techcourse;

import com.interface21.web.WebApplicationInitializer;
import com.interface21.webmvc.servlet.mvc.handler.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.handler.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.handler.adapter.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.handler.adapter.HandlerAdapters;
import com.interface21.webmvc.servlet.mvc.handler.mapping.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.handler.mapping.HandlerMappings;
import jakarta.servlet.ServletContext;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for {@link WebApplicationInitializer} implementations that register a {@link DispatcherServlet} in the
 * servlet context.
 */
public class DispatcherServletInitializer implements WebApplicationInitializer {

    private static final Logger log = LoggerFactory.getLogger(DispatcherServletInitializer.class);

    private static final String DEFAULT_SERVLET_NAME = "dispatcher";
    private static final List<HandlerMapping> DEFAULT_MAPPINGS = List.of(
            new AnnotationHandlerMapping("com.techcourse")
    );
    private static final List<HandlerAdapter> DEFAULT_ADAPTERS = List.of(
            new AnnotationHandlerAdapter()
    );

    @Override
    public void onStartup(final ServletContext servletContext) {
        HandlerMappings handlerMappings = new HandlerMappings(DEFAULT_MAPPINGS);
        HandlerAdapters handlerAdapters = new HandlerAdapters(DEFAULT_ADAPTERS);

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
