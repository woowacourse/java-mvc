package com.techcourse.air.mvc.core.resolver;

import com.techcourse.air.mvc.core.view.View;

public interface ViewResolver {
    View resolveViewName(String viewName);
}
