package com.interface21.webmvc.servlet.view;

import com.interface21.webmvc.servlet.View;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        log.debug("Rendering view: {} | Method: {} | Request URI: {}", viewName, request.getMethod(),
                request.getRequestURI());

        setAttributes(model, request);
        if (isRedirect()) {
            handleRedirect(response);
            return;
        }
        forwardToView(request, response);

        throw new IllegalArgumentException("RequestDispatcher is null for viewName: " + viewName);
    }

    private void forwardToView(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(viewName);
        if (requestDispatcher != null) {
            log.debug("Forwarding to view: {}", viewName);
            requestDispatcher.forward(request, response);
        }
    }

    private void handleRedirect(HttpServletResponse response) throws IOException {
        log.debug("Redirecting to: {}", viewName);
        response.sendRedirect(viewName.substring(REDIRECT_PREFIX.length()));
    }

    private boolean isRedirect() {
        return viewName.startsWith(REDIRECT_PREFIX);
    }

    private void setAttributes(Map<String, ?> model, HttpServletRequest request) {
        model.keySet().forEach(key -> {
            log.debug("attribute name : {}, value : {}", key, model.get(key));
            request.setAttribute(key, model.get(key));
        });
    }
}
