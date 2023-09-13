package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import webmvc.org.springframework.web.servlet.ModelAndView;

public class HandlerExecution {

    private final Class<?> controllerClass;
    private final Method method;

    public HandlerExecution(Class<?> controllerClass, Method method) {
        this.controllerClass = controllerClass;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        /// TODO: 2023/09/13 Class<?> 타입으로 괜찮은가? Constructor 매번 만들어줘야 하나?
        final Constructor<?> constructor = controllerClass.getConstructor();

        return (ModelAndView) method.invoke(constructor.newInstance(), request, response);
    }
}
