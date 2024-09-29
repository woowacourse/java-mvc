package com.interface21.webmvc.servlet;

import jakarta.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.web.WebApplicationInitializer;
import com.interface21.webmvc.servlet.DispatcherServlet;
import com.interface21.webmvc.servlet.mvc.adapter.HandlerAdapterContainer;
import com.interface21.webmvc.servlet.mvc.mapping.HandlerMappingContainer;

/**
 * Base class for {@link WebApplicationInitializer}
 * implementations that register a {@link DispatcherServlet} in the servlet context.
 */
public class DispatcherServletInitializer implements WebApplicationInitializer {

    private static final Logger log = LoggerFactory.getLogger(DispatcherServletInitializer.class);

    private static final String DEFAULT_SERVLET_NAME = "dispatcher";
    private static final String BASE_PACKAGE = "com";

    @Override
    public void onStartup(final ServletContext servletContext) {
        final var dispatcherServlet = new DispatcherServlet(
                new HandlerMappingContainer(BASE_PACKAGE),
                new HandlerAdapterContainer(BASE_PACKAGE)
        );

        final var registration = servletContext.addServlet(DEFAULT_SERVLET_NAME, dispatcherServlet);
        if (registration == null) {
            throw new IllegalStateException("'" + DEFAULT_SERVLET_NAME + "' 이름으로 서블릿 등록 실패. " +
                    "동일한 이름으로 이미 등록된 서블릿이 있는지 확인하십시오.");
        }

        registration.setLoadOnStartup(1);
        registration.addMapping("/");

        log.info("AppWebApplication 초기화 시작");
    }
}
