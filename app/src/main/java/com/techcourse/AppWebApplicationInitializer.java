package com.techcourse;

import com.interface21.web.WebApplicationInitializer;
import com.interface21.webmvc.servlet.DispatcherServlet;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration.Dynamic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class AppWebApplicationInitializer implements WebApplicationInitializer {

    private static final Logger log = LoggerFactory.getLogger(AppWebApplicationInitializer.class);

    @Override
    public void onStartup(final ServletContext servletContext) {
        final DispatcherServlet dispatcherServlet = new DispatcherServlet("com.techcourse");
        final Dynamic registration = servletContext.addServlet("dispatcher", dispatcherServlet);
        registration.setLoadOnStartup(1);
        registration.addMapping("/");
        log.info("Start AppWebApplication Initializer");
    }
}
