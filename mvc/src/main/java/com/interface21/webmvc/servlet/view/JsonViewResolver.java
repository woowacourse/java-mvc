package com.interface21.webmvc.servlet.view;

import com.interface21.webmvc.servlet.View;

import java.util.Optional;

public class JsonViewResolver implements ViewResolver {

    private static final String JSON_VIEW_NAME = "json";

    @Override
    public Optional<View> resolveViewName(final String viewName) {
        if (!JSON_VIEW_NAME.equals(viewName)) {
            return Optional.empty();
        }
        return Optional.of(new JsonView());
    }
}
