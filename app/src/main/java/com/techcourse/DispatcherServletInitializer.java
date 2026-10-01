package com.techcourse;

import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMappingRegistry;
import com.interface21.webmvc.servlet.mvc.tobe.ManualHandlerAdapter;
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
        final var dispatcherServlet = new DispatcherServlet(
                createHandlerMappingRegistry(), createHandlerAdapterRegistry());

        final var registration = servletContext.addServlet(DEFAULT_SERVLET_NAME, dispatcherServlet);
        if (registration == null) {
            throw new IllegalStateException("Failed to register servlet with name '" + DEFAULT_SERVLET_NAME + "'. " +
                    "Check if there is another servlet registered under the same name.");
        }

        registration.setLoadOnStartup(1);
        registration.addMapping("/");

        log.info("Start AppWebApplication Initializer");
    }

    private HandlerMappingRegistry createHandlerMappingRegistry() {
        HandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        HandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("com.techcourse.controller");
        manualHandlerMapping.initialize();
        annotationHandlerMapping.initialize();

        HandlerMappingRegistry registry = new HandlerMappingRegistry();
        registry.addHandlerMapping(annotationHandlerMapping);
        registry.addHandlerMapping(manualHandlerMapping);
        return registry;
    }

    private HandlerAdapterRegistry createHandlerAdapterRegistry() {
        HandlerAdapterRegistry registry = new HandlerAdapterRegistry();
        registry.addHandlerAdapter(new AnnotationHandlerAdapter());
        registry.addHandlerAdapter(new ManualHandlerAdapter());
        return registry;
    }
}
