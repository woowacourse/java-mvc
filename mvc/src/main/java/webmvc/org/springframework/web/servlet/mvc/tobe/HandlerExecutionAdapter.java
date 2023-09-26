package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import webmvc.org.springframework.web.servlet.ModelAndView;

public class HandlerExecutionAdapter implements HandlerAdapter {

    @Override
    public boolean canAdapt(final Object controller) {
        return controller instanceof HandlerExecution;
    }

    @Override
    public ModelAndView adapt(Object controller, HttpServletRequest req, HttpServletResponse res)
            throws InvocationTargetException, IllegalAccessException {
        HandlerExecution handler = (HandlerExecution) controller;
        return handler.handle(req, res);
    }

}
