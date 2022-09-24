package nextstep.mvc.config;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import nextstep.mvc.controller.annotation.HandlerExecutionHandlerAdapter;
import nextstep.mvc.DispatcherServlet;
import nextstep.mvc.controller.annotation.AnnotationHandlerMapping;
import nextstep.web.WebApplicationInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultAppWebApplicationInitializer implements WebApplicationInitializer {

    private static final Logger log = LoggerFactory.getLogger(DefaultAppWebApplicationInitializer.class);

    @Override
    public void onStartup(final ServletContext servletContext) throws ServletException {
        final var dispatcherServlet = DispatcherServlet.getInstance();
        dispatcherServlet.addHandlerMapping(new AnnotationHandlerMapping());
        dispatcherServlet.addHandlerAdapter(new HandlerExecutionHandlerAdapter());

        final var dispatcher = servletContext.addServlet("dispatcher", dispatcherServlet);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        log.info("Start DefaultAppWebApplication Initializer");
    }
}
