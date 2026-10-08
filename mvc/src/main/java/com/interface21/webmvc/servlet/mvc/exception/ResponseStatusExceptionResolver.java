package com.interface21.webmvc.servlet.mvc.exception;

import com.interface21.web.server.ResponseStatusException;
import com.interface21.webmvc.servlet.HandlerExceptionResolver;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ResponseStatusExceptionResolver implements HandlerExceptionResolver {

    @Override
    public boolean resolveException(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler,
            final Exception exception
    ) throws Exception {
        if (exception instanceof ResponseStatusException responseStatusException) {
            response.sendError(responseStatusException.getStatus(), responseStatusException.getMessage());
            return true;
        }
        return false;
    }
}
