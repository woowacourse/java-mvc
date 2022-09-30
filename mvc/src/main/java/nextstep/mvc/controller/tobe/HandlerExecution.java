package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;

public class HandlerExecution implements Handleable{

    private final Method method;
    private final Object instance;

    public HandlerExecution(final Method method, final Object instance) {
        this.method = method;
        this.instance = instance;
    }

    @Override
    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response)
            throws InvocationTargetException, IllegalAccessException {
        final Object result = method.invoke(instance, request, response);
        if (result instanceof String) {
            final String viewName = (String) result;
            return new ModelAndView(new JspView(viewName));
        }
        return (ModelAndView) result;
    }
}
