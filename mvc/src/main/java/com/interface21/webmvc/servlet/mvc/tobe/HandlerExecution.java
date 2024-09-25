package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Method method;

    public HandlerExecution(Method method) {
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) {
        try {
            Class<?> declaringClass = method.getDeclaringClass();
            Constructor<?> constructor = declaringClass.getConstructor();
            Object controller = constructor.newInstance();
            return (ModelAndView) method.invoke(controller, request, response);
        } catch (InvocationTargetException e) {
            throw new HandlerExecutionException("메서드 호출 중 오류 발생: " + e.getCause().getMessage());
        } catch (IllegalAccessException e) {
            throw new HandlerExecutionException("메서드 접근이 불가능합니다: " + e.getMessage());
        } catch (NoSuchMethodException e) {
            throw new HandlerExecutionException("메서드를 찾을 수 없습니다: " + e.getMessage());
        } catch (InstantiationException e) {
            throw new HandlerExecutionException("컨트롤러 인스턴스화에 실패했습니다: " + e.getMessage());
        }
    }
}
