package com.techcourse;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration;
import nextstep.mvc.*;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.web.WebApplicationInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppWebApplicationInitializer implements WebApplicationInitializer {

    private static final Logger log = LoggerFactory.getLogger(AppWebApplicationInitializer.class);

    @Override
    public void onStartup(ServletContext servletContext) {
        HandlerMappings handlerMappings = new HandlerMappings(new ManualHandlerMapping(), new AnnotationHandlerMapping("controller"));
        HandlerAdapters handlerAdapters = new HandlerAdapters(new ControllerHandlerAdapter(), new RequestMappingHandlerAdapter());
        final DispatcherServlet dispatcherServlet = new DispatcherServlet(handlerMappings, handlerAdapters);
        dispatcherServlet.init();

        final ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", dispatcherServlet);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        log.info("Start AppWebApplication Initializer");
    }
}
