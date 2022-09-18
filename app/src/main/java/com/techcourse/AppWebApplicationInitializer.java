package com.techcourse;

import jakarta.servlet.ServletContext;
import java.util.List;
import nextstep.mvc.DispatcherServlet;
import nextstep.mvc.controller.asis.ManualHandlerAdapter;
import nextstep.mvc.controller.tobe.AnnotationHandlerAdapter;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.web.WebApplicationInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppWebApplicationInitializer implements WebApplicationInitializer {

    private static final Logger log = LoggerFactory.getLogger(AppWebApplicationInitializer.class);

    @Override
    public void onStartup(final ServletContext servletContext) {
        final var dispatcherServlet = new DispatcherServlet(
                List.of(new ManualHandlerMapping(), new AnnotationHandlerMapping()),
                List.of(new ManualHandlerAdapter(), new AnnotationHandlerAdapter())
        );

        final var dispatcher = servletContext.addServlet("dispatcher", dispatcherServlet);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        log.info("Start AppWebApplication Initializer");
    }
}
