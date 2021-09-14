package com.techcourse;

import com.techcourse.adapter.AnnotationHandlerAdapter;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration;
import nextstep.mvc.DispatcherServlet;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.web.WebApplicationInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppWebApplicationInitializer implements WebApplicationInitializer {

    private static final Logger LOG = LoggerFactory.getLogger(AppWebApplicationInitializer.class);
    private static final String PACKAGE_PATH = "com.techcourse";
    public static final String NEXTSTEP_MVC_PACKAGE_PATH = "nextstep.mvc";

    @Override
    public void onStartup(ServletContext servletContext) {
        final DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.addHandlerMapping(new AnnotationHandlerMapping(PACKAGE_PATH));
        dispatcherServlet.addHandlerMapping(new AnnotationHandlerMapping(NEXTSTEP_MVC_PACKAGE_PATH));
        dispatcherServlet.addHandlerAdapter(new AnnotationHandlerAdapter());

        final ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", dispatcherServlet);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        LOG.info("Start AppWebApplication Initializer");
    }
}
