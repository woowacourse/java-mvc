package com.interface21.webmvc.servlet;

import com.interface21.web.bind.annotation.RequestMethod;

public interface HandlerMapping {
    void initialize();

    Object getHandler(Object request);

    boolean support(String requestUrl, RequestMethod method);
}
