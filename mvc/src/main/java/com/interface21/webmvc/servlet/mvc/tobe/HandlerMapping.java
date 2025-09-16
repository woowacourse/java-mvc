package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 누가 이 요청을 처리할 것인가
 * 요청 정보를 보고, 이 요청을 처리할 핸들러를 찾아주는 것
 */
public interface HandlerMapping {

    Object getHandler(HttpServletRequest request);
}
