package nextstep.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.HandlerExecution;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;

public class RequestMappingHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(final Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response,
                               final Object handler) throws Exception {
        final var handlerExecution = (HandlerExecution) handler;
        final Object handle = handlerExecution.handle(request, response);

        if (handle instanceof String) {
            return new ModelAndView(new JspView((String) handle));
        }

        return (ModelAndView) handle;
    }
}
