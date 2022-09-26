package nextstep.mvc.adapter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.mapping.HandlerExecution;
import nextstep.mvc.view.ModelAndView;

public class ModelAndViewHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return ((HandlerExecution) handler).hasReturnTypeOf(ModelAndView.class);
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        return (ModelAndView) ((HandlerExecution) handler).handle(request, response);
    }
}
