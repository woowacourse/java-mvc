package com.interface21.webmvc.servlet;

import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapping {
    void initialize();

    Object getHandler(HttpServletRequest request);

    boolean support(String requestUrl, RequestMethod method);
}
