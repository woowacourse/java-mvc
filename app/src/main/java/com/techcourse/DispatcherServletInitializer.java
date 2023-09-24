package com.techcourse;

import jakarta.servlet.ServletContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.WebApplicationInitializer;
import webmvc.org.springframework.web.servlet.mvc.asis.AnnotationControllerAdapter;
import webmvc.org.springframework.web.servlet.mvc.asis.HandlerAdapterRegistry;
import webmvc.org.springframework.web.servlet.mvc.asis.InterfaceControllerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMappingRegistry;

/**
 * Base class for {@link WebApplicationInitializer} implementations that register a {@link DispatcherServlet} in the servlet
 * context.
 */
public class DispatcherServletInitializer implements WebApplicationInitializer {

    private static final Logger log = LoggerFactory.getLogger(DispatcherServletInitializer.class);

    private static final String DEFAULT_SERVLET_NAME = "dispatcher";

    @Override
    public void onStartup(final ServletContext servletContext) {
        final var dispatcherServlet = new DispatcherServlet(getHandlerMappingRegistry(), getHandlerAdapterRegistry());

        final var registration = servletContext.addServlet(DEFAULT_SERVLET_NAME, dispatcherServlet);
        if (registration == null) {
            throw new IllegalStateException("Failed to register servlet with name '" + DEFAULT_SERVLET_NAME + "'. " +
                "Check if there is another servlet registered under the same name.");
        }

        registration.setLoadOnStartup(1);
        registration.addMapping("/");

        log.info("Start AppWebApplication Initializer");
    }

    private HandlerMappingRegistry getHandlerMappingRegistry() {
        HandlerMappingRegistry registry = new HandlerMappingRegistry();
        registry.addHandlerMapping(
            getInitializedHandlerMapping(new AnnotationHandlerMapping("com")));
        return registry;
    }

    private HandlerMapping getInitializedHandlerMapping(HandlerMapping handlerMapping) {
        handlerMapping.initialize();
        return handlerMapping;
    }

    private HandlerAdapterRegistry getHandlerAdapterRegistry() {
        HandlerAdapterRegistry registry = new HandlerAdapterRegistry();
        registry.addHandlerAdapter(new AnnotationControllerAdapter());
        registry.addHandlerAdapter(new InterfaceControllerAdapter());
        return registry;
    }
}
