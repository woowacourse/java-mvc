package com.interface21.webmvc.servlet.mvc.tobe.argumentresolver;

import com.interface21.web.bind.annotation.RequestParam;
import com.interface21.webmvc.servlet.mvc.tobe.ArgumentResolver;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class RequestParamArgumentResolver implements ArgumentResolver {

    private static final Map<Class<?>, Function<String, ?>> converters = new HashMap<>();

    static {
        converters.put(String.class, Function.identity());
        converters.put(Integer.class, Integer::parseInt);
        converters.put(Long.class, Long::parseLong);
        converters.put(int.class, str -> str == null ? 0 : Integer.parseInt(str));
        converters.put(long.class, str -> str == null ? 0L : Long.parseLong(str));
    }

    @Override
    public boolean supports(Parameter parameter) {
        return parameter.isAnnotationPresent(RequestParam.class);
    }

    @Override
    public Object resolveArgument(HttpServletRequest request, HttpServletResponse response, Parameter parameter) {
        RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
        String argument = getArgument(request, requestParam);
        if (requestParam.required() && argument == null) {
            throw new IllegalArgumentException("존재하지 않는 파라미터입니다.");
        }
        Class<?> type = parameter.getType();
        return converters.get(type).apply(argument);
    }

    private String getArgument(HttpServletRequest request, RequestParam requestParam) {
        String name = requestParam.value();
        return request.getParameter(name);
    }
}
