package com.interface21.webmvc.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HandlerExecutor {

    public Object execute(final HandlerExecution handlerExecution, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return handlerExecution.handle(request, response);
    }
}
