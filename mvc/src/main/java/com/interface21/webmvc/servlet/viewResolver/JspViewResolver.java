package com.interface21.webmvc.servlet.viewResolver;

import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.ViewResolver;
import com.interface21.webmvc.servlet.view.JspView;

public class JspViewResolver implements ViewResolver {

    @Override
    public boolean canResolve(String viewName) {
        return viewName.endsWith(".jsp");
    }

    @Override
    public View resolve(String viewName) {
        return new JspView(viewName);
    }
}
