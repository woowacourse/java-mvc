package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

public class HandlerExecution {

    private final HandlerMethod handlerMethod;

    public HandlerExecution(HandlerMethod handlerMethod) {
        this.handlerMethod = handlerMethod;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        Object result = handlerMethod.getMethod()
                .invoke(handlerMethod.getBean(), request, response);
        if (result instanceof String) {
            return new ModelAndView(new JspView((String) result));
        }
        return (ModelAndView) result;
    }
}
