package com.techcourse;

import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.mvc.ExceptionHandlerAdapter;

import java.io.IOException;
import java.util.NoSuchElementException;

public class ExceptionHandler extends ExceptionHandlerAdapter {

    @Override
    protected void handleException(HttpServletResponse response, Exception exception) throws IOException {
        // TODO 401 예외 처리 구현 후 매핑
        if (exception instanceof NoSuchElementException) {
            response.sendRedirect("/404.jsp");
            return;
        }
        if (exception instanceof Exception) {
            response.sendRedirect("/500.jsp");
        }
    }
}
