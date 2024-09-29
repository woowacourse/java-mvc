package com.interface21.webmvc.servlet.mvc.handler.mapping;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;

public class RequestMappingResolver {

    private final RequestMapping requestMapping;

    public RequestMappingResolver(RequestMapping requestMapping) {
        this.requestMapping = requestMapping;
    }

    public String getUrl() {
        String url = requestMapping.value();
        if (url.isBlank()) {
            throw new IllegalStateException("@RequestMapping의 value값이 지정되어 있지 않습니다.");
        }
        return url;
    }

    public Set<RequestMethod> getRequestMethods() {
        Set<RequestMethod> methods = Arrays.stream(requestMapping.method())
                .collect(Collectors.toSet());

        if (methods.isEmpty()) {
            Set<RequestMethod> allRequestMethods = Arrays.stream(RequestMethod.values()).collect(Collectors.toSet());
            return allRequestMethods;
        }
        return methods;
    }
}
