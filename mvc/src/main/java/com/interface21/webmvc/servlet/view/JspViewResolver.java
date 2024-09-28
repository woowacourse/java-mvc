package com.interface21.webmvc.servlet.view;

import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.ViewResolver;

public class JspViewResolver implements ViewResolver {

    @Override
    public View resolveViewName(String viewName) {
        return new JspView(viewName);
    }
}
