package com.interface21.webmvc.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapping {

    /**
     * 요청을 처리할 수 있는 핸들러를 반환한다.
     * @param request HTTP 요청
     * @return 핸들러 객체 (Controller 또는 HandlerExecution)
     */
    Object getHandler(HttpServletRequest request);

    /**
     * 핸들러 매핑을 초기화한다.
     */
    void initialize();
}
