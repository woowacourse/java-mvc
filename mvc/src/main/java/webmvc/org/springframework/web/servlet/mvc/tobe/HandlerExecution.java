package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Method;

public class HandlerExecution {


    private final Object classOfMethod;
    private final Method method;

    public HandlerExecution(final Object classOfMethod, final Method method) {
        this.classOfMethod = classOfMethod;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return (ModelAndView) method.invoke(classOfMethod, request, response);
    }
}
