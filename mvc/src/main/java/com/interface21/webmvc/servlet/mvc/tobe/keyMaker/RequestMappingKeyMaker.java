package com.interface21.webmvc.servlet.mvc.tobe.keyMaker;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerKey;

import java.lang.reflect.Method;
import java.util.Arrays;

public class RequestMappingKeyMaker implements KeyMaker {

    public static final int EMPTY_METHOD_LENGTH = 0;

    private RequestMapping requestMapping;

    @Override
    public boolean hasAnnotation(Method method) {
        requestMapping = method.getAnnotation(RequestMapping.class);
        return requestMapping != null;
    }

    @Override
    public HandlerKey[] makeKeys(Method method) {
        RequestMethod[] requestMethods = findRequestMethods(requestMapping);

        return Arrays.stream(requestMethods)
                .map(requestMethod -> new HandlerKey(requestMapping.value(), requestMethod))
                .toArray(HandlerKey[]::new);
    }

    private RequestMethod[] findRequestMethods(RequestMapping requestMapping) {
        RequestMethod[] requestMethods = requestMapping.method();
        if (requestMethods.length == EMPTY_METHOD_LENGTH) {
            return RequestMethod.values();
        }
        return requestMethods;
    }
}
