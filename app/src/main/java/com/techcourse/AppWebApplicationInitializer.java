package com.techcourse;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration;
import nextstep.mvc.DispatcherServlet;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.web.WebApplicationInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppWebApplicationInitializer implements WebApplicationInitializer {
    private static final Logger log = LoggerFactory.getLogger(AppWebApplicationInitializer.class);
    private static final String BASE_PACKAGE_PATH = "com.techcourse";

    @Override
    public void onStartup(ServletContext servletContext) {
        final DispatcherServlet dispatcherServlet = new DispatcherServlet();
        // 진짜 Manual한 친구네
        dispatcherServlet.addHandlerMapping(new ManualHandlerMapping());

        // 어노테이션 기반 추가
        dispatcherServlet.addHandlerMapping(new AnnotationHandlerMapping(BASE_PACKAGE_PATH));

        final ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", dispatcherServlet);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        log.info("Start AppWebApplication Initializer");
    }
}
