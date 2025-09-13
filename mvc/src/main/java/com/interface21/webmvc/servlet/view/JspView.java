package com.interface21.webmvc.servlet.view;

import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * MVC 패턴에서 View의 역할을 수행
 * 컨트롤러가 처리한 결과 데이터(Model)를 받아, 사용자에게 보여줄 JSP 페이지 생성
 */
public class JspView implements View {

    private static final Logger log = LoggerFactory.getLogger(JspView.class);

    public static final String REDIRECT_PREFIX = "redirect:";

    private final String viewName;

    public JspView(final String viewName) {
        this.viewName = viewName;
    }

    /**
     * 두 가지 방식으로 뷰를 처리
     * 1. Forward: Model 데이터를 request에 담아 지정된 JSP 파일로 넘김
     * 2. Redirect: 클라이언트에게 지정된 URL로 다시 요청하라고 명령
     */
    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        // redirect가 있으면 그 경로로 리다이렉트 처리 (ex. redirect:/login)
        if (viewName.startsWith(JspView.REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(JspView.REDIRECT_PREFIX.length()));
            return;
        }

        // model의 키를 순회
        // 그 key로 model에서 값을 가져와 request attribute에 저장
        model.keySet().forEach(key -> {
            log.debug("attribute name : {}, value : {}", key, model.get(key));
            request.setAttribute(key, model.get(key));
        });

        // viewName으로 넘겨줄 JSP 파일에 대한 RequestDispatcher를 얻음
        // forward를 호출하여 현재 요청과 응답을 그대로 JSP에 전달
        final var requestDispatcher = request.getRequestDispatcher(viewName);
        requestDispatcher.forward(request, response);
    }
}
