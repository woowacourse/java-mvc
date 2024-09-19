package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class HandlerKeyGenerator {

    public static List<HandlerKey> fromAnnotatedMethod(Method method) {
        validateHasAnnotation(method);
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        String uri = requestMapping.value();
        RequestMethod[] requestMethods = requestMapping.method();
        return generate(uri,requestMethods);
    }

    private static void validateHasAnnotation(Method method) {
        if(!method.isAnnotationPresent(RequestMapping.class)) {
            throw new IllegalArgumentException("RequestMapping 어노테이션이 붙지 않은 메서드 입니다");
        }
    }

    private static List<HandlerKey> generate(String uri, RequestMethod[] requestMethods) {
        if(requestMethods.length == 0) {
            return generateFromAllRequestMethods(uri);
        }
        return generateFromRequestMethod(uri, requestMethods);
    }

    private static List<HandlerKey> generateFromAllRequestMethods(String uri) {
        return Arrays.stream(RequestMethod.values())
                .map(requestMethod -> new HandlerKey(uri, requestMethod))
                .toList();
    }

    private static List<HandlerKey> generateFromRequestMethod(String uri, RequestMethod[] requestMethods) {
        return Arrays.stream(requestMethods)
                .map(requestMethod -> new HandlerKey(uri,requestMethod))
                .toList();
    }
}
