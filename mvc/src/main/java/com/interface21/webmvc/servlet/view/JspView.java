package com.interface21.webmvc.servlet.view;

import com.interface21.webmvc.servlet.View;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class JspView implements View {

    private static final Logger log = LoggerFactory.getLogger(JspView.class);
    public static final String REDIRECT_PREFIX = "redirect:";

    private final String viewName;

    public JspView(final String viewName) {
        this.viewName = viewName;
    }

    @Override
    public void render(
            final Map<String, ?> model,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws Exception {
        applyModelAttributes(model, request);
        if (isRedirect()) {
            handleRedirect(response);
            return;
        }
        handleForward(request, response);
    }

    private void applyModelAttributes(
            final Map<String, ?> model,
            final HttpServletRequest request
    ) {
        model.forEach((key, value) -> {
            log.debug("attribute name : {}, value : {}", key, model.get(key));
            request.setAttribute(key, value);
        });
    }

    private boolean isRedirect() {
        return viewName.startsWith(REDIRECT_PREFIX);
    }

    private void handleRedirect(final HttpServletResponse response) throws Exception {
        final String redirectUrl = viewName.substring(REDIRECT_PREFIX.length());
        log.info("Redirecting to {}", redirectUrl);
        response.sendRedirect(redirectUrl);
    }

    private void handleForward(
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws Exception {
        log.info("Forwarding to {}", viewName);
        final RequestDispatcher requestDispatcher = request.getRequestDispatcher(viewName);
        requestDispatcher.forward(request, response);
    }
}
