package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.ModelAndView;

import java.lang.reflect.Method;

public class HandlerExecution {


    private final Method handler;
    private final Class<?> controller;

    public HandlerExecution(Method handler, Class<?> controller) {
        this.handler = handler;
        this.controller = controller;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object instance = controller.getDeclaredConstructor().newInstance();
        handler.setAccessible(true);
        return (ModelAndView) handler.invoke(instance, request, response);
    }
}
