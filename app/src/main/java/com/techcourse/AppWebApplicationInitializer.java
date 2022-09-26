package com.techcourse;

import jakarta.servlet.ServletContext;
import nextstep.mvc.DispatcherServlet;
import nextstep.mvc.mapping.AnnotationHandlerMapping;
import nextstep.mvc.adapter.ModelAndViewHandlerAdapter;
import nextstep.mvc.adapter.ViewNameHandlerAdapter;
import nextstep.mvc.adapter.VoidHandlerAdapter;
import nextstep.web.WebApplicationInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppWebApplicationInitializer implements WebApplicationInitializer {

    private static final Logger log = LoggerFactory.getLogger(AppWebApplicationInitializer.class);

    @Override
    public void onStartup(final ServletContext servletContext) {
        log.info("Start AppWebApplication Initializer");
        final var dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.addHandlerMapping(new AnnotationHandlerMapping("com.techcourse"));
        dispatcherServlet.addHandlerAdapter(new ModelAndViewHandlerAdapter());
        dispatcherServlet.addHandlerAdapter(new ViewNameHandlerAdapter());
        dispatcherServlet.addHandlerAdapter(new VoidHandlerAdapter());

        final var dispatcher = servletContext.addServlet("dispatcher", dispatcherServlet);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
    }
}
