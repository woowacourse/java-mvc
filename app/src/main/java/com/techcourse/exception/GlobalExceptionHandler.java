package com.techcourse.exception;

import com.interface21.webmvc.servlet.HandlerExceptionResolver;
import jakarta.servlet.http.HttpServletResponse;

public class GlobalExceptionHandler implements HandlerExceptionResolver {

    @Override
    public boolean resolve(Throwable exception, HttpServletResponse response) {
        if (exception instanceof UserException) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return true;
        }
        return false;
    }
}
