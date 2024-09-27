package com.interface21.webmvc.servlet.mvc.tobe.argumentresolver;

import com.interface21.webmvc.servlet.mvc.tobe.ArgumentResolver;
import com.interface21.webmvc.servlet.mvc.tobe.MethodParameter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class DefaultRequestArgumentResolver implements ArgumentResolver {

    @Override
    public boolean supports(MethodParameter methodParameter) {
        return methodParameter.getType().equals(HttpServletRequest.class);
    }

    @Override
    public Object resolveArgument(HttpServletRequest request, HttpServletResponse response, MethodParameter methodParameter) {
        return request;
    }
}
