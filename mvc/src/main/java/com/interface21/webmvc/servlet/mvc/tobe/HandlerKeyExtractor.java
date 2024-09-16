package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HandlerKeyExtractor {

    public static List<HandlerKey> extract(Method method) {
        List<HandlerKey> handlerKeys = new ArrayList<>();
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        String value = requestMapping.value();
        RequestMethod[] requestMethods = requestMapping.method();
        if (requestMethods.length == 0) {
            requestMethods = RequestMethod.values();
        }
        Arrays.stream(requestMethods)
                .forEach(requestMethod -> handlerKeys.add(new HandlerKey(value, requestMethod)));
        return handlerKeys;
    }
}
