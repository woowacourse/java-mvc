package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

import java.lang.reflect.Method;

public class ViewNameHandlerExecution implements HandlerExecution {

    private final Object instance;
    private final Method method;

    public ViewNameHandlerExecution(Object instance, Method method) {
        this.instance = instance;
        this.method = method;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String viewName = (String) method.invoke(instance, request, response);
        return new ModelAndView(new JspView(viewName));
    }
}
