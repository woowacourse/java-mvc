package com.techcourse.servlet.view;

import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.view.JspView;

public class ViewResolver {

    public View resolveByStatusCode(int statusCode){
        String viewName = ViewMapper.findViewNameWithStatusCode(statusCode);
        return new JspView(viewName);
    }
}
