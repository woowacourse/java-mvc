package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Stream;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;

public class HandlerKeyCreator {

    private final Method method;

    public HandlerKeyCreator(final Method method) {
        this.method = method;
    }

    public List<HandlerKey> create() {
        if (!method.isAnnotationPresent(RequestMapping.class)) {
            throw new IllegalArgumentException("매핑 정보가 없는 메서드 값");
        }
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        final RequestMethod[] requestMethods = requestMapping.method();
        final String url = requestMapping.value();
        return createRequestMethod(requestMethods, url);
    }

    private List<HandlerKey> createRequestMethod(final RequestMethod[] requestMethods, final String url) {
        if (requestMethods.length == 0) {
            return getAllRequestMethod(url);
        }
        return getSpecificRequestMethod(requestMethods, url);
    }

    private List<HandlerKey> getSpecificRequestMethod(final RequestMethod[] requestMethods, final String url) {
        return Stream.of(requestMethods)
                .map(requestMethod -> new HandlerKey(url, requestMethod))
                .toList();
    }

    private List<HandlerKey> getAllRequestMethod(final String url) {
        return getSpecificRequestMethod(RequestMethod.values(), url);
    }
}
