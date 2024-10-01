package com.interface21.webmvc.servlet.view;

import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.ViewResolver;

public class JspViewResolver implements ViewResolver {

    private static final String SUFFIX = ".jsp";

    @Override
    public View resolveViewName(String viewName) {
        if (viewName.endsWith(SUFFIX)) {
            return new JspView(viewName);
        }
        return null;
    }
}
