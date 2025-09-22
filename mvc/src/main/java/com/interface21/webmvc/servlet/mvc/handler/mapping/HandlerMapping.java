package com.interface21.webmvc.servlet.mvc.handler.mapping;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;

public interface HandlerMapping {

    Optional<Object> getHandler(final HttpServletRequest request);
}
