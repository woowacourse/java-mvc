package com.interface21.webmvc.servlet.view;

public class ViewName {

    private final String viewName;

    public ViewName(final String viewName) {
        this.viewName = viewName;
    }

    public boolean redirectable() {
        return viewName.startsWith(JspView.REDIRECT_PREFIX);
    }

    public String path() {
        if (redirectable()) {
            return viewName.substring(JspView.REDIRECT_PREFIX.length());
        }
        return viewName;
    }
}
