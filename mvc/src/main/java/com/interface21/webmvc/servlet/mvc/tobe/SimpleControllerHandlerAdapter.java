package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.view.JspView;
import com.interface21.webmvc.servlet.view.RedirectView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SimpleControllerHandlerAdapter implements HandlerAdapter {

    private static final String REDIRECT_PREFIX = "redirect:";

    @Override
    public boolean supports(final Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(final HttpServletRequest req, final HttpServletResponse res, final Object handler)
            throws Exception {
        String viewName = ((Controller) handler).execute(req, res);
        if (viewName == null || viewName.isBlank()) {
            return null;
        }

        View view = resolveView(viewName);
        return new ModelAndView(view);
    }

    private View resolveView(final String viewName) {
        if (viewName == null || viewName.isBlank()) {
            throw new IllegalArgumentException("viewName must not be null or blank");
        }

        if (viewName.startsWith(REDIRECT_PREFIX)) {
            return new RedirectView(viewName.substring(REDIRECT_PREFIX.length())); // ex "redirect:/login"
        }
        return new JspView(viewName); // ex "stadiums/fan-rates"
    }
}
