package com.interface21.webmvc.servlet.view;

import com.interface21.webmvc.servlet.View;
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
    public void render(
            final Map<String, ?> model,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws Exception {
        // redirect면 새 주소로 이동시킨다.
        if (viewName.startsWith(REDIRECT_PREFIX)) {
            final String redirectUrl = viewName.substring(REDIRECT_PREFIX.length());

            response.sendRedirect(redirectUrl);
            return;
        }

        // JSP가 사용할 모델 데이터를 request에 넣는다.
        for (final var entry : model.entrySet()) {
            log.debug( "attribute name : {}, value : {}", entry.getKey(), entry.getValue());

            request.setAttribute(entry.getKey(), entry.getValue());
        }

        // 지정된 JSP에 현재 요청과 응답을 전달한다.
        final var requestDispatcher = request.getRequestDispatcher(viewName);
        requestDispatcher.forward(request, response);
    }
}
