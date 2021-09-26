package com.techcourse;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration;
import nextstep.mvc.DispatcherServlet;
import nextstep.mvc.mapping.AnnotationHandlerMapping;
import nextstep.mvc.mapping.HandlerMappings;
import nextstep.mvc.view.JspFileViewResolver;
import nextstep.mvc.view.ViewResolvers;
import nextstep.web.WebApplicationInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppWebApplicationInitializer implements WebApplicationInitializer {

    private static final Logger log = LoggerFactory.getLogger(AppWebApplicationInitializer.class);

    @Override
    public void onStartup(ServletContext servletContext) {
        log.info("Start AppWebApplication Initializer");

        final DispatcherServlet dispatcherServlet = new DispatcherServlet();

        addHandlerMappings(dispatcherServlet);
        addViewResolvers(dispatcherServlet);

        final ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", dispatcherServlet);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
    }

    private void addViewResolvers(DispatcherServlet dispatcherServlet) {
        ViewResolvers viewResolvers = dispatcherServlet.getViewResolvers();
        viewResolvers.addViewResolver(new JspFileViewResolver("app/webapp"));
        log.info("added JspFileViewAdapter as default");
    }

    private void addHandlerMappings(DispatcherServlet dispatcherServlet) {
        HandlerMappings handlerMappings = dispatcherServlet.getHandlerMappings();
        handlerMappings.addHandlerMapping(new AnnotationHandlerMapping("com.techcourse"));
        log.info("added current module's annotation based handlers");
    }
}
