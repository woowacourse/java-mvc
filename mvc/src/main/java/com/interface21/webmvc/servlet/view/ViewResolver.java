package com.interface21.webmvc.servlet.view;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ViewResolver {
    public static void checkRedirect(String viewName, HttpServletResponse response) throws IOException {
        if (viewName.startsWith(JspView.REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(JspView.REDIRECT_PREFIX.length()));
        }
    }
}
