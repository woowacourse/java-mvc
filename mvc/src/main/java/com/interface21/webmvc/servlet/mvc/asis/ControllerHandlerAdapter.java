package com.interface21.webmvc.servlet.mvc.asis;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.HandlerAdapter;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerHandlerAdapter implements HandlerAdapter {

    private static final Logger log = LoggerFactory.getLogger(ControllerHandlerAdapter.class);

    @Override
    public boolean supports(final Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(final Object handler,
                               final HttpServletRequest request,
                               final HttpServletResponse response) {
        try {
            final var controller = (Controller) handler;
            final var viewName = controller.execute(request, response);
            log.debug("Controller Handler / View Name : {}", viewName);
            final var view = getView(viewName);
            return new ModelAndView(view);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // TODO: View 생성 로직을 ViewResolver로 분리 필요 = Step 3 [2025-09-18]
    private View getView(final String viewName) {
        return new JspView(viewName);
    }
}
