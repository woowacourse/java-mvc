package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.ModelAndView;

import java.lang.reflect.Method;

public class HandlerExecution {

    public HandlerExecution(final Object controllerInstance, final Method method) {
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return null;
    }
}
