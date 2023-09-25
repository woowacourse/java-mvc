package webmvc.org.springframework.web.servlet.mvc.handler.annoationhandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.frontcontroller.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.handler.HandlerExecution;
import webmvc.org.springframework.web.servlet.view.JsonView;
import webmvc.org.springframework.web.servlet.view.JspView;

public class AnnotationHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean isSupporting(final Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public ModelAndView handle(final HttpServletRequest request,
                               final HttpServletResponse response,
                               final Object handler) throws Exception {
        final HandlerExecution handlerExecution = (HandlerExecution) handler;
        final Object result = handlerExecution.handle(request, response);

        if (result instanceof ModelAndView) {
            return (ModelAndView) result;
        }

        if (result instanceof String) {
            return new ModelAndView(new JspView((String) result));
        }

        final ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("model", result);
        return modelAndView;
    }
}
