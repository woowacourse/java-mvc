package com.techcourse;

import com.interface21.webmvc.servlet.mvc.tobe.DispatcherServlet;
import com.interface21.webmvc.servlet.mvc.tobe.handler.adapter.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.handler.adapter.HandlerAdapters;
import com.interface21.webmvc.servlet.mvc.tobe.handler.mapping.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.handler.mapping.HandlerMappings;
import jakarta.servlet.ServletContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.interface21.web.WebApplicationInitializer;

/**
 * Base class for {@link WebApplicationInitializer}
 * implementations that register a {@link DispatcherServlet} in the servlet context.
 */
public class DispatcherServletInitializer implements WebApplicationInitializer {

    private static final Logger log = LoggerFactory.getLogger(DispatcherServletInitializer.class);

    private static final String DEFAULT_SERVLET_NAME = "dispatcher";

    @Override
    public void onStartup(ServletContext servletContext) {
        DispatcherServlet dispatcherServlet = createDispatcherServlet();

        final var registration = servletContext.addServlet(DEFAULT_SERVLET_NAME, dispatcherServlet);
        if (registration == null) {
            throw new IllegalStateException("Failed to register servlet with name '" + DEFAULT_SERVLET_NAME + "'. " +
                    "Check if there is another servlet registered under the same name.");
        }

        registration.setLoadOnStartup(1);
        registration.addMapping("/");

        log.info("Start AppWebApplication Initializer");
    }

    private DispatcherServlet createDispatcherServlet() {
        HandlerMappings handlerMappings = new HandlerMappings(
                new AnnotationHandlerMapping(getClass().getPackageName())
        );
        HandlerAdapters handlerAdapters = new HandlerAdapters(
                new AnnotationHandlerAdapter()
        );

        return new DispatcherServlet(
                handlerMappings,
                handlerAdapters
        );
    }
}
