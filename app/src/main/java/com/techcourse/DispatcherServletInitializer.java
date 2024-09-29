package com.techcourse;

import com.interface21.web.WebApplicationInitializer;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.DispatcherServlet;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMappingRegistry;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerRegistry;
import com.techcourse.handleradapter.HandlerExecutionHandlerAdapter;
import com.techcourse.viewresolver.SimpleViewResolver;
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

    private static final String CONTROLLER_BASE_PACKAGE = "com.techcourse.controller";
    private static final String DEFAULT_SERVLET_NAME = "dispatcher";

    @Override
    public void onStartup(final ServletContext servletContext) {
        final var dispatcherServlet = getDispatcherServlet();

        final var registration = servletContext.addServlet(DEFAULT_SERVLET_NAME, dispatcherServlet);
        if (registration == null) {
            throw new IllegalStateException("Failed to register servlet with name '" + DEFAULT_SERVLET_NAME + "'. " +
                    "Check if there is another servlet registered under the same name.");
        }

        registration.setLoadOnStartup(1);
        registration.addMapping("/");

        log.info("Start AppWebApplication Initializer");
    }

    private DispatcherServlet getDispatcherServlet() {
        HandlerRegistry handlerRegistry = getHandlerRegistry();
        return new DispatcherServlet(handlerRegistry, new SimpleViewResolver());
    }

    private HandlerRegistry getHandlerRegistry() {
        HandlerMappingRegistry handlerMappingRegistry = getHandlerMappingRegistry();
        HandlerAdapterRegistry handlerAdapterRegistry = getHandlerAdapterRegistry();
        return new HandlerRegistry(handlerMappingRegistry, handlerAdapterRegistry);
    }

    private HandlerMappingRegistry getHandlerMappingRegistry() {
        return new HandlerMappingRegistry(
                List.of(new AnnotationHandlerMapping(CONTROLLER_BASE_PACKAGE))
        );
    }

    private HandlerAdapterRegistry getHandlerAdapterRegistry() {
        return new HandlerAdapterRegistry(
                List.of(new HandlerExecutionHandlerAdapter())
        );
    }
}
