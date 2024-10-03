package com.interface21.webmvc.servlet.view;

import java.util.Optional;
import com.interface21.webmvc.servlet.View;

public class JspViewResolver implements ViewResolver {

    @Override
    public Optional<View> resolveViewName(String viewName) {
        if (viewName.endsWith(".jsp")) {
            return Optional.of(new JspView(viewName));
        }
        return Optional.empty();
    }
}
