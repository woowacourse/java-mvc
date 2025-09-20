package com.interface21.webmvc.servlet.mvc.handlermapping;

import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapping {

    Object getHandler(final HttpServletRequest request);
}
