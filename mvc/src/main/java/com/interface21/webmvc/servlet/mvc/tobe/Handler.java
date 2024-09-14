package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Handler {

    private final HandlerKey key;
    private final HandlerExecution execution;

    public Handler(HandlerKey key, HandlerExecution execution) {
        this.key = key;
        this.execution = execution;
    }

    public static List<Handler> createHandlers(Method method, Object controller) {
        HandlerExecution handlerExecution = new HandlerExecution(controller, method);
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        if (requestMapping == null) {
            return List.of();
        }
        String url = requestMapping.value();

        return Arrays.stream(requestMapping.method())
                .map(requestMethod -> new HandlerKey(url, requestMethod))
                .map(handlerKey -> new Handler(handlerKey, handlerExecution))
                .toList();
    }

    public HandlerKey getKey() {
        return key;
    }

    public HandlerExecution getExecution() {
        return execution;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Handler handler = (Handler) o;
        return Objects.equals(key, handler.key) && Objects.equals(execution, handler.execution);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, execution);
    }
}
