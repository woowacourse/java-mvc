package com.interface21.webmvc.servlet.mvc;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.view.JspView;import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class DefaultHandlerAdapter implements HandlerAdapter {

    @Override
    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        if (handler instanceof Controller) {
            final String viewName = ((Controller) handler).execute(request, response);
            final View view = new JspView(viewName);
            return new ModelAndView(view);
        } else if (handler instanceof HandlerExecution) {
            return ((HandlerExecution) handler).handle(request, response);
        } else {
            throw new IllegalArgumentException(handler + " 핸들러로 " + request.getRequestURI() + "요청을 처리할 수 없습니다.");
        }
    }
}
