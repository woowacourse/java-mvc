package com.interface21.web.support;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HttpServletRequestResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(Class<?> parameterType) {
        return parameterType == HttpServletRequest.class;
    }

    @Override
    public Object resolveArgument(HttpServletRequest request, HttpServletResponse response, Class<?> parameterType) {
        return request;
    }
}
