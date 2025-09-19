package com.interface21.webmvc.servlet.view;

import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JspView : 스프링 초창기 프레임워크에서 등장했던 뷰 구현체. JSP 파일을 이용해서 화면을 그린다. JSP(JavaServerPage) : 자바 진영의 오래된 웹 뷰 기술. HTML 안에 자바 코드를
 * 삽입할 수 있는 서버 사이드 템플릿 언어.
 */
public class JspView implements View {

    private static final Logger log = LoggerFactory.getLogger(JspView.class);

    public static final String REDIRECT_PREFIX = "redirect:";

    private final String viewName;

    public JspView(final String viewName) {
        this.viewName = viewName;
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {

        if (viewName.startsWith(JspView.REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(JspView.REDIRECT_PREFIX.length()));
            return;
        }

        /**
         *  model - request attribute 바인딩
         *  JSP의 동적 응답을 위해 필요.
         *  ${id}같은 동적 표현이 동작하려면 request.setAttribute("id", ...)가 되어 있어야 한다.
         *  원래 Controller가 동적 데이터가 담긴 ModelAndView 객체를 응답하고, modelAndView.getModel()로 Map<String, Object> 타입의 모델 객체를 받음.
         *  근데 지금은 컨트롤러들이 그냥 뷰 이름(String)만 응답하므로 모델 바인딩이 의미가 없다.
         *  컨트롤러가 ModelAndView 객체를 반환할 때를 지원하는 코드
         */
        bindRequestAttribute(model, request);

        // view를 직접 그리는 부분 : JSP Servlet 으로 forward 처리
        final var requestDispatcher = request.getRequestDispatcher(viewName);
        requestDispatcher.forward(request, response);
    }

    private void bindRequestAttribute(Map<String, ?> model, HttpServletRequest request) {
        model.keySet().forEach(key -> {
            log.debug("attribute name : {}, value : {}", key, model.get(key));
            request.setAttribute(key, model.get(key));
        });
    }
}
