package com.interface21.webmvc.servlet.view;

import java.util.Map;

import jakarta.servlet.RequestDispatcher;
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

    public static boolean isRedirectView(String viewName) {
        return viewName != null && viewName.startsWith(REDIRECT_PREFIX);
    }

    public static String getRedirectUrl(String viewName) {
        if (!isRedirectView(viewName)) {
            throw new IllegalArgumentException("redirect를 사용하지 않은 viewName입니다.");
        }

        return viewName.substring(REDIRECT_PREFIX.length());
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        if (viewName.startsWith(JspView.REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(JspView.REDIRECT_PREFIX.length()));
            return;
        }

        model.keySet().forEach(key -> {
            log.debug("attribute name : {}, value : {}", key, model.get(key));
            request.setAttribute(key, model.get(key));
        });

        final RequestDispatcher requestDispatcher = request.getRequestDispatcher(viewName);
        requestDispatcher.forward(request, response);
    }
}
