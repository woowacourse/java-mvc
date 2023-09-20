package webmvc.org.springframework.web.servlet.mvc.tobe.handler.mapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Method;

public class HandlerExecution {

    private final Method method;

    public HandlerExecution(final Method method) {
        this.method = method;
    }

    public ModelAndView execute(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        Object controller = method.getDeclaringClass()
                .getConstructor()
                .newInstance();

        return (ModelAndView) method.invoke(controller, req, res);
    }
}
