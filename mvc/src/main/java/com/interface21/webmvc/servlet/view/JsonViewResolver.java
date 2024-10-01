package com.interface21.webmvc.servlet.view;

import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.ViewResolver;

public class JsonViewResolver implements ViewResolver {

    private static final String VIEW_NAME = "jsonView";

    @Override
    public View resolveViewName(String viewName) {
        if (VIEW_NAME.equals(viewName)) {
            return new JsonView();
        }
        return null;
    }
}
