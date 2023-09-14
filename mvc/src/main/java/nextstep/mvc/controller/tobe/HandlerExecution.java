package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import nextstep.mvc.view.ModelAndView;

import java.lang.reflect.Method;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class HandlerExecution {

    private final Object controller;
    private final Method method;

    public static HandlerExecution of(final Object controller, final Method method) {
        return new HandlerExecution(controller, method);
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return (ModelAndView) method.invoke(controller, request, response);
    }
}
