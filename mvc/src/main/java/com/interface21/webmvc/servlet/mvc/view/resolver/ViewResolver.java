package com.interface21.webmvc.servlet.mvc.view.resolver;

import com.interface21.webmvc.servlet.mvc.view.JspView;
import com.interface21.webmvc.servlet.mvc.view.View;

public class ViewResolver {

    public View getView(final String viewName) {
        return new JspView(viewName);
    }
}
