package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.webmvc.servlet.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.List;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final HandlerExecutionStorage handlerExecutionStorage;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutionStorage = new HandlerExecutionStorage();
    }

    @Override
    public void initialize() {
        Reflections reflections = new Reflections(basePackage);
        for (Class<?> controller : reflections.getTypesAnnotatedWith(Controller.class)) {
            registerMethodAnnotatedWithRequestMappingAsHandler(controller);
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        return handlerExecutionStorage.get(new HandlerKey(request));
    }

    private void registerMethodAnnotatedWithRequestMappingAsHandler(final Class<?> controller) {
        Object instance = newInstance(controller);
        Method[] declaredMethods = controller.getDeclaredMethods();

        for (Method method : declaredMethods) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                HandlerExecution handlerExecution = new HandlerExecution(instance, method);
                RequestMapping annotation = method.getAnnotation(RequestMapping.class);

                List<HandlerKey> handlerKeys = new RequestMappingInfo(
                        annotation.value(),
                        annotation.method()
                ).handlerKeys();
                handlerExecutionStorage.add(handlerExecution, handlerKeys);
            }
        }
    }

    private Object newInstance(final Class<?> controller) {
        try {
            return controller.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("컨트롤러 인스턴스 생성 실패: " + controller.getName(), e);
        }
    }
}
