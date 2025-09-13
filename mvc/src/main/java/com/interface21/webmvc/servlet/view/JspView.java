package com.interface21.webmvc.servlet.view;

import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

public class JspView implements View {

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
        if (viewName.startsWith(REDIRECT_PREFIX)) {
            final var redirectUrl = viewName.substring(REDIRECT_PREFIX.length());

            response.sendRedirect(redirectUrl);
            return;
        }

        model.forEach(request::setAttribute);
        final var dispatcher = request.getRequestDispatcher(viewName);

        dispatcher.forward(request, response);
    }
}
