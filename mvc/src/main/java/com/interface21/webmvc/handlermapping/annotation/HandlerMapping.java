package com.interface21.webmvc.handlermapping.annotation;

import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapping {

    Object getHandler(final HttpServletRequest request);
}
