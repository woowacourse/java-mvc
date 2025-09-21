package com.techcourse.view;

import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.view.JspView;
import com.interface21.webmvc.servlet.view.RedirectView;

public class ViewResolver {

    private static final String REDIRECT_PREFIX = "redirect:";

    public View resolveViewName(final String viewName) {
        if (viewName.startsWith(REDIRECT_PREFIX)) {
            return new RedirectView(viewName.substring(REDIRECT_PREFIX.length()));
        }
        return new JspView(viewName);
    }
}
