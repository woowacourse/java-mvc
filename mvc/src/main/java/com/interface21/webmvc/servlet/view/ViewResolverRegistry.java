package com.interface21.webmvc.servlet.view;

import java.util.ArrayList;
import java.util.List;
import com.interface21.webmvc.servlet.View;

import static com.interface21.webmvc.servlet.view.JspView.NOT_FOUND_VIEW;

public class ViewResolverRegistry {

    private final List<ViewResolver> viewResolvers = new ArrayList<>();

    public void addViewResolver(ViewResolver viewResolver) {
        viewResolvers.add(viewResolver);
    }

    public View resolveViewName(String viewName) {
        return viewResolvers.stream()
                .filter(viewResolver -> viewResolver.resolveViewName(viewName).isPresent())
                .map(viewResolver -> viewResolver.resolveViewName(viewName).get())
                .findFirst()
                .orElse(new JspView(NOT_FOUND_VIEW));
    }
}
