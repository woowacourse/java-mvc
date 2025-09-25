package com.techcourse;

import com.interface21.web.WebApplicationInitializer;
import com.interface21.webmvc.servlet.mvc.adapter.ControllerHandlerAdaptor;
import com.interface21.webmvc.servlet.mvc.adapter.HandlerExecutionHandlerAdaptor;
import com.interface21.webmvc.servlet.mvc.asis.ForwardController;
import com.interface21.webmvc.servlet.mvc.mapping.AnnotationHandlerMapping;
import com.techcourse.controller.LoginController;
import com.techcourse.controller.LoginViewController;
import com.techcourse.controller.LogoutController;
import com.techcourse.controller.RegisterController;
import com.techcourse.controller.RegisterViewController;
import jakarta.servlet.ServletContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for {@link WebApplicationInitializer}
 * implementations that register a {@link DispatcherServlet} in the servlet context.
 */
public class DispatcherServletInitializer implements WebApplicationInitializer {

    private static final Logger log = LoggerFactory.getLogger(DispatcherServletInitializer.class);

    private static final String DEFAULT_SERVLET_NAME = "dispatcher";

    @Override
    public void onStartup(final ServletContext servletContext) {

        final var dispatcherServlet = new DispatcherServlet(createHandlerMappingRegistry(), createHandlerAdaptorRegistry());

        final var registration = servletContext.addServlet(DEFAULT_SERVLET_NAME, dispatcherServlet);
        if (registration == null) {
            throw new IllegalStateException("Failed to register servlet with name '" + DEFAULT_SERVLET_NAME + "'. " +
                    "Check if there is another servlet registered under the same name.");
        }

        registration.setLoadOnStartup(1);
        registration.addMapping("/");

        log.info("Start AppWebApplication Initializer");
    }

    private HandlerMappingRegistry createHandlerMappingRegistry() {
        HandlerMappingRegistry registry = new HandlerMappingRegistry();

        ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();
        manualHandlerMapping.addController("/", new ForwardController("/index.jsp"));
        manualHandlerMapping.addController("/login", new LoginController());
        manualHandlerMapping.addController("/login/view", new LoginViewController());
        manualHandlerMapping.addController("/logout", new LogoutController());
        manualHandlerMapping.addController("/register/view", new RegisterViewController());
        manualHandlerMapping.addController("/register", new RegisterController());
        registry.addHandlerMapping(manualHandlerMapping);

        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("com.techcourse.controller");
        annotationHandlerMapping.initialize();

        registry.addHandlerMapping(annotationHandlerMapping);
        return registry;
    }

    private HandlerAdaptorRegistry createHandlerAdaptorRegistry() {
        HandlerAdaptorRegistry registry = new HandlerAdaptorRegistry();
        registry.addHandlerAdapter(new ControllerHandlerAdaptor());
        registry.addHandlerAdapter(new HandlerExecutionHandlerAdaptor());
        return registry;
    }
}
