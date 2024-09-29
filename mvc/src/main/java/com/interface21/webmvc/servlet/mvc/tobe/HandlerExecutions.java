package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class HandlerExecutions {

    Map<HandlerKey, HandlerExecution> executions;

    public HandlerExecutions() {
        this.executions = new HashMap<>();
    }

    public void addHandlerExecution(Object executionTarget, Method method) {
        List<HandlerKey> handlerKeys = constructHandlerKey(method);
        validateDuplicateKeys(handlerKeys);
        HandlerExecution handlerExecution = constructHandlerExecution(executionTarget, method);
        handlerKeys.forEach(handlerKey -> executions.put(handlerKey, handlerExecution));
    }

    private void validateDuplicateKeys(List<HandlerKey> handlerKeys) {
        for (HandlerKey key : handlerKeys) {
            validateDuplicateKey(key);
        }
    }

    private void validateDuplicateKey(HandlerKey key) {
        if (executions.containsKey(key)) {
            throw new IllegalArgumentException("이미 등록된 HandlerKey 입니다. [%s]".formatted(key));
        }
    }

    private HandlerExecution constructHandlerExecution(Object executionTarget, Method targetMethod) {
        return new HandlerExecution(executionTarget, targetMethod);
    }

    private List<HandlerKey> constructHandlerKey(Method method) {
        return HandlerKeyGenerator.fromAnnotatedMethod(method);
    }

    public Optional<Object> get(HandlerKey key) {
        return Optional.ofNullable(executions.get(key));
    }
}
