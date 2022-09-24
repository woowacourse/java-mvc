package com.techcourse;

import jakarta.servlet.ServletContext;
import nextstep.mvc.DispatcherServlet;
import nextstep.mvc.HandlerAdapterRegistry;
import nextstep.mvc.HandlerMappingRegistry;
import nextstep.mvc.handler.adapter.AnnotationHandlerAdapter;
import nextstep.mvc.handler.mapping.AnnotationHandlerMapping;
import nextstep.web.WebApplicationInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.techcourse.support.web.handler.ManualHandlerAdapter;
import com.techcourse.support.web.handler.ManualHandlerMapping;

public class AppWebApplicationInitializer implements WebApplicationInitializer {

    private static final Logger log = LoggerFactory.getLogger(AppWebApplicationInitializer.class);

    @Override
    public void onStartup(final ServletContext servletContext) {
        final var handlerMappingRegistry = HandlerMappingRegistry.builder()
            .add(new ManualHandlerMapping())
            .add(new AnnotationHandlerMapping("com.techcourse"))
            .build();

        final var handlerAdapterRegistry = HandlerAdapterRegistry.builder()
            .add(new ManualHandlerAdapter())
            .add(new AnnotationHandlerAdapter())
            .build();

        final var dispatcherServlet = new DispatcherServlet(handlerMappingRegistry, handlerAdapterRegistry);

        final var dispatcher = servletContext.addServlet("dispatcher", dispatcherServlet);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        log.info("Start AppWebApplication Initializer");
    }
}
