package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.support.RequestMethod;

public class HandlerExecution {

    private final Method method;

    public HandlerExecution(Method method) {
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        Class<?> declaringClass = method.getDeclaringClass();
        Object instance = declaringClass.getDeclaredConstructor().newInstance();
        return (ModelAndView) method.invoke(instance, request, response);
    }
}
