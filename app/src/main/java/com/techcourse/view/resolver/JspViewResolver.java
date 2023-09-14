package com.techcourse.view.resolver;

import webmvc.org.springframework.web.servlet.View;
import webmvc.org.springframework.web.servlet.view.JspView;
import webmvc.org.springframework.web.servlet.view.resolver.ViewResolver;

public class JspViewResolver implements ViewResolver {

    @Override
    public boolean supports(final String viewName) {
        return viewName.endsWith(JspView.JSP_SUFFIX);
    }

    @Override
    public View resolve(final String viewName) {
        return new JspView(viewName);
    }
}
