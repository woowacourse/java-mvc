package nextstep.mvc.handler.adaptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.view.ModelAndView;

public class AnnotationHandlerAdaptor implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return handler instanceof AnnotationHandlerMapping;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response,
                               Object handler) throws Exception {
        AnnotationHandlerMapping cast = (AnnotationHandlerMapping) handler;
        HandlerExecution handler1 = cast.getHandler(request);

        return handler1.handle(request, response);
    }
}
