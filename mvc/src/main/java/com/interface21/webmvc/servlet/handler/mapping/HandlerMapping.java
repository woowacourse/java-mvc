package com.interface21.webmvc.servlet.handler.mapping;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;

public interface HandlerMapping {

    void initialize();
    Optional<Object> getHandler(HttpServletRequest httpServletRequest);
}
