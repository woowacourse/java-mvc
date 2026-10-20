package com.techcourse;

import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecutionHandlerAdapter;
import jakarta.servlet.ServletContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.interface21.web.WebApplicationInitializer;

/**
 * Base class for {@link WebApplicationInitializer}
 * implementations that register a {@link DispatcherServlet} in the servlet context.
 */
public class DispatcherServletInitializer implements WebApplicationInitializer {

    private static final Logger log = LoggerFactory.getLogger(DispatcherServletInitializer.class);

    private static final String DEFAULT_SERVLET_NAME = "dispatcher";

    @Override
    public void onStartup(final ServletContext servletContext) {
        final var dispatcherServlet = new DispatcherServlet();
        addHandlerMappings(dispatcherServlet);
        addHandlerAdapters(dispatcherServlet);

        final var registration = servletContext.addServlet(DEFAULT_SERVLET_NAME, dispatcherServlet);
        if (registration == null) {
            throw new IllegalStateException("Failed to register servlet with name '" + DEFAULT_SERVLET_NAME + "'. " +
                    "Check if there is another servlet registered under the same name.");
        }

        registration.setLoadOnStartup(1);
        registration.addMapping("/");

        log.info("Start AppWebApplication Initializer");
    }

    // 새로운 HandlerMapping, HandlerAdapter가 생기면 DispatcherServlet이 아닌 이곳만 수정한다
    private void addHandlerMappings(final DispatcherServlet dispatcherServlet) {
        dispatcherServlet.addHandlerMapping(new AnnotationHandlerMapping("com.techcourse"));
    }

    private void addHandlerAdapters(final DispatcherServlet dispatcherServlet) {
        dispatcherServlet.addHandlerAdapter(new HandlerExecutionHandlerAdapter());
    }
}
