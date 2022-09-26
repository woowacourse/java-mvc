package com.techcourse;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration.Dynamic;
import nextstep.mvc.DispatcherServlet;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.mvc.controller.tobe.HandlerExecutionHandlerAdapter;
import nextstep.web.WebApplicationInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppWebApplicationInitializer implements WebApplicationInitializer {

    private static final Logger log = LoggerFactory.getLogger(AppWebApplicationInitializer.class);

    @Override
    public void onStartup(final ServletContext servletContext) {
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        addHandlerMappings(dispatcherServlet);
        addHandlerAdapters(dispatcherServlet);
        initDispatcher(servletContext, dispatcherServlet);
        log.info("Start AppWebApplication Initializer");
    }

    private void addHandlerMappings(final DispatcherServlet dispatcherServlet) {
        dispatcherServlet.addHandlerMapping(new AnnotationHandlerMapping(getClass().getPackageName()));
    }

    private void addHandlerAdapters(final DispatcherServlet dispatcherServlet) {
        dispatcherServlet.addHandlerAdapters(new HandlerExecutionHandlerAdapter());
    }

    private void initDispatcher(final ServletContext servletContext, final DispatcherServlet dispatcherServlet) {
        Dynamic dispatcher = servletContext.addServlet("dispatcher", dispatcherServlet);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
    }
}
