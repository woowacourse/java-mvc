package nextstep.mvc.controller.tobe;

import java.lang.reflect.Method;
import nextstep.mvc.view.ModelAndView;

public class HandlerExecution {

    private final Method method;
    private final Object handler;

    public HandlerExecution(Method method, Object handler) {
        this.method = method;
        this.handler = handler;
    }

    public ModelAndView handle(Object... parameters)
        throws Exception {
        return (ModelAndView) method.invoke(handler, parameters);
    }

    public Method getMethod() {
        return method;
    }
}
