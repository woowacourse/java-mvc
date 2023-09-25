package webmvc.org.springframework.web.servlet.mvc.handlerMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import webmvc.org.springframework.web.servlet.ModelAndView;

public class HandlerExecution {

    private final Object object;
    private final Method method;

    public HandlerExecution(final Object object, final Method method) {
        this.object = object;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response)
            throws InvocationTargetException, IllegalAccessException {
        return (ModelAndView) method.invoke(object, request, response);
    }
}
