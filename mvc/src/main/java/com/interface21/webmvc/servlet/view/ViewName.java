package com.interface21.webmvc.servlet.view;

public class ViewName {

    private static final String REDIRECT_PREFIX = "redirect:";

    private final String viewName;

    public ViewName(String viewName) {
        this.viewName = stripRedirectPrefix(viewName);
    }

    private String stripRedirectPrefix(String viewName) {
        if (isRedirect(viewName)) {
            return viewName.substring(REDIRECT_PREFIX.length());
        }
        return viewName;
    }

    private boolean isRedirect(String viewName) {
        return viewName.startsWith(REDIRECT_PREFIX);
    }

    public String getViewName() {
        return viewName;
    }
}
