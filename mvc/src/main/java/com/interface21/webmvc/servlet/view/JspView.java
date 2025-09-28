package com.interface21.webmvc.servlet.view;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class JspView implements View {

    private static final Logger log = LoggerFactory.getLogger(JspView.class);

    public static final String REDIRECT_PREFIX = "redirect:";

    private final String viewName;

    public JspView(final String viewName) {
        this.viewName = viewName;
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        if (handleRedirectIfNeeded(response)) {
            return;
        }

        applyModelToRequest(model, request);
        forward(request, response);
    }

    private boolean handleRedirectIfNeeded(HttpServletResponse response) throws IOException {
        if (viewName.startsWith(REDIRECT_PREFIX)) {
            String redirectPath = viewName.substring(REDIRECT_PREFIX.length());
            log.debug("Redirecting to: {}", redirectPath);
            response.sendRedirect(redirectPath);
            return true;
        }
        return false;
    }

    private void applyModelToRequest(Map<String, ?> model, HttpServletRequest request) {
        model.keySet().forEach(key -> {
            log.debug("attribute name : {}, value : {}", key, model.get(key));
            request.setAttribute(key, model.get(key));
        });
    }

    private void forward(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Forwarding to view: {}", viewName);
        request.getRequestDispatcher(viewName).forward(request, response);
    }
}
