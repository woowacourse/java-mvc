package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;

public class AnnotationHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(final Object handler) {
        return handler instanceof Handleable;
    }

    @Override
    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response,
                               final Object handler)
            throws Exception {
        Handleable handleable = (Handleable) handler;
        final Object result = handleable.handle(request, response);
        if (handleable instanceof HandlerExecution) {
            return (ModelAndView) result;
        }
        return new ModelAndView(new JspView(String.valueOf(result)));
    }
}
