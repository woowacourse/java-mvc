package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerScanner {

    private static final Logger log = LoggerFactory.getLogger(ControllerScanner.class);

    public static Map<Object, List<Method>> scan(Object packageName){
        Map<Object, List<Method>> scanResult = new HashMap<>();
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        controllers.forEach(controller -> {
            Object controllerInstance = createControllerInstance(controller);

            if (controllerInstance != null) {
                List<Method> requestMappingMethods = getRequestMappingMethods(controller);
                scanResult.put(controllerInstance, requestMappingMethods);
            }
        });
        return scanResult;
    }

    private static Object createControllerInstance(Class<?> controllerClass) {
        try {
            return controllerClass.getConstructor().newInstance();
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("생성자를 호출할 수 없습니다. " + controllerClass.getName());
        } catch (InstantiationException e) {
            log.error("인스턴스화 할 수 없습니다. " + controllerClass.getName());
        }
        return null;
    }

    private static List<Method> getRequestMappingMethods(Class<?> controller) {
        return Arrays.stream(controller.getMethods())
                .filter(ControllerScanner::isRequestMappingAnnotated)
                .toList();
    }

    private static boolean isRequestMappingAnnotated(Method method) {
        return method.getAnnotation(RequestMapping.class) != null;
    }
}
