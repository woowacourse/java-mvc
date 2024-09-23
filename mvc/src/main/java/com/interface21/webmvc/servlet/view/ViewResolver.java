package com.interface21.webmvc.servlet.view;

import com.interface21.webmvc.servlet.View;

public interface ViewResolver {

    View resolveViewName(String viewName);
}
