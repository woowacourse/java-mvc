package com.interface21.webmvc.servlet.mvc.tobe.keyMaker;

import java.lang.reflect.Method;
import java.util.List;

public class HandlerKeyMakerMapping {

    private final List<KeyMaker> keyMakers;

    public HandlerKeyMakerMapping() {
        this.keyMakers = List.of(new RequestMappingKeyMaker());
    }

    public KeyMaker match(Method method) {
        return keyMakers.stream()
                .filter(keyMaker -> keyMaker.hasAnnotation(method))
                .findAny()
                .orElseThrow(() -> new IllegalStateException("해당하는 Method에 매핑할 수 있는 어노테이션이 존재하지 않습니다."));
    }
}
