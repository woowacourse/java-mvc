package com.techcourse.air.mvc;

import com.techcourse.air.core.context.ApplicationContext;
import com.techcourse.air.core.context.ApplicationContextProvider;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration;
import com.techcourse.air.mvc.core.DispatcherServlet;
import com.techcourse.air.mvc.web.WebApplicationInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppWebApplicationInitializer implements WebApplicationInitializer {

    private static final Logger log = LoggerFactory.getLogger(AppWebApplicationInitializer.class);

    private final ApplicationContext context = ApplicationContextProvider.getApplicationContext();

    @Override
    public void onStartup(ServletContext servletContext) {
        DispatcherServlet dispatcherServlet = context.findBeanByType(DispatcherServlet.class);
        final ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", dispatcherServlet);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        log.info("Start AppWebApplication Initializer");
    }
}
