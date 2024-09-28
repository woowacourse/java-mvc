package com.techcourse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.adapter.HandlerAdapter;
import com.interface21.webmvc.servlet.view.JspView;

public class ManualHandlerAdapter implements HandlerAdapter {

    private static final Logger log = LoggerFactory.getLogger(ManualHandlerAdapter.class);
    private static final View PAGE_500_VIEW = new JspView("redirect:/500.jsp");

    private Controller controller;

    @Override
    public boolean supports(Object handler) {
        if (handler instanceof Controller) {
            this.controller = (Controller) handler;
            return true;
        }
        return false;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) {
        try {
            return new ModelAndView(new JspView(controller.execute(request, response)));
        } catch (Exception e) {
            log.error("컨트롤러 실행 중 문제가 발생했습니다.: {}", e.getMessage(), e);
            return new ModelAndView(PAGE_500_VIEW);
        }
    }
}
