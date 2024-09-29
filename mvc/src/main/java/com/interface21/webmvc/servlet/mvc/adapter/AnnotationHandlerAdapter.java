package com.interface21.webmvc.servlet.mvc.adapter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.HandlerExecution;
import com.interface21.webmvc.servlet.view.JspView;

public class AnnotationHandlerAdapter implements HandlerAdapter {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerAdapter.class);
    private static final View PAGE_500_VIEW = new JspView("/500.jsp");

    @Override
    public boolean supports(Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public ModelAndView handle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler
    ) {
        try {
            return ((HandlerExecution) handler).handle(request, response);
        } catch (Exception e) {
            log.error("컨트롤러 실행 중 문제가 발생했습니다.: {}", e.getMessage(), e);
            return new ModelAndView(PAGE_500_VIEW);
        }
    }
}
