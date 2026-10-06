package com.interface21.webmvc.servlet;

import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapping {

    /**
     * 요청을 받기 전에 매핑을 준비한다. 요청 처리 중에는 다시 초기화하지 않는다.
     */
    void initialize();

    /**
     * 요청 조건에 맞는 실행 대상을 반환한다.
     * 경로가 없거나 지원하지 않는 HTTP 메서드이면 null을 반환하며,
     * DispatcherServlet은 다음 매핑을 조회한다.
     */
    Object getHandler(HttpServletRequest request);
}
