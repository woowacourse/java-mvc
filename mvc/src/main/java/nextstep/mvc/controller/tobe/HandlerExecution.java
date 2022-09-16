package nextstep.mvc.controller.tobe;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.ModelAndView;

public class HandlerExecution {

    private HandlerExecution() {
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return null;
    }

    public static HandlerExecution of(Class<?> handler, Method method) {
        return new HandlerExecution() {
            @Override
            public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
                Constructor<?> constructor = handler.getConstructor();
                return (ModelAndView)method.invoke(constructor.newInstance(), request, response);
            }
        };
    }
}
