package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

public class HandlerExecution {

    private final Method method;
    private final Object handler;

    public HandlerExecution(Method method, Object handler) {
        this.method = method;
        this.handler = handler;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return new ModelAndView(new JspView((String) method.invoke(handler, request, response)));
    }
}
