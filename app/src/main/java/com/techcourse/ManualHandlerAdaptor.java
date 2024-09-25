package com.techcourse;

import com.interface21.webmvc.servlet.HandlerAdaptor;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.view.type.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ManualHandlerAdaptor implements HandlerAdaptor {
    private static final String EXTENSION_SEPARATOR = ".";
    private static final String JSP_EXTENSION = ".jsp";

    @Override
    public boolean supports(Object handler) {
        return (handler instanceof Controller);
    }

    @Override
    public ModelAndView handle(HttpServletRequest req, HttpServletResponse resp, Object handler) {
        if (supports(handler)) {
            String viewName = ((Controller) handler).execute(req, resp);
            return new ModelAndView(getView(viewName));
        }
        throw new IllegalArgumentException("Fail to Handler Adapting");
    }

    private View getView(String viewName) {
        int extensionSeparatorIndex = viewName.lastIndexOf(EXTENSION_SEPARATOR);
        String extension = viewName.substring(extensionSeparatorIndex);

        if (extension.equals(JSP_EXTENSION)) {
            return new JspView(viewName);
        }
        throw new IllegalArgumentException("매핑 불가능한 View 확장자(%s) 입니다.".formatted(extension));
    }
}
