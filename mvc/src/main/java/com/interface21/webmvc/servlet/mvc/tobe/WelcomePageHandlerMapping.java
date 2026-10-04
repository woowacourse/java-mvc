package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;

public class WelcomePageHandlerMapping implements HandlerMapping {

    private static final String WELCOME_PAGE = "/index.html";

    @Override
    public Object getHandler(HttpServletRequest request) {
        if (!"/".equals(request.getRequestURI())) {
            return null;
        }
        if (existsWelcomePage(request)) {
            return new WelcomePageHandler(WELCOME_PAGE);
        }
        return null;
    }

    private boolean existsWelcomePage(final HttpServletRequest request) {
        try {
            return request.getServletContext().getResource(WELCOME_PAGE) != null;
        } catch (MalformedURLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void initialize() {
    }
}
