package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import webmvc.org.springframework.web.servlet.view.ModelAndView;

public class HandlerExecution {

    private final Method handler;
    private final Object object;

    public HandlerExecution(Method handler, Object object) {
        this.handler = handler;
        this.object = object;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return (ModelAndView) handler.invoke(object, request, response);
    }

}
