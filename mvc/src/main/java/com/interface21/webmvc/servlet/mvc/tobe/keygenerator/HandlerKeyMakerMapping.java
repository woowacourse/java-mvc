package com.interface21.webmvc.servlet.mvc.tobe.keygenerator;

import java.lang.reflect.Method;
import java.util.List;

public class HandlerKeyMakerMapping {

    private static final List<KeyMaker> keyMakers = List.of(new RequestMappingKeyMaker());

    public HandlerKeyMakerMapping() {
    }

    public KeyMaker match(Method method) {
        return keyMakers.stream()
                .filter(keyMaker -> keyMaker.hasAnnotation(method))
                .findAny()
                .orElse(null);
    }
}
