package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Method method;

    public HandlerExecution(final Method method){
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return (ModelAndView) method.invoke(getHandlerInstance(), request, response);
    }

    private Object getHandlerInstance() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return method.getDeclaringClass().getDeclaredConstructor().newInstance();
    }
}
