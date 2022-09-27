package nextstep.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.ModelAndView;

public interface HandlerAdapter {
    boolean supports(final Object handler);

    ModelAndView handle(final HttpServletRequest req, final HttpServletResponse res, final Object handler)
            throws Exception;
}
