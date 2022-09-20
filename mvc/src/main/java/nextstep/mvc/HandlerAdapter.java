package nextstep.mvc;

import nextstep.mvc.view.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface HandlerAdapter {
    boolean supports(Object handler);

    ModelAndView handle(HttpServletRequest request, HttpServletResponse response, final Object handler) throws Exception;

    void render(final ModelAndView modelAndView, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception;
}
