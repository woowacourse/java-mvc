package webmvc.org.springframework.web.servlet.mvc.reflection;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;

import java.lang.reflect.Method;

public class ReflectiveHandlerExecutionAdapter extends HandlerExecution {

    private final Object instance;
    private final Method method;

    public ReflectiveHandlerExecutionAdapter(Object instance, Method method) {
        this.instance = instance;
        this.method = method;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return (ModelAndView) method.invoke(instance, request, response);
    }
}
