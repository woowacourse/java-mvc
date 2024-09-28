package com.interface21.webmvc.servlet.view;

import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.ViewResolver;

public class JsonViewResolver implements ViewResolver {

    private static final String PREFIX = "json:";

    @Override
    public View resolveViewName(String viewName) {
        if (PREFIX.startsWith(viewName)) {
            return new JsonView();
        }
        return null;
    }
}
