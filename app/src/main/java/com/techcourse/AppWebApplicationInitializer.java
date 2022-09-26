package com.techcourse;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration.Dynamic;
import nextstep.mvc.DispatcherServlet;
import nextstep.mvc.controller.tobe.handleradapter.AnnotationHandlerAdapter;
import nextstep.mvc.controller.tobe.handlermapping.AnnotationHandlerMapping;
import nextstep.mvc.controller.tobe.handleradapter.ControllerHandlerAdapter;
import nextstep.web.WebApplicationInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppWebApplicationInitializer implements WebApplicationInitializer {

    private static final Logger log = LoggerFactory.getLogger(AppWebApplicationInitializer.class);

    @Override
    public void onStartup(final ServletContext servletContext) {
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.addHandlerMapping(new ManualHandlerMapping());
        dispatcherServlet.addHandlerMapping(new AnnotationHandlerMapping("com.techcourse"));
        dispatcherServlet.addHandlerAdapter(new ControllerHandlerAdapter());
        dispatcherServlet.addHandlerAdapter(new AnnotationHandlerAdapter());

        Dynamic dispatcher = servletContext.addServlet("dispatcher", dispatcherServlet);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        log.info("Start AppWebApplication Initializer");
    }
}
