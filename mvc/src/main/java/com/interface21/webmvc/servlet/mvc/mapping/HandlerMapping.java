package com.interface21.webmvc.servlet.mvc.mapping;

import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Set;

public interface HandlerMapping {
    Object getHandler(HttpServletRequest request);

    Set<RequestMethod> getAllowedMethods(String requestURI);
}
