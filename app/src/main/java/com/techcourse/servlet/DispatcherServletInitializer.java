package com.techcourse.servlet;

import com.interface21.servlet.DispatcherServlet;
import com.interface21.webmvc.servlet.mvc.tobe.handler.adapter.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.handler.adapter.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.mvc.tobe.handler.mapper.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.handler.mapper.HandlerMappings;
import com.interface21.webmvc.servlet.view.ViewResolver;
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
    public void onStartup(final ServletContext servletContext) {
        final var handlerMappings = new HandlerMappings();
        handlerMappings.addHandlerMapping(new AnnotationHandlerMapping("com"));

        final var handlerAdapterRegistry = new HandlerAdapterRegistry();
        handlerAdapterRegistry.addAdapter(new AnnotationHandlerAdapter());

        final var dispatcherServlet = new DispatcherServlet(handlerMappings, handlerAdapterRegistry, new ViewResolver());

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
