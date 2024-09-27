package com.interface21.webmvc.servlet.mvc.tobe.argumentresolver;

import com.interface21.webmvc.servlet.mvc.tobe.ArgumentResolver;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Parameter;

public class DefaultRequestArgumentResolver implements ArgumentResolver {

    @Override
    public boolean supports(Parameter parameter) {
        return parameter.getType().equals(HttpServletRequest.class);
    }

    @Override
    public Object resolveArgument(HttpServletRequest request, HttpServletResponse response, Parameter parameter) {
        return request;
    }
}
