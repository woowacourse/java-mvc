package com.interface21.webmvc.servlet.view;

public class ViewName {

    public static final String REDIRECT_PREFIX = "redirect:";

    private final String value;

    public ViewName(final String viewName) {
        validateViewNameIsNullOrEmpty(viewName);
        this.value = viewName;
    }

    private void validateViewNameIsNullOrEmpty(final String viewName) {
        if (viewName == null || viewName.isBlank()) {
            throw new IllegalArgumentException("view name은 null 혹은 공백이 입력될 수 없습니다. - " + viewName);
        }
    }

    public boolean isRedirect() {
        return value.startsWith(REDIRECT_PREFIX);
    }

    public String getUri() {
        if (isRedirect()) {
            return value.substring(REDIRECT_PREFIX.length());
        }

        return value;
    }
}
