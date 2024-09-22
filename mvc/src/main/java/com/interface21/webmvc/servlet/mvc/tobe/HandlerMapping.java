package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Register;
import jakarta.servlet.http.HttpServletRequest;

@Register
public interface HandlerMapping {

    void initialize();

    Object getHandler(HttpServletRequest request);
}
