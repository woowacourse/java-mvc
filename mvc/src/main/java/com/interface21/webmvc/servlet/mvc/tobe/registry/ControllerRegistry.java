package com.interface21.webmvc.servlet.mvc.tobe.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class ControllerRegistry {

    private static final Logger log = LoggerFactory.getLogger(ControllerRegistry.class);
    private static final Map<Class<?>, Object> controllers = new HashMap<>();

    private ControllerRegistry() {
    }

    public static Object getOrCreateController(Class<?> controller) {
        if (controllers.containsKey(controller)) {
            return controllers.get(controller);
        }
        Object controllerInstance = makeControllerInstance(controller);
        controllers.put(controller, controllerInstance);

        return controllerInstance;
    }

    private static Object makeControllerInstance(Class<?> controller) {
        try {
            Constructor<?> constructor = controller.getConstructor();
            Object controllerInstance = constructor.newInstance();

            return controllerInstance;
        } catch (NoSuchMethodException e) {
            log.error("메서드를 찾지 못했습니다. {}", e.getStackTrace()[0]);
            throw new IllegalArgumentException("메서드를 찾지 못했습니다.");
        } catch (IllegalAccessException e) {
            log.error("생성자에 접근할 수 없습니다. {}", e.getStackTrace()[0]);
            throw new IllegalStateException("생성자에 접근할 수 없습니다.");
        } catch (ExceptionInInitializerError e) {
            log.error("메서드를 초기화 하던 도중 실패했습니다. {}", e.getStackTrace()[0]);
            throw new IllegalStateException("메서드를 초기화 하던 도중 실패했습니다.");
        } catch (InvocationTargetException | InstantiationException e) {
            log.error("생성자를 실행하던 도중 실패했습니다. {}", e.getStackTrace()[0]);
            throw new IllegalStateException("생성자를 실행하던 도중 실패했습니다.");
        }
    }
}
