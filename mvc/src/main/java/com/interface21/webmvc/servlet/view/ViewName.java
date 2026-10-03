package com.interface21.webmvc.servlet.view;

import java.util.Objects;

public class ViewName {

    private final String viewName;

    public ViewName(final String viewName) {
        this.viewName = Objects.requireNonNull(viewName, "viewName 은 null 이면 안된다.");
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
