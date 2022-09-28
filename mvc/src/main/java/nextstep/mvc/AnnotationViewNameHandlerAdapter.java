package nextstep.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;

public class AnnotationViewNameHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(final Object handler) {
        return handler instanceof HandlerExecution &&
                ((HandlerExecution)handler).isReturnTypeViewName(String.class);
    }

    @Override
    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response,
                               final Object handler) throws Exception {
        return new ModelAndView(new JspView(
                (String)((HandlerExecution) handler).handle(request, response)
        )
        );
    }
}
