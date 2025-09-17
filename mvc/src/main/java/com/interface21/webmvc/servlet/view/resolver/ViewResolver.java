package com.interface21.webmvc.servlet.view.resolver;

import com.interface21.webmvc.servlet.view.JspView;
import com.interface21.webmvc.servlet.view.View;

public class ViewResolver {

    public View getView(final String viewName) {
        return new JspView(viewName);
    }
}
