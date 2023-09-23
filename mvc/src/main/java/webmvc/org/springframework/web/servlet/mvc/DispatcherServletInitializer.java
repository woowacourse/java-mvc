package webmvc.org.springframework.web.servlet.mvc;

import jakarta.servlet.ServletContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.WebApplicationInitializer;
import webmvc.org.springframework.web.servlet.mvc.tobe.handler.adapter.AnnotationHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.handler.adapter.HandlerAdapters;
import webmvc.org.springframework.web.servlet.mvc.tobe.handler.mapper.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.handler.mapper.HandlerMappers;

/**
 * Base class for {@link WebApplicationInitializer}
 * implementations that register a {@link DispatcherServlet} in the servlet context.
 */
public class DispatcherServletInitializer implements WebApplicationInitializer {

    private static final Logger log = LoggerFactory.getLogger(DispatcherServletInitializer.class);

    private static final String DEFAULT_SERVLET_NAME = "dispatcher";
    private static final String ANNOTATION_BASED_CONTROLLER_PACKAGE = "com.techcourse";

    @Override
    public void onStartup(final ServletContext servletContext) {
        HandlerMappers handlerMappers = new HandlerMappers();
        HandlerAdapters handlerAdapters = new HandlerAdapters();

        handlerMappers.addHandlerMapper(new AnnotationHandlerMapping(ANNOTATION_BASED_CONTROLLER_PACKAGE));
        handlerAdapters.addHandlerAdapter(new AnnotationHandlerAdapter());

        DispatcherServlet dispatcherServlet = new DispatcherServlet(handlerMappers, handlerAdapters);

        final var registration = servletContext.addServlet(DEFAULT_SERVLET_NAME, dispatcherServlet);
        if (registration == null) {
            throw new IllegalStateException("Failed to register servlet with name '" + DEFAULT_SERVLET_NAME + "'. " +
                    "Check if there is another servlet registered under the same name.");
        }

        registration.setLoadOnStartup(1);
        registration.addMapping("/");

        log.info("Start AppWebApplication Initializer");
    }
}
