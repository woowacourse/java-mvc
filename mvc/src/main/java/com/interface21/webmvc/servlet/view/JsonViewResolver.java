package com.interface21.webmvc.servlet.view;

import java.util.Optional;
import com.interface21.webmvc.servlet.View;

public class JsonViewResolver implements ViewResolver {

    @Override
    public Optional<View> resolveViewName(String viewName) {
        if (viewName.endsWith(".json")) {
            return Optional.of(new JsonView());
        }
        return Optional.empty();
    }
}
