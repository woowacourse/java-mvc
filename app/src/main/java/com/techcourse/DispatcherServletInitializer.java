package com.techcourse;

import jakarta.servlet.ServletContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.WebApplicationInitializer;
import webmvc.org.springframework.web.servlet.DispatcherServlet;
import webmvc.org.springframework.web.servlet.mvc.adapter.Adapter;
import webmvc.org.springframework.web.servlet.mvc.adapter.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.adapter.HandlerExecutionAdapter;
import webmvc.org.springframework.web.servlet.mvc.mapper.HandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.mapper.Mapper;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for {@link WebApplicationInitializer}
 * implementations that register a {@link DispatcherServlet} in the servlet context.
 */
public class DispatcherServletInitializer implements WebApplicationInitializer {

    private static final Logger log = LoggerFactory.getLogger(DispatcherServletInitializer.class);

    private static final String DEFAULT_SERVLET_NAME = "dispatcher";

    @Override
    public void onStartup(final ServletContext servletContext) {
        final var dispatcherServlet = new DispatcherServlet(handlerMapping(), handlerAdapter());

        final var registration = servletContext.addServlet(DEFAULT_SERVLET_NAME, dispatcherServlet);
        if (registration == null) {
            throw new IllegalStateException("Failed to register servlet with name '" + DEFAULT_SERVLET_NAME + "'. " +
                    "Check if there is another servlet registered under the same name.");
        }

        registration.setLoadOnStartup(1);
        registration.addMapping("/");

        log.info("Start AppWebApplication Initializer");
    }


    private HandlerMapping handlerMapping() {
        List<Mapper> mappers = new ArrayList<>();
        mappers.add(new AnnotationHandlerMapping("com.techcourse.controller"));
        return new HandlerMapping(mappers);
    }

    private HandlerAdapter handlerAdapter() {
        List<Adapter> adapters = new ArrayList<>();
        adapters.add(new HandlerExecutionAdapter());
        return new HandlerAdapter(adapters);
    }
}
