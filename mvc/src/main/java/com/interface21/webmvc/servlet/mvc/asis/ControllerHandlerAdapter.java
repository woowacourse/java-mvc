package com.interface21.webmvc.servlet.mvc.asis;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.HandlerAdapter;
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
    public ModelAndView handle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler
    ) throws Exception {
        final var controller = (Controller) handler;
        final var viewName = controller.execute(request, response);

        return resolveView(viewName);
    }

    // TODO. 추후 ViewResolver 도입 고려
    private ModelAndView resolveView(final String viewName) {
        if (viewName.startsWith(RedirectView.REDIRECT_PREFIX)) {
            return resolveRedirectView(viewName);
        }

        if (viewName.endsWith(JspView.JSP_POSTFIX)) {
            return resolveJspView(viewName);
        }

        throw new IllegalArgumentException("Unsupported view type: " + viewName);
    }

    private ModelAndView resolveRedirectView(final String viewName) {
        final var redirectUrl = viewName.substring(RedirectView.REDIRECT_PREFIX.length());

        return new ModelAndView(new RedirectView(redirectUrl));
    }

    private ModelAndView resolveJspView(final String viewName) {
        return new ModelAndView(new JspView(viewName));
    }
}
