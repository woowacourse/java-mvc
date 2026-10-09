package com.interface21.webmvc.servlet;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;

public interface HandlerMapping {

    Optional<?> getHandler(HttpServletRequest request);
}
