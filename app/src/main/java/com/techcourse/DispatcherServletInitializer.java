package com.techcourse;

import com.techcourse.controller.LoginController;
import com.techcourse.controller.LoginViewController;
import com.techcourse.controller.LogoutController;
import com.techcourse.controller.RegisterController;
import com.techcourse.controller.RegisterViewController;
import jakarta.servlet.ServletContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.WebApplicationInitializer;
import webmvc.org.springframework.web.servlet.DispatcherServlet;
import webmvc.org.springframework.web.servlet.mvc.ForwardController;
import webmvc.org.springframework.web.servlet.mvc.InterfaceBasedHandlerMapping;

/**
 * Base class for {@link WebApplicationInitializer}
 * implementations that register a {@link DispatcherServlet} in the servlet context.
 */
public class DispatcherServletInitializer implements WebApplicationInitializer {

    private static final Logger log = LoggerFactory.getLogger(DispatcherServletInitializer.class);

    private static final String DEFAULT_SERVLET_NAME = "dispatcher";

    @Override
    public void onStartup(final ServletContext servletContext) {
        InterfaceBasedHandlerMapping.addController("/", new ForwardController("/index.jsp"));
        InterfaceBasedHandlerMapping.addController("/login", new LoginController());
        InterfaceBasedHandlerMapping.addController("/login/view", new LoginViewController());
        InterfaceBasedHandlerMapping.addController("/logout", new LogoutController());
        InterfaceBasedHandlerMapping.addController("/register/view", new RegisterViewController());
        InterfaceBasedHandlerMapping.addController("/register", new RegisterController());

        final var dispatcherServlet = new DispatcherServlet();

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

