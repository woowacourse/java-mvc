package com.interface21.webmvc.servlet.view;

import com.interface21.webmvc.servlet.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ViewResolverRegistry {

    private final List<ViewResolver> viewResolvers = new ArrayList<>();

    public void addViewResolver(final ViewResolver viewResolver) {
        viewResolvers.add(viewResolver);
    }

    public Optional<View> resolveViewName(final String viewName) {
        for (ViewResolver viewResolver : viewResolvers) {
            final Optional<View> view = viewResolver.resolveViewName(viewName);
            if (view.isPresent()) {
                return view;
            }
        }
        return Optional.empty();
    }
}
