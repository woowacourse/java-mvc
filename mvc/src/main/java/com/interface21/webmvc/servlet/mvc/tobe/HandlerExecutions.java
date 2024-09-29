package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.Constructor;
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

    public void addHandlerExecution(Class<?> controller, Method method) {
        List<HandlerKey> handlerKeys = constructHandlerKey(method);
        validateDuplicateKeys(handlerKeys);
        HandlerExecution handlerExecution = constructHandlerExecution(controller, method);
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

    private HandlerExecution constructHandlerExecution(Class<?> controller, Method targetMethod) {
        try {
            Constructor<?> firstConstructor = controller.getDeclaredConstructor();
            Object executionTarget = firstConstructor.newInstance();
            return new HandlerExecution(executionTarget, targetMethod);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(
                    "default constructor가 존재하지 않습니다 %s".formatted(controller.getCanonicalName()));
        } catch (Exception e) {
            throw new IllegalArgumentException("HandlerMapping을 초기화 하는데 실패했습니다.");
        }
    }

    private List<HandlerKey> constructHandlerKey(Method method) {
        return HandlerKeyGenerator.fromAnnotatedMethod(method);
    }

    public Optional<Object> get(HandlerKey key) {
        return Optional.ofNullable(executions.get(key));
    }
}
