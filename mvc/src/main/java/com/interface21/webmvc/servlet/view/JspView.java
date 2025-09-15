package com.interface21.webmvc.servlet.view;

import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        setModelAttributes(model, request);
        
        if (isRedirectView()) {
            handleRedirect(response);
            return;
        }
        
        if (isValidViewName()) {
            forwardToJsp(request, response);
        }
    }

    private void setModelAttributes(final Map<String, ?> model, final HttpServletRequest request) {
        model.keySet().forEach(key -> {
            log.debug("attribute name : {}, value : {}", key, model.get(key));
            request.setAttribute(key, model.get(key));
        });
    }

    private boolean isRedirectView() {
        return viewName != null && viewName.startsWith(REDIRECT_PREFIX);
    }

    private boolean isValidViewName() {
        return viewName != null && !viewName.trim().isEmpty();
    }

    private void handleRedirect(final HttpServletResponse response) throws Exception {
        final String redirectUrl = viewName.substring(REDIRECT_PREFIX.length());
        response.sendRedirect(redirectUrl);
    }

    private void forwardToJsp(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final var requestDispatcher = request.getRequestDispatcher(viewName);
        requestDispatcher.forward(request, response);
    }
}
