package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import webmvc.org.springframework.web.servlet.ModelAndView;

public class HandlerExecution {

    public final Method method;

    public HandlerExecution(Method method) {
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return (ModelAndView) method.invoke(getInstance(), request, response);
    }

    private Object getInstance() throws ReflectiveOperationException {
        return method.getDeclaringClass()
                .getConstructor()
                .newInstance();
    }
}
