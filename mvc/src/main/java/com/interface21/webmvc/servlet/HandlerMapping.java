package com.interface21.webmvc.servlet;

import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapping {

    /**
     * 요청을 처리할 핸들러를 반환한다. 처리할 수 없는 요청이면 예외 대신 null을 반환해 다음 HandlerMapping이 찾도록 한다.
     */
    Object getHandler(HttpServletRequest request);
}
