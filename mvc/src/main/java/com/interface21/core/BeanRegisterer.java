package com.interface21.core;

import com.interface21.context.stereotype.Controller;
import org.reflections.Reflections;

public class BeanRegisterer {

    private static final SingletonBeanContainer container = SingletonBeanContainer.getInstance();

    private BeanRegisterer() {
    }

    public static void registerBeans(Class<?> basePackageClass) {
        Reflections reflections = new Reflections(basePackageClass.getPackageName());
        reflections.getTypesAnnotatedWith(Controller.class)
                .forEach(container::createSingleton);
    }
}
