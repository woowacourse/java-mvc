package com.techcourse;

import jakarta.servlet.ServletContext;
import nextstep.mvc.DispatcherServlet;
import nextstep.mvc.controller.asis.ControllerHandlerAdapter;
import nextstep.mvc.controller.registry.HandlerAdapterRegistry;
import nextstep.mvc.controller.registry.HandlerMappingRegistry;
import nextstep.mvc.controller.tobe.AnnotationHandlerAdapter;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.web.WebApplicationInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppWebApplicationInitializer implements WebApplicationInitializer {

    private static final Logger log = LoggerFactory.getLogger(AppWebApplicationInitializer.class);

    @Override
    public void onStartup(final ServletContext servletContext) {
        HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
        HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();

        var dispatcherServlet = new DispatcherServlet(handlerMappingRegistry, handlerAdapterRegistry);
        dispatcherServlet.addHandlerMapping(new ManualHandlerMapping());
        dispatcherServlet.addHandlerMapping(new AnnotationHandlerMapping("com.techcourse"));
        dispatcherServlet.addAdapterMapping(new ControllerHandlerAdapter());
        dispatcherServlet.addAdapterMapping(new AnnotationHandlerAdapter());

        var dispatcher = servletContext.addServlet("dispatcher", dispatcherServlet);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        log.info("Start AppWebApplication Initializer");
    }
}
