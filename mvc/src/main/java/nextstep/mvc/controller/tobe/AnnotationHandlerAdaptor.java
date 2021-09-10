package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;

public class AnnotationHandlerAdaptor implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return handler.getClass().isAnnotationPresent(Controller.class);
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerExecution handlerExecution = (HandlerExecution) handler;
        return handlerExecution.handle(request, response);
    }

}
