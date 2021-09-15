package com.techcourse;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration;
import nextstep.mvc.DispatcherServlet;
import nextstep.mvc.adapter.AnnotationHandlerAdapter;
import nextstep.mvc.adapter.ManualHandlerAdapter;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.web.WebApplicationInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppWebApplicationInitializer implements WebApplicationInitializer {

    private static final Logger log = LoggerFactory.getLogger(AppWebApplicationInitializer.class);

    @Override
    public void onStartup(ServletContext servletContext) {
        final DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.addToHandlerMappingRegistry(
                new ManualHandlerMapping(),
                new AnnotationHandlerMapping("com.techcourse.controller")
        );
        dispatcherServlet.addToHandlerAdapterRegistry(
                new AnnotationHandlerAdapter(),
                new ManualHandlerAdapter()
        );

        final ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", dispatcherServlet);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        log.info("Start AppWebApplication Initializer");
    }
}
