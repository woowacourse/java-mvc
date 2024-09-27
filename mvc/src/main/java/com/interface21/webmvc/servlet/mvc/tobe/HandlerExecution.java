package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.bean.container.BeanContainer;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class HandlerExecution {

    private final Method handler;

    public HandlerExecution(Method handler) {
        this.handler = handler;
    }

    public Object handle(Object... args) throws Exception {
        Class<?> clazz = handler.getDeclaringClass();
        Object controller = BeanContainer.getInstance().getBean(clazz);

        return handler.invoke(controller, args);
    }

    public Parameter[] getParameters() {
        return handler.getParameters();
    }
}
