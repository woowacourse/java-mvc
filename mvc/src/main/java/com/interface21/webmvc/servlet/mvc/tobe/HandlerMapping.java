package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Set;

public interface HandlerMapping {
    void initialize();
    Object getHandler(HttpServletRequest request);
    Set<RequestMethod> getAllowedMethods(String path);
}
