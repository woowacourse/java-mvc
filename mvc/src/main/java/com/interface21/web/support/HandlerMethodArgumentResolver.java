package com.interface21.web.support;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/*
메서드 매개변수를 초기화 한다.
 */
public interface HandlerMethodArgumentResolver {

    boolean supportsParameter(Class<?> parameterType);

    Object resolveArgument(HttpServletRequest request, HttpServletResponse response, Class<?> parameterType);
}
