package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.core.util.ReflectionUtils;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandlerExecution {

    private static final Logger log = LoggerFactory.getLogger(HandlerExecution.class);
    private Class<?> clazz;
    private Method method;

    public HandlerExecution(Class<?> clazz, Method method) {
        this.clazz = clazz;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        log.info("clazz = {}, method = {}", clazz.getName(), method.getName());
        Constructor<?> constructor = ReflectionUtils.accessibleConstructor(clazz);

        // 매번 인스턴스를 새로 만들어서 처리한다는 단점이 존재한다.
        Object controller = constructor.newInstance();
        return (ModelAndView) method.invoke(controller, request, response);
    }
}
