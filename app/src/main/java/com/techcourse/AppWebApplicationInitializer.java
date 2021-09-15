package com.techcourse;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration;
import nextstep.mvc.DispatcherServlet;
import nextstep.mvc.controller.tobe.handler.adapter.AnnotationHandlerAdapter;
import nextstep.mvc.controller.tobe.handler.adapter.ManualHandlerAdapter;
import nextstep.mvc.controller.tobe.handler.mapping.AnnotationHandlerMapping;
import nextstep.web.WebApplicationInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppWebApplicationInitializer implements WebApplicationInitializer {

    private static final Logger LOG = LoggerFactory.getLogger(AppWebApplicationInitializer.class);

    @Override
    public void onStartup(ServletContext servletContext) {
        final DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.addHandlerMappings(
            new ManualHandlerMapping(),
            new AnnotationHandlerMapping("com.techcourse.controller")
        );
        dispatcherServlet.addHandlerAdapters(
            new ManualHandlerAdapter(),
            new AnnotationHandlerAdapter()
        );

        final ServletRegistration.Dynamic dispatcher = servletContext
            .addServlet("dispatcher", dispatcherServlet);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        LOG.info("Start AppWebApplicationInitializer");
    }
}
