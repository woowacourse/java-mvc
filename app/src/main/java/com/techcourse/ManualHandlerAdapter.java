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

    @Override
    public boolean supports(Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler
    ) {
        try {
            JspView view = new JspView(((Controller) handler).execute(request, response));
            return new ModelAndView(view);
        } catch (Exception e) {
            log.error("컨트롤러 실행 중 문제가 발생했습니다.: {}", e.getMessage(), e);
            return new ModelAndView(PAGE_500_VIEW);
        }
    }
}
