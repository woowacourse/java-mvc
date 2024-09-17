package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.Constructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.core.util.ReflectionUtils;

public class ConstructorGenerator {

    private static final Logger log = LoggerFactory.getLogger(ConstructorGenerator.class);

    private ConstructorGenerator() {
    }

    public static Object generate(final Class<?> clazz, final Class<?>... parameterTypes) {
        try {
            final Constructor<?> constructor = ReflectionUtils.accessibleConstructor(clazz, parameterTypes);
            return constructor.newInstance((Object[]) parameterTypes);
        } catch (final ReflectiveOperationException e) {
            log.warn(e.getMessage(), e);
            throw new IllegalStateException("객체 생성에 실패하였습니다.");
        }
    }
}
