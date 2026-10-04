package com.interface21.webmvc.servlet.view;

import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JspView implements View {

    private static final Logger log = LoggerFactory.getLogger(JspView.class);
    public static final String REDIRECT_PREFIX = "redirect:";

    private final String viewName;

    public JspView(final String viewName) {
        this.viewName = viewName;
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        model.keySet().forEach(key -> {
            log.debug("attribute name : {}, value : {}", key, model.get(key));
            // 모델 데이터를 JSP가 읽을 수 있도록 전달
            request.setAttribute(key, model.get(key));
        });
        if (viewName.startsWith(REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(REDIRECT_PREFIX.length()));
            return;
        }
        // JSP를 사용해 화면을 만듦
        request.getRequestDispatcher(viewName).forward(request, response);
    }
}
