package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.view.JspView;
import com.interface21.webmvc.servlet.view.RedirectView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ControllerHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean canHandle(final Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler
    ) throws Exception {
        final Controller controller = (Controller) handler;
        final String viewName = controller.execute(request, response);

        if (viewName.startsWith(RedirectView.REDIRECT_PREFIX)) {
            final String redirectUrl = viewName.substring(RedirectView.REDIRECT_PREFIX.length());
            return new ModelAndView(new RedirectView(redirectUrl));
        }

        return new ModelAndView(new JspView(viewName));
    }
}
