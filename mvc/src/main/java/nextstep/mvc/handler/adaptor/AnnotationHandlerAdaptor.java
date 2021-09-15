package nextstep.mvc.handler.adaptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;

public class AnnotationHandlerAdaptor implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response,
                               Object handler) throws Exception {
        HandlerExecution cast = (HandlerExecution) handler;
        Object handle = cast.handle(request, response);

        if(handle instanceof String) {
            return new ModelAndView(new JspView((String )handle));
        }

        return (ModelAndView) handle;
    }
}
