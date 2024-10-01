package com.interface21.webmvc.servlet.mvc.mapping;

import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;

public record HandlerKey(String url, RequestMethod requestMethod) {

    public static HandlerKey from(HttpServletRequest request) {
        return new HandlerKey(request.getRequestURI(), RequestMethod.from(request.getMethod()));
    }
}
