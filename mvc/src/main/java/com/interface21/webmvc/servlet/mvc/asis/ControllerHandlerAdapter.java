package com.interface21.webmvc.servlet.mvc.asis;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.HandlerAdapter;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 기존 Controller 인터페이스 기반 핸들러를 처리하는 어댑터
 */
public class ControllerHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Controller controller = (Controller) handler;
        String viewName = controller.execute(request, response);

        return new ModelAndView(new JspView(viewName));
    }
}