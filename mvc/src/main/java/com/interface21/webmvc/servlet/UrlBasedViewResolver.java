package com.interface21.webmvc.servlet;

import com.interface21.webmvc.servlet.view.JspView;
import com.interface21.webmvc.servlet.view.RedirectView;

public class UrlBasedViewResolver {

    private static final String REDIRECT_PREFIX = "redirect:";

    public View resolveViewName(String viewName) {
        if (viewName.startsWith(REDIRECT_PREFIX)) {
            return new RedirectView(viewName.substring(REDIRECT_PREFIX.length()));
        }

        return new JspView(viewName);
    }
}
