package com.interface21.webmvc.servlet.view;

import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// JspView 클래스는 MVC 패턴에서 뷰 렌더링을 담당하는 클래스
public class JspView implements View {

    private static final Logger log = LoggerFactory.getLogger(JspView.class);

    public static final String REDIRECT_PREFIX = "redirect:";

    private final String viewName; // viewName은 컨트롤러의 처리 결과로, 어떤 뷰를 렌더링할지 알려줌

    public JspView(final String viewName) {
        this.viewName = viewName;
    }

    // 컨트롤러에서 반환한 viewName을 기반으로 응답을 생성, 뷰의 종류에 따라 포워드 또는 리다이렉트를 수행
    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        if (viewName.startsWith(REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(REDIRECT_PREFIX.length()));
            return;
        }

        // viewName을 JSP 페이지의 경로로 간주하고 해당 페이지로 요청을 포워드해 뷰를 렌더링
        model.keySet().forEach(key -> {
            log.debug("attribute name : {}, value : {}", key, model.get(key));
            request.setAttribute(key, model.get(key));
        });

        final var requestDispatcher = request.getRequestDispatcher(viewName);
        requestDispatcher.forward(request, response);
        // RequestDispatcher는 서블릿에서 다른 자원으로 요청을 전달하거나 포함할 때 사용하는 인터페이스
    }
}
