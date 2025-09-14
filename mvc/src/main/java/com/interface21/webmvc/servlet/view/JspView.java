package com.interface21.webmvc.servlet.view;

import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {

        // 리다이렉트 처리
        if (viewName.startsWith(REDIRECT_PREFIX)) {
            String redirectPath = viewName.substring(REDIRECT_PREFIX.length());
            log.debug("Redirecting to: {}", redirectPath);
            response.sendRedirect(redirectPath);
            return;
        }

        // 모델 세팅
        model.keySet().forEach(key -> {
            log.debug("attribute name : {}, value : {}", key, model.get(key));
            request.setAttribute(key, model.get(key));
        });

        // JSP 포워드
        log.debug("Forwarding to view: {}", viewName);
        request.getRequestDispatcher(viewName).forward(request, response);
    }
}
