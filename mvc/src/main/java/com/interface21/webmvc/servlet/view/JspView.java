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
    private static final String RESOURCE_NOT_FOUND_PAGE = "/404.jsp";
    public static final String REDIRECT_PREFIX = "redirect:";

    private final String viewName;

    public JspView(final String viewName) {
        this.viewName = viewName;
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        log.info("Rendering view: {} | Method: {} | Request URI: {}", viewName, request.getMethod(),
                request.getRequestURI());

        if (isRedirect()) {
            handleRedirect(response);
            return;
        }

        forward(model, request, response);
    }

    private boolean isRedirect() {
        return viewName.startsWith(REDIRECT_PREFIX);
    }

    private void handleRedirect(HttpServletResponse response) throws IOException {
        log.info("Redirecting to: {}", viewName);
        response.sendRedirect(viewName.substring(REDIRECT_PREFIX.length()));
    }

    private void forward(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setAttributes(model, request);

        try {
            forwardToView(request, response);
        } catch (IllegalArgumentException e) {
            log.error("Error forwarding to view: {}", viewName, e);
            response.sendRedirect(RESOURCE_NOT_FOUND_PAGE);
        }
    }

    private void setAttributes(Map<String, ?> model, HttpServletRequest request) {
        model.keySet().forEach(key -> {
            log.info("attribute name : {}, value : {}", key, model.get(key));
            request.setAttribute(key, model.get(key));
        });
    }

    private void forwardToView(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(viewName);
        if (requestDispatcher == null) {
            throw new IllegalArgumentException("RequestDispatcher is null for viewName: " + viewName);
        }
        log.info("Forwarding to view: {}", viewName);
        requestDispatcher.forward(request, response);
    }
}
