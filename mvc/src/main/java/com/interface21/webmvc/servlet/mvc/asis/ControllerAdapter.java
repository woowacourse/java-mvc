package com.interface21.webmvc.servlet.mvc.asis;

import com.interface21.webmvc.servlet.HandlerAdapter;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ControllerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(final Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(
            final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        if (!(handler instanceof Controller controller)) {
            throw new IllegalArgumentException("Controller 타입의 실행 대상만 처리할 수 있습니다.");
        }
        final var viewName = controller.execute(request, response);
        return new ModelAndView(new JspView(viewName));
    }
}
