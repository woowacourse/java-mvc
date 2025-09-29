package com.interface21.webmvc.servlet.mvc.tobe.handleradapter;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.view.JspView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Controller의 구현체인 핸들러를 실행하기 위한 어댑터
 */
public class ControllerAdapter implements HandlerAdapter{

    @Override
    public boolean supports(Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(Object handler, HttpServletRequest request, HttpServletResponse response) {
        try {
            String viewName = ((Controller)handler).execute(request, response);
            return new ModelAndView(new JspView(viewName));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
