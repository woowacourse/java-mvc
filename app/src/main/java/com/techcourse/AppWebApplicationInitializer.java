package com.techcourse;

import jakarta.servlet.ServletContext;
import nextstep.mvc.DispatcherServlet;
import nextstep.web.WebApplicationInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppWebApplicationInitializer implements WebApplicationInitializer {

    private static final Logger log = LoggerFactory.getLogger(AppWebApplicationInitializer.class);

    @Override
    public void onStartup(final ServletContext servletContext) {
        // DispatcherServlet 생성
        final var dispatcherServlet = new DispatcherServlet();
        // DispatcherServlet에 HandlerMapping 등록
        // AnnotationHandlerMapping도 넣어줘야한다.
        dispatcherServlet.addHandlerMapping(new ManualHandlerMapping());

        // ServletContext에 DispatcherServlet 등록
        final var dispatcher = servletContext.addServlet("dispatcher", dispatcherServlet);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        log.info("Start AppWebApplication Initializer");
    }
}
