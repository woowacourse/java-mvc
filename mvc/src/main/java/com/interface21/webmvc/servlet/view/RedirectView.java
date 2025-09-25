package com.interface21.webmvc.servlet.view;

import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

public class RedirectView implements View {

    private final String redirectUrl;

    public RedirectView(final String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    @Override
    public void render(final Map<String, ?> model,
                       final HttpServletRequest request,
                       final HttpServletResponse response) throws Exception {
        response.sendRedirect(redirectUrl);
    }
}

