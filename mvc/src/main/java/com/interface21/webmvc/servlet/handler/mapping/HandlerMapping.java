package com.interface21.webmvc.servlet.handler.mapping;

import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapping {

    void initialize();

    // request를 처리할 수 있는 핸들러를 반환
    Object getHandler(HttpServletRequest request);
}
