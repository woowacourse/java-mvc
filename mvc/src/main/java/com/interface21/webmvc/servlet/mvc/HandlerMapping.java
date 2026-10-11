package com.interface21.webmvc.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;
// 누가 처리할지 찾는다
public interface HandlerMapping {

    // 현재 요청을 처리할 Handler를 찾아 반환한다.
    Object getHandler(HttpServletRequest request);
}