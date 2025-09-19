package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapping {

    Object getHandler(HttpServletRequest request);

    void initialize();
}
