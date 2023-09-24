package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.HandlerAdapter;

public class HandlerExecutionHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean isSupport(final Object object) {
        return object instanceof HandlerExecution;
    }

    @Override
    public ModelAndView handle(final Object object, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        HandlerExecution handlerExecution = (HandlerExecution) object;

        return (ModelAndView) handlerExecution.handle(request, response);
    }
}
