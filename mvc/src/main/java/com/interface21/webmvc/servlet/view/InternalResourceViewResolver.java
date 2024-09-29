package com.interface21.webmvc.servlet.view;

public class InternalResourceViewResolver implements ViewResolver {
    private final String extension;

    public InternalResourceViewResolver(String extension) {
        this.extension = extension;
    }

    @Override
    public View resolveViewName(String viewName) {
        return new JspView(viewName + extension);
    }
}
