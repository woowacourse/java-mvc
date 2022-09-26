package nextstep.mvc.controller.tobe.adapter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.view.ModelAndView;

public class ModelAndViewHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        if (handler instanceof HandlerExecution) {
            return ((HandlerExecution) handler).hasReturnTypeOf(ModelAndView.class);
        }
        return false;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        return (ModelAndView) ((HandlerExecution) handler).handle(request, response);
    }
}
