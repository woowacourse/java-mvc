package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;

public class ManualHandlerAdapter implements HandlerAdapter{

    @Override
    public boolean supports(final Object handler) {
        return false;
    }

    @Override
    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response,
        final Object handler)
        throws Exception {
        return null;
    }
}
