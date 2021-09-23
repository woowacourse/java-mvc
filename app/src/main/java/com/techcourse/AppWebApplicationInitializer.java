package com.techcourse;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration;
import java.util.ArrayList;
import nextstep.mvc.handleradapter.AnnotaionHandlerAdapter;
import nextstep.mvc.handleradapter.HandlerAdapterRegistry;
import nextstep.mvc.handlermapping.AnnotationHandlerMapping;
import nextstep.mvc.handlermapping.HandlerMappingRegistry;
import nextstep.mvc.servlet.DispatcherServlet;
import nextstep.web.WebApplicationInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppWebApplicationInitializer implements WebApplicationInitializer {

    private static final Logger log = LoggerFactory.getLogger(AppWebApplicationInitializer.class);

    @Override
    public void onStartup(ServletContext servletContext) {
        final DispatcherServlet dispatcherServlet = generateDispatchServletWithRegistry();
        dispatcherServlet.addHandlerMapping(new AnnotationHandlerMapping("com.techcourse"));
        dispatcherServlet.addHandlerAdapter(new AnnotaionHandlerAdapter());

        final ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher",
            dispatcherServlet);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        log.info("Start AppWebApplication Initializer");
    }

    private DispatcherServlet generateDispatchServletWithRegistry() {
        final HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry(
            new ArrayList<>());
        final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry(
            new ArrayList<>());
        return new DispatcherServlet(handlerAdapterRegistry, handlerMappingRegistry);
    }
}
