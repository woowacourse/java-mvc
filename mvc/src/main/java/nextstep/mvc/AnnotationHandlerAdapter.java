package nextstep.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.View;
import nextstep.web.annotation.Controller;

public class AnnotationHandlerAdapter implements HandlerAdapter{

    @Override
    public boolean supports(final Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response,
                               final Object handler) throws Exception {
        return ((HandlerExecution) handler).handle(request, response);
    }

    @Override
    public void render(final ModelAndView modelAndView, final HttpServletRequest request,
                       final HttpServletResponse response) throws Exception {
        final Map<String, Object> model = modelAndView.getModel();
        final View view = modelAndView.getView();
        view.render(model, request, response);
    }
}
