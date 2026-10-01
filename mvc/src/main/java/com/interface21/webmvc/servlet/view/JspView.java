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
        if (isRedirectView()) {
            response.sendRedirect(viewName.substring(REDIRECT_PREFIX.length()));
            return;
        }

        exposeModelAsRequestAttributes(model, request);
        request.getRequestDispatcher(viewName).forward(request, response);
    }

    private boolean isRedirectView() {
        return viewName.startsWith(REDIRECT_PREFIX);
    }

    private void exposeModelAsRequestAttributes(final Map<String, ?> model, final HttpServletRequest request) {
        model.forEach((name, value) -> {
            log.debug("attribute name : {}, value : {}", name, value);
            request.setAttribute(name, value);
        });
    }
}
