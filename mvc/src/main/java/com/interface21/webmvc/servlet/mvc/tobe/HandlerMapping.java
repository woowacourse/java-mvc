package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.Handler;
import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapping {

    void initialize();

    boolean hasHandler(HttpServletRequest request);

    Handler getHandler(HttpServletRequest request);
}
