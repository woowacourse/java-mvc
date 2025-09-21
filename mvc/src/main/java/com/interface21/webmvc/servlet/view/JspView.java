package com.interface21.webmvc.servlet.view;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JspView implements View {

    public static final String REDIRECT_PREFIX = "redirect:";
    private static final Logger log = LoggerFactory.getLogger(JspView.class);
    private final String viewName;

    public JspView(String viewName) {
        this.viewName = viewName;
    }

    public static JspView redirectTo(String path) {
        return new JspView(REDIRECT_PREFIX + path);
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {

        // 리다이렉트 처리
        if (handleRedirectIfNeeded(response)) {
            return;
        }
        applyModelToRequest(model, request);
        forward(request, response);
    }

    private boolean handleRedirectIfNeeded(HttpServletResponse response) throws IOException {
        if (viewName.startsWith(REDIRECT_PREFIX)) {
            String redirectPath = viewName.substring(REDIRECT_PREFIX.length());
            log.debug("Redirecting to: {}", redirectPath);
            response.sendRedirect(redirectPath);
            return true;
        }
        return false;
    }

    // 모델 세팅
    private void applyModelToRequest(Map<String, ?> model, HttpServletRequest request) {
        model.keySet().forEach(key -> {
            log.debug("attribute name : {}, value : {}", key, model.get(key));
            request.setAttribute(key, model.get(key));
        });
    }

    // JSP 포워드
    private void forward(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.debug("Forwarding to view: {}", viewName);
        request.getRequestDispatcher(viewName).forward(request, response);
    }
}
