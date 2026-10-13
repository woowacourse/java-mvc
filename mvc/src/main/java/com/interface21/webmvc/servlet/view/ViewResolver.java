package com.interface21.webmvc.servlet.view;

import com.interface21.webmvc.servlet.View;

import java.util.Optional;

public interface ViewResolver {

    Optional<View> resolveViewName(String viewName);
}
