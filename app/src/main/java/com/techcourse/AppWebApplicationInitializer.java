package com.techcourse;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration;
import nextstep.mvc.DispatcherServlet;
import nextstep.mvc.HandlerMapping;
import nextstep.mvc.controller.adapter.ControllerHandlerAdapter;
import nextstep.mvc.controller.adapter.HandlerAdapter;
import nextstep.mvc.controller.adapter.HandlerExecutionHandlerAdapter;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.web.WebApplicationInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppWebApplicationInitializer implements WebApplicationInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppWebApplicationInitializer.class);
    private static final String BASE_PACKAGE = "com.techcourse";

    @Override
    public void onStartup(ServletContext servletContext) {
        final DispatcherServlet dispatcherServlet = new DispatcherServlet();

        addFrameWork(dispatcherServlet, new ManualHandlerMapping(), new ControllerHandlerAdapter());
        addFrameWork(dispatcherServlet, new AnnotationHandlerMapping(BASE_PACKAGE), new HandlerExecutionHandlerAdapter());

        final ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", dispatcherServlet);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        LOGGER.info("Start AppWebApplication Initializer");
    }

    private void addFrameWork(final DispatcherServlet dispatcherServlet, final HandlerMapping handlerMapping, final HandlerAdapter handlerAdapter) {
        dispatcherServlet.addHandlerMapping(handlerMapping);
        dispatcherServlet.addHandlerAdapter(handlerAdapter);
    }
}
