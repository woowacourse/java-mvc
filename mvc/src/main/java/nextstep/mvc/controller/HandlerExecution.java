package nextstep.mvc.controller;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.ModelAndView;

public class HandlerExecution {

    private final static Logger log = LoggerFactory.getLogger(HandlerExecution.class);

    private final Object clazz;
    private final Method method;

    public HandlerExecution(final Object clazz, final Method method) {
        this.clazz = clazz;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final ModelAndView modelAndView = (ModelAndView) method.invoke(clazz, request, response);
        log.info("execute controller method [{}]", method);
        return modelAndView;
    }

    @Override
    public String toString() {
        return "HandlerExecution{" +
            "clazz=" + clazz +
            ", method=" + method +
            '}';
    }
}
