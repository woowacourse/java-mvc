package com.techcourse;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration.Dynamic;
import nextstep.mvc.DispatcherServlet;
import nextstep.mvc.controller.tobe.adapters.AnnotationHandlerAdapter;
import nextstep.mvc.controller.tobe.adapters.ManualHandlerAdapter;
import nextstep.mvc.controller.tobe.mappings.AnnotationHandlerMapping;
import nextstep.mvc.controller.tobe.resolvers.DefaultResolver;
import nextstep.mvc.controller.tobe.resolvers.JsonResolver;
import nextstep.mvc.controller.tobe.resolvers.JspResolver;
import nextstep.web.WebApplicationInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppWebApplicationInitializer implements WebApplicationInitializer {

    private static final Logger log = LoggerFactory.getLogger(AppWebApplicationInitializer.class);

    @Override
    public void onStartup(final ServletContext servletContext) {
        final DispatcherServlet dispatcherServlet = initializeDispatcherServlet();

        final Dynamic dispatcher = servletContext.addServlet("dispatcher", dispatcherServlet);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        log.info("Start AppWebApplication Initializer");
    }

    private static DispatcherServlet initializeDispatcherServlet() {
        final DispatcherServlet dispatcherServlet = new DispatcherServlet();

        dispatcherServlet.addHandlerMapping(new ManualHandlerMapping());
        dispatcherServlet.addHandlerMapping(new AnnotationHandlerMapping("com.techcourse"));
        dispatcherServlet.addHandlerAdapter(new ManualHandlerAdapter());
        dispatcherServlet.addHandlerAdapter(new AnnotationHandlerAdapter());
        dispatcherServlet.addModelAndViewResolver(new DefaultResolver());
        dispatcherServlet.addModelAndViewResolver(new JspResolver());
        dispatcherServlet.addModelAndViewResolver(new JsonResolver());

        return dispatcherServlet;
    }
}
