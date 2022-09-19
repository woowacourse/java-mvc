package com.techcourse;

import jakarta.servlet.ServletContext;
import nextstep.mvc.DispatcherServlet;
import nextstep.mvc.handleradaptor.HandlerAdapterRegistry;
import nextstep.mvc.handleradaptor.HandlerExecutionHandlerAdapter;
import nextstep.mvc.handleradaptor.ManualHandlerAdaptor;
import nextstep.mvc.handlermapping.AnnotationHandlerMapping;
import nextstep.mvc.handlermapping.HandlerMappingRegistry;
import nextstep.web.WebApplicationInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppWebApplicationInitializer implements WebApplicationInitializer {

    private static final Logger log = LoggerFactory.getLogger(AppWebApplicationInitializer.class);

    @Override
    public void onStartup(final ServletContext servletContext) {
        final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
        final HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();

        final var dispatcherServlet = new DispatcherServlet(handlerMappingRegistry, handlerAdapterRegistry);
        dispatcherServlet.addHandlerMapping(new ManualHandlerMapping());
        dispatcherServlet.addHandlerMapping(new AnnotationHandlerMapping("com.techcourse.controller"));
        dispatcherServlet.addHandlerAdapter(new HandlerExecutionHandlerAdapter());
        dispatcherServlet.addHandlerAdapter(new ManualHandlerAdaptor());

        final var dispatcher = servletContext.addServlet("dispatcher", dispatcherServlet);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        log.info("Start AppWebApplication Initializer");
    }
}
