package com.interface21.webmvc.servlet.view;

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
    public void render(final Map<String, ?> model,
                       final HttpServletRequest request,
                       final HttpServletResponse response) throws Exception {
        model.keySet().forEach(key -> {
            log.debug("attribute name : {}, value : {}", key, model.get(key));
            request.setAttribute(key, model.get(key));
        });

        /**
         * redirect or forward
         *
         * redirect → 클라이언트에 302 응답 → 브라우저가 새 요청을 보냄 → URL 바뀜.
         * forward → 서버 내부에서 JSP로 제어를 넘김 → URL 그대로 유지.
         */
        if (viewName.startsWith(REDIRECT_PREFIX)) {
            String redirectPath = viewName.substring(REDIRECT_PREFIX.length());
            response.sendRedirect(redirectPath);
            return;
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewName);
        dispatcher.forward(request, response);
    }
}
