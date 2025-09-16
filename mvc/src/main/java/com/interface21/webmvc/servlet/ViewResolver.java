package com.interface21.webmvc.servlet;

import com.interface21.webmvc.servlet.view.JspView;

public class ViewResolver {

    public View getView(final String viewName) {
        return new JspView(viewName);
    }
}
