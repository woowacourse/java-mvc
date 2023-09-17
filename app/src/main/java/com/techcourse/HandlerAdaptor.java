package com.techcourse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface HandlerAdaptor {

    boolean isSame(final Object handler);

    String execute(final Object handler, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception;
}
