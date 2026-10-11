package com.interface21.webmvc.servlet.mvc;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 레거시 Controller 인터페이스 구현체(ManualHandlerMapping 이 반환)를 실행하는 어댑터.
 */
public class ManualHandlerAdapter implements HandlerAdapter {

    /**
     * Controller 구현체일 때만 처리한다.
     */
    @Override
    public boolean supports(final Object handler) {
        return handler instanceof Controller;
    }

    /**
     * Controller.execute() 가 반환하는 뷰 이름(String)을 JspView 로 감싸 ModelAndView 로 변환한다.
     */
    @Override
    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response,
                               final Object handler) throws Exception {
        final var viewName = ((Controller) handler).execute(request, response);
        return new ModelAndView(new JspView(viewName));
    }
}
