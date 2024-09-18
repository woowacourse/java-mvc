package com.interface21.webmvc.servlet.view;

import com.interface21.webmvc.servlet.View;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JspView implements View {

    private static final Logger log = LoggerFactory.getLogger(JspView.class);

    public static final String REDIRECT_PREFIX = "redirect:";
    private static final String NOT_FOUND_VIEW = "404.jsp";

    private final String viewName;

    public JspView(final String viewName) {
        this.viewName = viewName;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        if (viewName.startsWith(JspView.REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(JspView.REDIRECT_PREFIX.length()));
            return;
        }

        setModelAttribute(model, request);
        forwardRequest(request, response);
    }

    private void setModelAttribute(Map<String, ?> model, HttpServletRequest request) {
        model.keySet().forEach(key -> {
            log.debug("attribute name : {}, value : {}", key, model.get(key));
            request.setAttribute(key, model.get(key));
        });
    }

    private void forwardRequest(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        // RequestDispatcher : 이전 요청의 파라미터 정보를 보존하여 요청
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(viewName);

        if (requestDispatcher == null) {
            response.sendRedirect(NOT_FOUND_VIEW);
            return;
        }
        requestDispatcher.forward(request, response);
    }
}
