package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.view.ModelAndView;

public class AnnotationHandlerAdaptor implements HandlerAdapter {

    @Override
    public boolean supports(final Object handler) {
        return false;
    }

    @Override
    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response,
        final Object handler) {
        return null;
    }
}
