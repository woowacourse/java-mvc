package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HandlerExecutionExtractor {

    public List<HandlerRegistration> extract(final Map<Class<?>, Object> controllers) {
        final List<HandlerRegistration> registrations = new ArrayList<>();
        for (Map.Entry<Class<?>, Object> controller : controllers.entrySet()) {
            registrations.addAll(extractFrom(controller.getKey(), controller.getValue()));
        }
        return registrations;
    }

    private List<HandlerRegistration> extractFrom(final Class<?> controller, final Object instance) {
        final List<HandlerRegistration> registrations = new ArrayList<>();

        for (Method method : controller.getDeclaredMethods()) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                final HandlerExecution handlerExecution = new HandlerExecution(instance, method);
                final RequestMapping annotation = method.getAnnotation(RequestMapping.class);

                final RequestMappingInfo requestMappingInfo = new RequestMappingInfo(annotation.value(), annotation.method());
                for (HandlerKey handlerKey : requestMappingInfo.handlerKeys()) {
                    registrations.add(new HandlerRegistration(handlerKey, handlerExecution));
                }
            }
        }

        return registrations;
    }
}
