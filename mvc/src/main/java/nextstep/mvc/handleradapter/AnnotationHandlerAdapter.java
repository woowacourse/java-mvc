package nextstep.mvc.handleradapter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.handlermapping.HandlerExecution;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;

public class AnnotationHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(final Object handler) {
        return handler.getClass().isAnnotationPresent(Controller.class);
    }

    @Override
    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        return ((HandlerExecution) handler).handle(request, response);
    }
}
