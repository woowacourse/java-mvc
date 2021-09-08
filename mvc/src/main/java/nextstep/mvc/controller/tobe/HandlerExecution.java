package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import nextstep.mvc.view.ModelAndView;

public class HandlerExecution {

    private final Class<?> clazz;
    private final Method handlerMethod;

    public HandlerExecution(Class<?> clazz, Method handlerMethod) {
        this.clazz = clazz;
        this.handlerMethod = handlerMethod;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return (ModelAndView) handlerMethod.invoke(
            clazz.getConstructor().newInstance(),
            request,
            response
        );
    }
}
