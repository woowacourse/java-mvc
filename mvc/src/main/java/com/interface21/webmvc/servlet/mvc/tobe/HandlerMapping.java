package com.interface21.webmvc.servlet.mvc.tobe;

import java.util.Optional;
import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapping {

    Optional<Object> getHandler(HttpServletRequest request);
}
