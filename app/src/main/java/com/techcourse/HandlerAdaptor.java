package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.view.type.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HandlerAdaptor {
    private static final String EXTENSION_SEPARATOR = ".";
    private static final String JSP_EXTENSION = ".jsp";

    public ModelAndView adaptHandler(Object handler, HttpServletRequest req, HttpServletResponse resp) {
        if (handler instanceof Controller) {
            String viewName = ((Controller) handler).execute(req, resp);
            return new ModelAndView(getView(viewName));
        }
        else if (handler instanceof HandlerExecution) {
            return ((HandlerExecution) handler).handle(req, resp);
        } else {
            throw new IllegalArgumentException("Fail to Handler Adapting");
        }
    }

    private View getView(String viewName) {
        int extensionSeparatorIndex = viewName.lastIndexOf(EXTENSION_SEPARATOR);
        String extension = viewName.substring(extensionSeparatorIndex);

        if(extension.equals(JSP_EXTENSION)) {
            return new JspView(viewName);
        }
        throw new IllegalArgumentException("매핑 불가능한 View 확장자(%s) 입니다.".formatted(extension));
    }
}
