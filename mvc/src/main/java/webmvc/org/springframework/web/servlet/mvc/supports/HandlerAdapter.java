package webmvc.org.springframework.web.servlet.mvc.supports;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.mvc.supports.adapter.ModelAndView;

public interface HandlerAdapter {

    boolean supports(final Object handler);

    ModelAndView execute(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler
    ) throws Exception;
}
