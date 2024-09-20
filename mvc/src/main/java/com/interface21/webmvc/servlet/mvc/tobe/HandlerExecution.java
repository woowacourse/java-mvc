package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.BeanFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.webmvc.servlet.ModelAndView;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Method method;

    public HandlerExecution(Method hadlerMethod) {
        this.method = hadlerMethod;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        BeanFactory beanFactory = BeanFactory.getInstance();
        Object handler = beanFactory.getBean(method.getDeclaringClass());
        return (ModelAndView) method.invoke(handler, request, response);
    }
}
