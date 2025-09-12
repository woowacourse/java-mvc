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

    public JspView(final String viewName) {
        this.viewName = viewName;
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {

        // redirect 먼저 확인
        if (viewName.startsWith(REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(REDIRECT_PREFIX.length()));
            return;
        }

        // model의 key: 뷰에서 사용할 변수명 (ex. "user", "places")
        // model의 value: 뷰에서 사용할 값 (ex. User 객체, List<Place> 객체)
        // model로부터 request attribute 세팅
        model.keySet().forEach(key -> {
            log.debug("attribute name : {}, value : {}", key, model.get(key));
            request.setAttribute(key, model.get(key));
        });

        // JSP forward 흐름
        // 1. 사용자가 /users 요청
        // 2. DispatcherServlet -> HandlerMapping -> HandlerAdapter -> Controller 조회
        // 3. Controller 실행(jsp 파일 경로 반환)
        // 4. DispatcherServlet -> ViewResolver -> View 생성
        final var requestDispatcher = request.getRequestDispatcher(viewName);
        requestDispatcher.forward(request, response);
    }
}
