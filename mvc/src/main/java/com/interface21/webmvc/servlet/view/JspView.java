package com.interface21.webmvc.servlet.view;

import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

public class JspView implements View {

    public static final String REDIRECT_PREFIX = "redirect:";

    private final String name;

    public JspView(final String name) {
        this.name = name;
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        if (isRedirectView()) {
            response.sendRedirect(getRedirectUrl());
            return;
        }

        model.forEach(request::setAttribute);
        request.getRequestDispatcher(name).forward(request, response);
    }

    private boolean isRedirectView() {
        return name.startsWith(REDIRECT_PREFIX);
    }

    private String getRedirectUrl() {
        return name.substring(REDIRECT_PREFIX.length());
    }
}
