package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.ModelAndView;

import java.lang.reflect.Method;

public class HandlerExecution {


    private final Method handler;
    private final Object instance;

    public HandlerExecution(Method handler, Object instance) {
        this.handler = handler;
        this.instance = instance;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        handler.setAccessible(true);
        return (ModelAndView) handler.invoke(instance, request, response);
    }
}
