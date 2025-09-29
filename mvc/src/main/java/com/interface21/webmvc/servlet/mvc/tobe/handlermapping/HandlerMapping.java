package com.interface21.webmvc.servlet.mvc.tobe.handlermapping;

import jakarta.servlet.http.HttpServletRequest;

/**
 * app 모듈에 존재하면 안된다.
 * 프레임워크/라이브러리가 프로덕션 코드에 의존할 수는 없다.
 */
public interface HandlerMapping {

    void initialize();

    Object getHandler(HttpServletRequest request);
}
