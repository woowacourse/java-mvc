package com.interface21.webmvc.servlet.view;

import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.view.type.JspView;

public class MvcViewResolver implements ViewResolver {

    private static final String EXTENSION_SEPARATOR = ".";
    private static final String JSP_EXTENSION = ".jsp";

    @Override
    public View resolveViewName(String viewName) {
        int extensionSeparatorIndex = viewName.lastIndexOf(EXTENSION_SEPARATOR);
        String extension = viewName.substring(extensionSeparatorIndex);

        if(extension.equals(JSP_EXTENSION)) {
            return new JspView(viewName);
        }
        throw new IllegalArgumentException("매핑 불가능한 View 확장자(%s) 입니다.".formatted(extension));
    }
}
