package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.exception.MethodException;

public class HandlerExecution {

    private final Method method;
    private final Object instance;

    public HandlerExecution(Method method, Object instance) {
        this.method = method;
        this.instance = instance;
    }


    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) {
        try {
            return (ModelAndView) method.invoke(instance, request, response);
        } catch (IllegalAccessException e) {
            throw new MethodException(String.format("%s: 해당 메서드에 접근하지 못했습니다.", method.getName()), e);
        } catch (InvocationTargetException e) {
            throw new MethodException(String.format("%s: 메서드 호출 중 에러가 발생했습니다.", method.getName()), e);
        }
    }

}
