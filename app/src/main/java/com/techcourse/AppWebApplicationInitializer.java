package com.techcourse;

import jakarta.servlet.ServletContext;
import java.util.ArrayList;
import java.util.List;
import nextstep.mvc.DispatcherServlet;
import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.HandlerAdapterRegistry;
import nextstep.mvc.HandlerMapping;
import nextstep.mvc.HandlerMappingRegistry;
import nextstep.mvc.controller.asis.ControllerHandlerAdapter;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.mvc.controller.tobe.HandlerExecutionHandlerAdapter;
import nextstep.web.WebApplicationInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppWebApplicationInitializer implements WebApplicationInitializer {

    private static final Logger log = LoggerFactory.getLogger(AppWebApplicationInitializer.class);

    @Override
    public void onStartup(final ServletContext servletContext) {
        final DispatcherServlet dispatcherServlet = initDispatcherServlet();
        final var dispatcher = servletContext.addServlet("dispatcher", dispatcherServlet);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
        log.info("Start AppWebApplication Initializer");
    }

    private DispatcherServlet initDispatcherServlet() {
        List<HandlerMapping> handlerMappings = new ArrayList<>();
        handlerMappings.add(new ManualHandlerMapping());
        handlerMappings.add(new AnnotationHandlerMapping());
        HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry(handlerMappings);
        List<HandlerAdapter> handlerAdapters = new ArrayList<>();
        handlerAdapters.add(new ControllerHandlerAdapter());
        handlerAdapters.add(new HandlerExecutionHandlerAdapter());

        final HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry(handlerAdapters);
        final var dispatcherServlet = new DispatcherServlet(handlerMappingRegistry, handlerAdapterRegistry);
        return dispatcherServlet;
    }
}
