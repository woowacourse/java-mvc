package com.interface21.webmvc.servlet.view.resolver;

import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.view.JspView;

public class JspViewResolver implements ViewResolver {

    @Override
    public View resolveViewName(final String viewName) {
        if (viewName.startsWith("redirect:") || viewName.endsWith(".jsp")) {
            return new JspView(viewName);
        }
        return null;
    }
}
