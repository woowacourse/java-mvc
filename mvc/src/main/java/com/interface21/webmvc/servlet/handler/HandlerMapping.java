package com.interface21.webmvc.servlet.handler;

import jakarta.servlet.http.HttpServletRequest;
import javax.annotation.Nullable;

public interface HandlerMapping {

    @Nullable
    Object getHandler(HttpServletRequest request);
}
