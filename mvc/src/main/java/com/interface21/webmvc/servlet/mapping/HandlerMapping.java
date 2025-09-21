package com.interface21.webmvc.servlet.mapping;

import com.interface21.webmvc.servlet.Handler;
import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapping {

    Handler getHandler(HttpServletRequest request);
}
