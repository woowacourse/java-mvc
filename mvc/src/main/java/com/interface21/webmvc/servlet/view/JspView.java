package com.interface21.webmvc.servlet.view;

import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

// JSP: HTML 문서 내에 자바 코드를 삽입 -> 동적 컨텐츠 생성 가능
public class JspView implements View {

    private static final Logger log = LoggerFactory.getLogger(JspView.class);

    public static final String REDIRECT_PREFIX = "redirect:";

    private final String viewName;

    public JspView(final String viewName) {
        this.viewName = viewName;
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        // viewName이 "redirect:"로 시작하면 해당 경로로 리다이렉트 응답
        if (viewName.startsWith(JspView.REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(JspView.REDIRECT_PREFIX.length()));
            return;
        }

        // JSP 렌더링 시 model map의 모든 속성들을 request의 attribute로 설정
        model.keySet().forEach(key -> {
            log.debug("attribute name : {}, value : {}", key, model.get(key));
            request.setAttribute(key, model.get(key));
        });

        // requestDispatcher를 이용하여
        final var requestDispatcher = request.getRequestDispatcher(viewName);
        // 서버 내부에서 요청 처리를 다른 자원으로(viewName으로) 넘긴다.(forward)
        requestDispatcher.forward(request, response);
    }
}
