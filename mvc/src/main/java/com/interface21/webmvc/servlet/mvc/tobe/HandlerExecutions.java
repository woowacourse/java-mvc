package com.interface21.webmvc.servlet.mvc.tobe;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public record HandlerExecutions(
        Map<HandlerKey, HandlerExecution> handlerExecutions
) {

    public static HandlerExecutions empty() {
        return new HandlerExecutions(new HashMap<>());
    }

    public void add(final HandlerKey handlerKey, final HandlerExecution handlerExecution) {
        // 중복 검사 후 삽입
        if (handlerExecutions.containsKey(handlerKey)) {
            throw new IllegalArgumentException("이미 존재하는 HandlerKey 입니다: " + handlerKey);
        }
        handlerExecutions.put(handlerKey, handlerExecution);
    }

    public void addAll(final Map<HandlerKey, HandlerExecution> handlerExecutions) {
        handlerExecutions.forEach(this::add);
    }

    public void addAll(final HandlerExecutions other) {
        addAll(other.handlerExecutions);
    }

    public Optional<HandlerExecution> get(final HandlerKey handlerKey) {
        return Optional.ofNullable(handlerExecutions.get(handlerKey));
    }
}
