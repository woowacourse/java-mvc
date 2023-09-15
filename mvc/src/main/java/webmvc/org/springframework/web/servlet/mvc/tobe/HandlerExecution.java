package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.tobe.exception.InvalidMethodException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Object classInstance;
    private final Method method;

    public HandlerExecution(final Object classInstance, final Method method) {
        this.classInstance = classInstance;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) {
        try {
            return (ModelAndView) method.invoke(classInstance, request, response);
        } catch (IllegalAccessException |
                 InvocationTargetException e) {
            throw new InvalidMethodException("메서드를 실행할 수 없습니다.");
        }
    }
}
