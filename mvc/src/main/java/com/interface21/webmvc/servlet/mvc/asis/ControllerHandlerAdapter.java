package com.interface21.webmvc.servlet.mvc.asis;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.adapter.HandlerAdapter;
import com.interface21.webmvc.servlet.view.JspView;
import com.interface21.webmvc.servlet.view.RedirectView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ControllerHandlerAdapter implements HandlerAdapter {

    private static final String REDIRECT_PREFIX = "redirect:";

    @Override
    public boolean supports(final Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        final var controller = (Controller) handler;
        final String viewName = controller.execute(request, response);

        if (viewName.startsWith(REDIRECT_PREFIX)) {
            return new ModelAndView(new RedirectView(viewName.substring(REDIRECT_PREFIX.length())));
        }
        return new ModelAndView(new JspView(viewName));
    }
}
