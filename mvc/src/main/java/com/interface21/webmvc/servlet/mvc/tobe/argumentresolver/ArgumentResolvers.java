package com.interface21.webmvc.servlet.mvc.tobe.argumentresolver;

import com.interface21.bean.container.BeanContainer;
import com.interface21.webmvc.servlet.mvc.tobe.ArgumentResolver;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;

public class ArgumentResolvers {

    private final List<ArgumentResolver> argumentResolvers;

    public ArgumentResolvers() {
        BeanContainer beanContainer = BeanContainer.getInstance();
        this.argumentResolvers = beanContainer.getSubTypeBeansOf(ArgumentResolver.class);
    }

    public Object[] handle(HttpServletRequest request, HttpServletResponse response, Parameter[] parameters) {
        return Arrays.stream(parameters)
                .map(parameter -> getArgument(request, response, parameter))
                .toArray();
    }

    private Object getArgument(HttpServletRequest request, HttpServletResponse response, Parameter parameter) {
        ArgumentResolver argumentResolver = findArgumentResolver(parameter);
        return argumentResolver.resolveArgument(request, response, parameter);
    }

    private ArgumentResolver findArgumentResolver(Parameter parameter) {
        return argumentResolvers.stream()
                .filter(argumentResolver -> argumentResolver.supports(parameter))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("변환 불가능한 타입입니다."));
    }
}
