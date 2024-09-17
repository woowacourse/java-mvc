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
    public static final String REDIRECT_PREFIX = "redirect:";

    private final String viewName;

    public JspView(final String viewName) {
        this.viewName = viewName;
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        // 모델 데이터를 요청 속성으로 설정
        model.keySet().forEach(key -> {
            log.debug("attribute name : {}, value : {}", key, model.get(key));
            request.setAttribute(key, model.get(key));
        });

        // 리디렉션 처리
        if (viewName.startsWith(REDIRECT_PREFIX)) {
            log.debug("Redirecting to: {}", viewName);
            response.sendRedirect(viewName.substring(REDIRECT_PREFIX.length()));
            return;
        }

        // JSP 페이지로 포워딩
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(viewName);
        if (requestDispatcher != null) {
            log.debug("Forwarding to view: {}", viewName);
            requestDispatcher.forward(request, response);
        }

        log.error("RequestDispatcher is null for viewName: {}", viewName);
        throw new IllegalArgumentException("RequestDispatcher is null for viewName: " + viewName);
    }
}
