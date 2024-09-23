package com.interface21.webmvc.servlet.mvc.tobe.keygenerator;

import java.lang.reflect.Method;
import java.util.List;

public class HandlerKeyGeneratorMapping {

    private static final List<HandlerKeyGenerator> HANDLER_KEY_GENERATORS = List.of(new RequestMappingHandlerKeyGenerator());

    public HandlerKeyGeneratorMapping() {
    }

    public HandlerKeyGenerator match(Method method) {
        return HANDLER_KEY_GENERATORS.stream()
                .filter(handlerKeyGenerator -> handlerKeyGenerator.hasAnnotation(method))
                .findAny()
                .orElse(null);
    }
}
