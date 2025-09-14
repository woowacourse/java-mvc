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
        validateRedirectUrl(redirectUrl);
        log.info("Redirecting to {}", redirectUrl);
        response.sendRedirect(redirectUrl);
    }

    private void validateRedirectUrl(final String redirectUrl) {
        if (redirectUrl == null || redirectUrl.trim().isEmpty()) {
            throw new IllegalStateException("리다이렉트 URL은 비어있을 수 없습니다.");
        }
        if (!redirectUrl.startsWith("/")) {
            throw new IllegalStateException("외부 URL 또는 상대 경로로는 리다이렉트할 수 없습니다. '/'로 시작하는 절대 경로만 사용해주세요.");
        }
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
