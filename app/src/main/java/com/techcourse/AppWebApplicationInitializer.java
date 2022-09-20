package com.techcourse;

import jakarta.servlet.ServletContext;
import nextstep.mvc.ControllerMappingHandlerAdapter;
import nextstep.mvc.DispatcherServlet;
import nextstep.mvc.RequestMappingHandlerAdapter;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.web.WebApplicationInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppWebApplicationInitializer implements WebApplicationInitializer {

    private static final Logger log = LoggerFactory.getLogger(AppWebApplicationInitializer.class);

    @Override
    public void onStartup(final ServletContext servletContext) {
        final var dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.addHandlerMapping(new ManualHandlerMapping());
        dispatcherServlet.addHandlerMapping(new AnnotationHandlerMapping(Application.class.getPackageName()));

        dispatcherServlet.addHandlerAdapter(new ControllerMappingHandlerAdapter());
        dispatcherServlet.addHandlerAdapter(new RequestMappingHandlerAdapter());

        final var dispatcher = servletContext.addServlet("dispatcher", dispatcherServlet);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        log.info("Start AppWebApplication Initializer");
    }
}
