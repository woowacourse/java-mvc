package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class HandlerExecution {

    private final Object controller;
    private final Method method;

    public static HandlerExecution of(final Object controller, final Method method) {
        validateModifier(method);

        return new HandlerExecution(controller, method);
    }

    private static void validateModifier(final Method method) {
        if (!Modifier.isPublic(method.getModifiers())) {
            throw new IllegalStateException("@RequestMapping 메서드는 public 이어야 합니다: "
                    + method.getDeclaringClass().getName() + "#" + method.getName());
        }
    }

    public ModelAndView handle(
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws Exception {
        final Object modelAndView = invoke(request, response);
        validateNullReturnValue(modelAndView);
        validateReturnType(modelAndView);
        return (ModelAndView) modelAndView;
    }

    private Object invoke(
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws Exception {
        try {
            return method.invoke(controller, request, response);
        } catch (final InvocationTargetException e) {
            final Throwable target = e.getTargetException();
            if (target instanceof Exception) {
                throw (Exception) target;
            }
            throw new RuntimeException(target);
        }
    }

    private void validateNullReturnValue(final Object modelAndView) {
        if (modelAndView == null) {
            throw new IllegalStateException("핸들러가 null을 반환했습니다: "
                    + method.getDeclaringClass().getName() + "#" + method.getName());
        }
    }

    private void validateReturnType(final Object modelAndView) {
        if (modelAndView instanceof ModelAndView) {
            return;
        }
        throw new IllegalStateException("컨트롤러의 @RequestMapping이 붙어있다면, ModelAndView를 반환해야 합니다");
    }
}
