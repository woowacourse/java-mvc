package nextstep.mvc.controller.tobe;

import java.lang.reflect.Method;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.ModelAndView;

public class HandlerExecution {

    private final Method method;

    public HandlerExecution(Method method) {
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return null;
    }
}
