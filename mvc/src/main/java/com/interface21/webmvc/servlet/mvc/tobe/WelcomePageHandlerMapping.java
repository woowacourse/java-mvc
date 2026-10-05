package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;

public class WelcomePageHandlerMapping implements HandlerMapping {

    private static final String WELCOME_PAGE = "/index.html";

    private final ServletContext servletContext;
    private WelcomePageHandler welcomePageHandler;

    public WelcomePageHandlerMapping(final ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public void initialize() {
        if (existsWelcomePage()) {
            welcomePageHandler = new WelcomePageHandler(WELCOME_PAGE);
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        if ("/".equals(request.getRequestURI())) {
            return welcomePageHandler;
        }
        return null;
    }

    private boolean existsWelcomePage() {
        try {
            return servletContext.getResource(WELCOME_PAGE) != null;
        } catch (MalformedURLException e) {
            throw new IllegalStateException(e);
        }
    }
}
