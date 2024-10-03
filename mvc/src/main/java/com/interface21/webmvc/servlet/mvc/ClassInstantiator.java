package com.interface21.webmvc.servlet.mvc;

import java.lang.reflect.Modifier;

public class ClassInstantiator {

    public static <T extends Object> T Instantiate(final Class<T> clazz) {
        if (isNotInstantiable(clazz)) {
            throw new IllegalArgumentException("인스턴스화가 불가능한 타입입니다. " + clazz.getName());
        }

        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("인스턴스 생성에 실패하였습니다.", e);
        }
    }

    private static boolean isNotInstantiable(final Class<?> clazz) {
        return clazz.isInterface()
                || Modifier.isAbstract(clazz.getModifiers())
                || clazz.isAnnotation()
                || clazz.isEnum()
                || clazz.isPrimitive()
                || clazz.isArray()
                || clazz == Void.class;
    }
}
