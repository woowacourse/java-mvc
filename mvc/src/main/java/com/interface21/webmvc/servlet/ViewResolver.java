package com.interface21.webmvc.servlet;

public interface ViewResolver {

    boolean canResolve(String viewName);

    View resolve(String viewName);
}
