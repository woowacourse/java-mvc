package com.interface21.webmvc.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapping {

    boolean hasHandler(HttpServletRequest request);

    Object getHandler(HttpServletRequest request);

}
