package com.interface21.webmvc.servlet.view;

import com.interface21.webmvc.servlet.View;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class JspView implements View {

    private static final Logger log = LoggerFactory.getLogger(JspView.class);

    private final ViewName viewName;

    public JspView(final String viewName) {
        this.viewName = new ViewName(viewName);
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        if (viewName.isRedirect()) {
            response.sendRedirect(viewName.getUri());
            return;
        }

        setAttributeToRequest(model, request);
        final RequestDispatcher requestDispatcher = request.getRequestDispatcher(viewName.getUri());
        requestDispatcher.forward(request, response);
    }

    private void setAttributeToRequest(final Map<String, ?> model, final HttpServletRequest request) {
        model.keySet().forEach(key -> {
            log.debug("attribute name : {}, value : {}", key, model.get(key));
            request.setAttribute(key, model.get(key));
        });
    }
}
