package com.techcourse;

import com.interface21.webmvc.servlet.DispatcherServlet;
import com.interface21.webmvc.servlet.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecutionAdapter;
import jakarta.servlet.ServletContext;
import com.interface21.webmvc.servlet.HandlerMappingRegistry;
import jakarta.servlet.ServletException;
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
    public void onStartup(final ServletContext servletContext) throws ServletException {
        final var annotationHandlerMapping =
                new AnnotationHandlerMapping("com.techcourse.controller");

        try {
            annotationHandlerMapping.initialize();
        } catch (ReflectiveOperationException e) {
            throw new ServletException("핸들러 매핑 초기화 실패", e);
        }

        final var handlerMappingRegistry = new HandlerMappingRegistry();
        handlerMappingRegistry.addHandlerMapping(annotationHandlerMapping);

        final var handlerAdapterRegistry = new HandlerAdapterRegistry();
        handlerAdapterRegistry.addHandlerAdapter(
                new HandlerExecutionAdapter());

        final var dispatcherServlet = new DispatcherServlet(
                handlerMappingRegistry, handlerAdapterRegistry);

        final var registration = servletContext.addServlet(
                DEFAULT_SERVLET_NAME, dispatcherServlet);

        if (registration == null) {
            throw new IllegalStateException("Failed to register servlet with name '" + DEFAULT_SERVLET_NAME + "'. " +
                    "Check if there is another servlet registered under the same name.");
        }

        registration.setLoadOnStartup(1);
        registration.addMapping("/");

        log.info("Start AppWebApplication Initializer");
    }
}
