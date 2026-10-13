package com.interface21.webmvc.servlet.view;

import com.interface21.webmvc.servlet.View;

import java.util.Optional;

public class JspViewResolver implements ViewResolver {

    @Override
    public Optional<View> resolveViewName(final String viewName) {
        if (!viewName.endsWith(".jsp") && !viewName.startsWith(JspView.REDIRECT_PREFIX)) {
            return Optional.empty();
        }
        return Optional.of(new JspView(viewName));
    }
}
