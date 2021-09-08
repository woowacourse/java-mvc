package nextstep.mvc.handleradapter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.controller.tobe.ResourceHandler;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.ResourceView;

public class ResourceHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(final Object handler) {
        return ResourceHandler.class.isAssignableFrom(handler.getClass());
    }

    @Override
    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        return new ModelAndView(new ResourceView(((ResourceHandler) handler).handle()));
    }
}
