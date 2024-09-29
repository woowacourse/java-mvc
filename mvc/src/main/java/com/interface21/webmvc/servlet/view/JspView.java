package com.interface21.webmvc.servlet.view;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.webmvc.servlet.View;

public class JspView implements View {

    private static final String REDIRECT_PREFIX = "redirect:";
    private static final Logger log = LoggerFactory.getLogger(JspView.class);

    private final String viewName;

    public JspView(final String viewName) {
        this.viewName = viewName;
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        if (isForwardView(viewName)) {
            forward(model, request, response);
            return;
        }

        response.sendRedirect(getRedirectUrl(viewName));
    }

    private boolean isForwardView(String viewName) {
        return viewName != null && !viewName.startsWith(REDIRECT_PREFIX);
    }

    private void forward(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        model.keySet().forEach(key -> {
            log.debug("attribute name : {}, value : {}", key, model.get(key));
            request.setAttribute(key, model.get(key));
        });

        final RequestDispatcher requestDispatcher = request.getRequestDispatcher(viewName);
        requestDispatcher.forward(request, response);
    }

    private String getRedirectUrl(String viewName) {
        return viewName.substring(REDIRECT_PREFIX.length());
    }
}
