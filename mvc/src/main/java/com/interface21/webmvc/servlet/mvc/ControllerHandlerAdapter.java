package com.interface21.webmvc.servlet.mvc;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.view.JspView;
import com.interface21.webmvc.servlet.view.RedirectView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ControllerHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(final Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(final HttpServletRequest request,
                               final HttpServletResponse response,
                               final Object handler) throws Exception {
        final Controller controller = (Controller) handler;
        final String viewName = controller.execute(request, response);
        if (viewName == null) {
            throw new IllegalStateException("Controller returned null view name");
        }

        if (viewName.startsWith(JspView.REDIRECT_PREFIX)) {
            final String redirectUrl = viewName.substring(JspView.REDIRECT_PREFIX.length());
            return new ModelAndView(new RedirectView(redirectUrl));
        }

        return new ModelAndView(new JspView(viewName));
    }
}
