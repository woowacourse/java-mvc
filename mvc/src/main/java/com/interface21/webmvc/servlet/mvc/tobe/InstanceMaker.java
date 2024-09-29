package com.interface21.webmvc.servlet.mvc.tobe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class InstanceMaker {

    private static final Logger log = LoggerFactory.getLogger(InstanceMaker.class);

    public static Object makeInstance(Class<?> clazz, Object... arg) {
        try {
            Class<?>[] parameterTypes = makeParamFrom(arg);
            return clazz.getDeclaredConstructor(parameterTypes).newInstance(arg);
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

    private static Class<?>[] makeParamFrom(Object[] arg) {
        return Arrays.stream(arg)
                .map(Object::getClass)
                .toArray(Class<?>[]::new);
    }
}
