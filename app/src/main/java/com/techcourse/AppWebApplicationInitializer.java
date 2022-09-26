package com.techcourse;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration.Dynamic;
import nextstep.context.PeanutBox;
import nextstep.mvc.handler.adapter.AnnotationHandlerAdapter;
import nextstep.mvc.servlet.DispatcherServlet;
import nextstep.mvc.handler.adapter.ManualHandlerAdapter;
import nextstep.mvc.handler.mapper.AnnotationHandlerMapper;
import nextstep.web.WebApplicationInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppWebApplicationInitializer implements WebApplicationInitializer {

    private static final Logger log = LoggerFactory.getLogger(AppWebApplicationInitializer.class);

    @Override
    public void onStartup(final ServletContext servletContext) {
        PeanutBox.INSTANCE.init("com.techcourse.controller");

        final DispatcherServlet dispatcherServlet = initDispatcherServlet();
        final Dynamic dispatcher = servletContext.addServlet("dispatcher", dispatcherServlet);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        log.info("Start AppWebApplication Initializer");
    }

    private DispatcherServlet initDispatcherServlet() {
        final var dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.addHandlerMapping(new ManualHandlerMapper());
        dispatcherServlet.addHandlerMapping(new AnnotationHandlerMapper("com.techcourse.controller"));
        dispatcherServlet.addHandlerAdapter(new ManualHandlerAdapter());
        dispatcherServlet.addHandlerAdapter(new AnnotationHandlerAdapter());
        return dispatcherServlet;
    }
}
