package com.techcourse;

import jakarta.servlet.ServletContext;
import nextstep.mvc.DispatcherServlet;
import nextstep.mvc.controller.tobe.AnnotationHandlerAdapter;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.web.WebApplicationInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppWebApplicationInitializer implements WebApplicationInitializer {

    private static final Logger log = LoggerFactory.getLogger(AppWebApplicationInitializer.class);

    @Override
    public void onStartup(final ServletContext servletContext) {
        var dispatcherServlet = new DispatcherServlet();
        // 3단계 요구사항에 의해서 Manual Controller 제거
        /*
        dispatcherServlet.addHandlerMapping(new ManualHandlerMapping());
        dispatcherServlet.addAdapterMapping(new ControllerHandlerAdapter());
        */
        dispatcherServlet.addHandlerMapping(new AnnotationHandlerMapping("com.techcourse"));
        dispatcherServlet.addAdapterMapping(new AnnotationHandlerAdapter());

        var dispatcher = servletContext.addServlet("dispatcher", dispatcherServlet);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        log.info("Start AppWebApplication Initializer");
    }
}
