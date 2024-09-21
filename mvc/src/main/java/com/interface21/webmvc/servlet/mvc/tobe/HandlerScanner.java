package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class HandlerScanner {

    private static final Logger log = LoggerFactory.getLogger(HandlerScanner.class);

    private HandlerScanner() {
    }

    public static List<Handler> scanHandlers(Object[] basePackage) {
        List<Handler> handlers = new ArrayList<>();
        for (Class<?> controller : scanControllers(basePackage)) {
            for (Method method : scanControllerMethods(controller)) {

                Object instanceController = instantiateController(controller);
                if (instanceController == null) continue;

                handlers.add(new Handler(instanceController, method));
            }
        }

        return handlers;
    }

    private static Set<Class<?>> scanControllers(Object[] basePackage) {
        Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private static List<Method> scanControllerMethods(Class<?> controller) {
        return Arrays.stream(controller.getMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .toList();
    }

    private static Object instantiateController(Class<?> controller) {
        try {
            Constructor<?> constructor = controller.getConstructor();
            return constructor.newInstance();
        } catch (NoSuchMethodException exception) {
            log.error("생성자를 호출할 수 없습니다.");
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException exception) {
            log.error("인스턴스를 생성할 수 없습니다.");
        }

        return null;
    }
}
