package com.techcourse.air.mvc.core.resolver;

import com.techcourse.air.mvc.core.view.JspView;
import com.techcourse.air.mvc.core.view.ResourceView;
import com.techcourse.air.mvc.core.view.View;

public class ResourceViewResolver implements ViewResolver {

    @Override
    public View resolveViewName(String viewName) {
        if (viewName.endsWith(".jsp")) {
            return new JspView(viewName);
        }
        return new ResourceView(viewName);
    }
}
