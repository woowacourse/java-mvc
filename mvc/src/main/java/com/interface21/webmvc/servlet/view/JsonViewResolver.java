package com.interface21.webmvc.servlet.view;

import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.ViewResolver;

public class JsonViewResolver implements ViewResolver {

    private static final String PREFIX = "json:";

    @Override
    public View resolveViewName(String viewName) {
        if (viewName == null) {
            throw new NullPointerException("viewName의 값이 null입니다.");
        }

        if (viewName.startsWith(PREFIX)) {
            return new JsonView();
        }
        return null;
    }
}
