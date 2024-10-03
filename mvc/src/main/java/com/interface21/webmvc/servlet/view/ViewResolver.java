package com.interface21.webmvc.servlet.view;

import java.util.Optional;
import com.interface21.webmvc.servlet.View;

public interface ViewResolver {

    Optional<View> resolveViewName(String viewName);
}
