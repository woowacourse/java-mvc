package com.interface21.webmvc.servlet.view;

import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedirectView implements View {

    private static final Logger log = LoggerFactory.getLogger(RedirectView.class);

    public static final String REDIRECT_PREFIX = "redirect:";

    private final String redirectUrl;

    public RedirectView(final String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    @Override
    public void render(
            final Map<String, ?> model,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws Exception {
        log.debug("Redirect to view: {}", redirectUrl);
        response.sendRedirect(redirectUrl);
    }
}
