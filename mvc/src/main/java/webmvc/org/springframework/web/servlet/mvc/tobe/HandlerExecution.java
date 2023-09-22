package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import webmvc.org.springframework.web.servlet.ModelAndView;

public class HandlerExecution {

    private final Object target;
    private final Method method;

    public HandlerExecution(final Object target, final Method method) {
        this.target = target;
        this.method = method;
    }

    public ModelAndView execute(final HttpServletRequest request, final HttpServletResponse response) {
        try {
            return (ModelAndView) method.invoke(target, request, response);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException("해당 controller를 실행시킬 수 없습니다.", e);
        }
    }
}
