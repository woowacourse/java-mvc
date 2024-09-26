package com.interface21.webmvc.servlet;

public abstract class AbstractView implements View {

    protected final String viewName;

    public AbstractView(String viewName) {
        this.viewName = viewName;
    }
}
