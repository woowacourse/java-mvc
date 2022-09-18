package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.view.ModelAndView;

public class HandlerExecution {

    private Object target;
    private Method method;

    public HandlerExecution(Object target, Method method) {
        this.target = target;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return (ModelAndView) method.invoke(target, request, response);
    }
}
