package nextstep.mvc.controller.tobe.fixture;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.view.ModelAndView;

public class FakeHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(final Object handler) {
        return true;
    }

    @Override
    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response,
                               final Object handler)
            throws Exception {
        return null;
    }
}
