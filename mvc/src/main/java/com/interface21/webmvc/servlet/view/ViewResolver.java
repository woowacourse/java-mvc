package com.interface21.webmvc.servlet.view;

import com.interface21.webmvc.servlet.View;

public class ViewResolver {

    public View resolveByStatusCode(int statusCode) {
        String viewName = ViewMapper.findViewNameWithStatusCode(statusCode);
        return new JspView(viewName);
    }
}
