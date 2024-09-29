package com.interface21.webmvc.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.view.JspView;

public class ControllerHandlerAdapter implements HandlerAdapter {

    private static final ControllerHandlerAdapter INSTANCE = new ControllerHandlerAdapter();

    private ControllerHandlerAdapter() {
    }

    public static ControllerHandlerAdapter getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean supports(Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(Object handler, HttpServletRequest request, HttpServletResponse response) {
        final Controller controller = (Controller) handler;
        final View view = new JspView(executeController(controller, request, response));
        return new ModelAndView(view);
    }

    private String executeController(Controller controller, HttpServletRequest request, HttpServletResponse response) {
        try {
            return controller.execute(request, response);
        } catch (Exception e) {
            throw new IllegalStateException("Controller 요청 처리 중 에러가 발생했습니다." + e);
        }
    }
}
