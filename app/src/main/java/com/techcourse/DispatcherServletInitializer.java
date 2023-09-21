package com.techcourse;

import com.techcourse.support.web.mapping.ManualHandlerMappingWrapper;
import jakarta.servlet.ServletContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.WebApplicationInitializer;
import webmvc.org.springframework.web.servlet.mvc.adapter.AnnotationHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.adapter.ManualHandlerMappingAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.view.resolver.ViewResolvers;

/**
 * Base class for {@link WebApplicationInitializer} implementations that register a {@link DispatcherServlet} in the
 * servlet context.
 */
public class DispatcherServletInitializer implements WebApplicationInitializer {

    private static final Logger log = LoggerFactory.getLogger(DispatcherServletInitializer.class);

    private static final String DEFAULT_SERVLET_NAME = "dispatcher";

    @Override
    public void onStartup(final ServletContext servletContext) {
        final ViewResolvers resolvers = new ViewResolvers();

        resolvers.initialize();

        final var dispatcherServlet = new DispatcherServlet();

        dispatcherServlet.addHandlerMapping(new ManualHandlerMappingWrapper())
                         .addHandlerMapping(new AnnotationHandlerMapping())
                         .addHandlerAdapter(new ManualHandlerMappingAdapter(resolvers))
                         .addHandlerAdapter(new AnnotationHandlerAdapter());

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
