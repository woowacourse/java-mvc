package nextstep.mvc.controller.tobe.adapter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.NullView;

public class VoidHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        if (handler instanceof HandlerExecution) {
            return ((HandlerExecution) handler).hasReturnTypeOf(Void.TYPE);
        }
        return false;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        ((HandlerExecution) handler).handle(request, response);
        return new ModelAndView(new NullView());
    }
}
