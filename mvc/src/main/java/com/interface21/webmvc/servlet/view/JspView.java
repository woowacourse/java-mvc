package com.interface21.webmvc.servlet.view;

import com.interface21.webmvc.servlet.View;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
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
    public void render(final Map<String, ?> model,
                       final HttpServletRequest request,
                       final HttpServletResponse response) throws Exception {
        /**
         * redirect or forward
         *
         * redirect → 클라이언트에 302 응답 → 브라우저가 새 요청을 보냄 → URL 바뀜.
         * forward → 서버 내부에서 JSP로 제어를 넘김 → URL 그대로 유지.
         */
        if (isRedirect()) {
            handleRedirect(response);
            return;
        }

        setModelAttributes(model, request);
        handleForward(request, response);

    }

    private boolean isRedirect() {
        return viewName.startsWith(REDIRECT_PREFIX);
    }

    private void handleRedirect(HttpServletResponse response) throws IOException {
        String redirectPath = viewName.substring(REDIRECT_PREFIX.length());
        log.debug("Redirecting to: {}", redirectPath);
        response.sendRedirect(redirectPath);
    }

    private void setModelAttributes(Map<String, ?> model, HttpServletRequest request) {
        if (model == null) return;
        model.forEach((key, value) -> {
            log.debug("attribute name : {}, value : {}", key, value);
            request.setAttribute(key, value);
        });
    }

    private void handleForward(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Forwarding to view: {}", viewName);
        request.getRequestDispatcher(viewName).forward(request, response);
    }
}
