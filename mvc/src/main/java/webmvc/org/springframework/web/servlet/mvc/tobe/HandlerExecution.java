package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

public class HandlerExecution {

    private final Method method;

    public HandlerExecution(final Method method) {
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final Object returnValue = method
                .invoke(method.getDeclaringClass().getDeclaredConstructor().newInstance(), request, response);
        if (returnValue instanceof String) {
            return new ModelAndView(new JspView((String) returnValue));
        }
        if (returnValue instanceof ModelAndView) {
            return (ModelAndView) returnValue;
        }
        throw new IllegalArgumentException("리턴타입이이상해욥");
    }
}
